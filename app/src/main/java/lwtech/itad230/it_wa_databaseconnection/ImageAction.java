package lwtech.itad230.it_wa_databaseconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ImageAction extends Fragment implements View.OnClickListener {
    private ImageView imageView;
    private Button addTravelList, addDonationList,addOutfit, removeImage;
    String imagePath;
    private TextView imageLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_action,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image_preview);

        imagePath = SharedPrefManager.getInstance(getActivity()).getCurrentImagePath();

        Glide.with(this).load(imagePath).into(imageView);

        imageLocation = view.findViewById(R.id.location);
        imageLocation.setText("Location: "+SharedPrefManager.getInstance(getActivity()).getCurrentLocation());


        addTravelList = view.findViewById(R.id.add_travel);
        addTravelList.setOnClickListener(this);

        addDonationList = view.findViewById(R.id.add_donation);
        addDonationList.setOnClickListener(this);

        addOutfit = view.findViewById(R.id.add_outfit);
        addOutfit.setOnClickListener(this);

        removeImage = view.findViewById(R.id.delete_item);
        removeImage.setOnClickListener(this);

        dateUpdate(imagePath);
    }
    @Override
    public void onClick(View view) {
        if(view == addTravelList)
        {
            addToList("travel_list");
        }

        if(view == addDonationList)
        {
            addToList("donation_list");
        }

        if(view == addOutfit)
        {
            addToOutfitList();
        }

        if(view == removeImage)
        {
            deleteItem();
        }

    }

    private void addToList(String list)
    {

        final String list_type = list;
        final String item_id = SharedPrefManager.getInstance(getActivity()).getCurrentImageId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ADDLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("list_type",list_type);
                params.put("item_id",item_id);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void addToOutfitList()
    {

        final String item_id = SharedPrefManager.getInstance(getActivity()).getCurrentImageId();
        final String outfit_name = SharedPrefManager.getInstance(getActivity()).getCurrentOutfitName();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CREATEOUTFIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation","add");
                params.put("outfit_name",outfit_name);
                params.put("item_id",item_id);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void deleteItem()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getActivity(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("delete_type","delete_item");
                params.put("image_path",imagePath);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void dateUpdate(String image_path) {
        final String image_path_url = image_path;

        StringRequest dateRequest = new StringRequest(Request.Method.POST, Constants.URL_LAST_VIEWED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getActivity(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image_path", image_path_url);

                return params;
            } };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(dateRequest);
    }

}
