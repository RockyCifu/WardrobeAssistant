package lwtech.itad230.it_wa_databaseconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseWardrobe extends android.support.v4.app.Fragment{
    private ArrayList<String> imagePaths = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browse_wardrobe,null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getImagesFromServer(view);
    }

    private void displayImages(View view,JSONObject obj){

        //for loop should run 1 less than total rows - first row is error status
        for(int i=1;i<obj.length();i++)
        {
            JSONObject temp = null;
            try {
                temp = new JSONObject(obj.getString("row"+i));
                //Add image path url to arraylist
                imagePaths.add(temp.getString("image_path"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        initRecyclerView(view);
    }

    private void initRecyclerView(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),imagePaths);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Hayden's code goes here
                dateUpdate(imagePaths.get(position));
                Toast.makeText(getActivity(), position+ " Last view date updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getImagesFromServer(View current_view)
    {
        final String filter_type = SharedPrefManager.getInstance(getActivity()).getFilterType();
        final String filter_value = SharedPrefManager.getInstance(getActivity()).getFilterValue();
        final View view = current_view;

        Toast.makeText(getActivity(), filter_type+" "+filter_value, Toast.LENGTH_SHORT).show();
        //progressDialog.setMessage("Loading.....");
        //progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BROWSEWARDROBE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                displayImages(view, obj);
                            }
                            else
                            {
                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filter_type",filter_type);
                params.put("filter_value",filter_value);
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

