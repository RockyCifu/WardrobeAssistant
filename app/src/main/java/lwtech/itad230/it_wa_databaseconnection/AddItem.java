package lwtech.itad230.it_wa_databaseconnection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddItem extends android.support.v4.app.Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String phpUrl = Constants.URL_ADDITEM;
    private Button submit,takePic;
    private Spinner type,season,color,location;
    private static String imageFilePath;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);

        takePic = view.findViewById(R.id.btn_take_picture);
        takePic.setOnClickListener(this);

        imageView = view.findViewById(R.id.image_preview);

        type = view.findViewById(R.id.apparel_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        season = view.findViewById(R.id.season_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.season_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(adapter);

        color = view.findViewById(R.id.item_color_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(adapter);

        location = view.findViewById(R.id.location_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view == submit){
            if(valuesSet()){
                uploadItem();
                Toast.makeText(getActivity(), "Upload Successful",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_LONG).show();
            }
        }

        if(view == takePic)
            openCameraIntent();

    }

    private boolean valuesSet() {
        return  imageView.getDrawable() != null && location.getSelectedItem().toString().length() > 0 &&
                color.getSelectedItem().toString().length() > 0 && type.getSelectedItem().toString().length() > 0 &&
                type.getSelectedItem().toString().length() > 0;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(pictureIntent.resolveActivity(getContext().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {}

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"lwtech.itad230.it_wa_databaseconnection.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile("tempPicture",".jpg",storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Glide.with(this).load(imageFilePath).into(imageView);
        }
    }

    private Bitmap getBitmapFromFile(){
        Bitmap img = BitmapFactory.decodeFile(imageFilePath);
        img = fixOrientation(img);

        File file = new File(imageFilePath);
        file.delete();

        return resizedBitmap(img, 480, 640);
    }

    private static Bitmap fixOrientation(Bitmap image){
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(image, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(image, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(image, 270);
            default:
                return image;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private static Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
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
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                params.put("color",color.getSelectedItem().toString());
                params.put("apparel_type",type.getSelectedItem().toString());
                params.put("season",season.getSelectedItem().toString());
                params.put("location",location.getSelectedItem().toString());
                params.put("image",imageToString(getBitmapFromFile()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}


