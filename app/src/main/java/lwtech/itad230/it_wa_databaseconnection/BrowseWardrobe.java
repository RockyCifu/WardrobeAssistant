package lwtech.itad230.it_wa_databaseconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

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
    private ArrayList<Integer> imageId = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();
    Fragment fragment_parent;

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String menuOptionSelected = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_parent = getParentFragment();
        return inflater.inflate(R.layout.browse_wardrobe,null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuOptionSelected = SharedPrefManager.getInstance(getActivity()).getMenuOptionSelected();
        if(menuOptionSelected.equals("filter") || (menuOptionSelected.equals("create_outfit"))) {
            getImagesFromServerFilter(view);
        }
        else if(menuOptionSelected.equals("travel_list") || menuOptionSelected.equals("donation_list")) {
            getImagesFromServerForList(view);
        }
        else if(menuOptionSelected.equals("view_outfit"))
        {
            getImagesFromServerForOutfit(view);
        }

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
                imageId.add(temp.getInt("item_id"));
                location.add(temp.getString("location"));
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
                //dateUpdate(imagePaths.get(position));

                if(menuOptionSelected.equals("filter") || menuOptionSelected.equals("create_outfit")) {
                    SharedPrefManager.getInstance(getActivity()).setCurrentImagePath(imagePaths.get(position));
                    SharedPrefManager.getInstance(getActivity()).setCurrentImageId(imageId.get(position));
                    SharedPrefManager.getInstance(getActivity()).setCurrentLocation(location.get(position));
                    fragment = new ImageAction();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.screen_area, fragment);
                    fragmentTransaction.addToBackStack("my_fragment");
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), position+ " Long click", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void getImagesFromServerFilter(View current_view)
    {
        final String filter_type = SharedPrefManager.getInstance(getActivity()).getFilterType();
        final String filter_value = SharedPrefManager.getInstance(getActivity()).getFilterValue();
        final View view = current_view;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BROWSEWARDROBE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filter_type",filter_type);
                params.put("filter_value",filter_value);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void getImagesFromServerForList(View current_view)
    {
        final String list_type = menuOptionSelected;
        final View view = current_view;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_READLIST,
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
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("list_type",list_type);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private void getImagesFromServerForOutfit(View current_view)
    {
        final View view = current_view;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CREATEOUTFIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation","read_outfit_items");
                params.put("outfit_name",SharedPrefManager.getInstance(getActivity()).getCurrentOutfit());
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}

