package lwtech.itad230.it_wa_databaseconnection;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddItem extends android.support.v4.app.Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String phpUrl = Constants.URL_ADDITEM;
    private Bitmap scaledBitmap;
    private Button submit,takePic;
    private Spinner type,season,color,location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submit = (Button)view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
        takePic = (Button)view.findViewById(R.id.btn_take_picture);
        takePic.setOnClickListener(this);

        type = (Spinner)view.findViewById(R.id.apparel_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        season = (Spinner)view.findViewById(R.id.season_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.season_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(adapter);

        color = (Spinner)view.findViewById(R.id.item_color_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(adapter);

        location = (Spinner)view.findViewById(R.id.location_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 160, 320, false);

            Drawable drawBitmap = new BitmapDrawable(getResources(), scaledBitmap);
            ImageView imageView = getActivity().findViewById(R.id.image_preview);
            imageView.setImageDrawable(drawBitmap);
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    private void uploadItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, phpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id", Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                params.put("color",color.getSelectedItem().toString());
                params.put("apparel_type",type.getSelectedItem().toString());
                params.put("season",season.getSelectedItem().toString());
                params.put("location",location.getSelectedItem().toString());
                params.put("image",imageToString(scaledBitmap));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view == submit){
            if(scaledBitmap == null || location.getSelectedItem().toString().length() <= 0 ||
                    color.getSelectedItem().toString().length() <= 0 || type.getSelectedItem().toString().length() <= 0 ||
                    type.getSelectedItem().toString().length() <= 0){
                Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_LONG).show();

            } else {
                uploadItem();
                Toast.makeText(getActivity(), "Upload Successful",
                        Toast.LENGTH_LONG).show();
                //startActivity(new Intent(getApplicationContext(), AddItem.class));
            }

        }

        if(view == takePic) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}


