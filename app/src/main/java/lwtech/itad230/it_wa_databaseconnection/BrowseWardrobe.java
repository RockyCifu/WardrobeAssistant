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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** BrowseWardrobe displays images from the server based upon the filtered selections
 */
public class BrowseWardrobe extends android.support.v4.app.Fragment{
    //Image path ArrayList
    private ArrayList<String> imagePaths = new ArrayList<>();

    //Image id ArrayList
    private ArrayList<Integer> imageId = new ArrayList<>();

    //Location ArrayList
    private ArrayList<String> location = new ArrayList<>();

    //Reference to fragment parent and fragment
    Fragment fragment_parent;
    Fragment fragment;

    //Reference to fragment manager
    FragmentManager fragmentManager;

    //Reference to FragmentTransaction
    FragmentTransaction fragmentTransaction;

    //Reference to menu option
    private String menuOptionSelected = null;

    @Override
    /**Method constructs the layout immediately upon BrowseWardrobe creation.
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_parent = getParentFragment();
        return inflater.inflate(R.layout.browse_wardrobe,null);
    }


    @Override
    /**Method is called after onCreateView is finished.
     * Calls the correct method of getting images depending on the filter selected
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Gets currently selected menu option
        menuOptionSelected = SharedPrefManager.getInstance(getActivity()).getMenuOptionSelected();

        //filter or create_outfit
        if(menuOptionSelected.equals("filter") || (menuOptionSelected.equals("create_outfit")))
            getImagesFromServerFilter(view);

        //travel_list or donation_list
        else if(menuOptionSelected.equals("travel_list") || menuOptionSelected.equals("donation_list"))
            getImagesFromServerForList(view);

        //view_outfit
        else if(menuOptionSelected.equals("view_outfit"))
            getImagesFromServerForOutfit(view);
    }

    /** Method populates the Array Lists from the returning query and initializes the RecyclerView
     */
    private void displayImages(View view,JSONObject obj){

        //Ignore first element because that is error response
        for(int i=1;i<obj.length();i++)
        {
            JSONObject temp;
            try {
                //Get data from JSONObject
                temp = new JSONObject(obj.getString("row"+i));

                //Adds image data to the Array Lists
                imagePaths.add(temp.getString("image_path"));
                imageId.add(temp.getInt("item_id"));
                location.add(temp.getString("location"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //Initialize RecyclerView
        initRecyclerView(view);
    }

    /** Method populates the RecyclerView using the ArrayLists in displayImages method
     */
    private void initRecyclerView(View view){

        //Creates layout manager and sets the orientation to horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        //Reference to the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //Sets layout manager of the RecyclerView
        recyclerView.setLayoutManager(layoutManager);

        //Creates new RecyclerViewAdapter to handle images
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),imagePaths);

        //Creates a SnapHelper object to prevent RecyclerView from free spinning
        SnapHelper snapHelper = new LinearSnapHelper();

        //Attaches SnapHelper to RecyclerView
        snapHelper.attachToRecyclerView(recyclerView);

        //Sets adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        //Creates a click listener for the RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            /** Method handles click events from the RecyclerView
             */
            public void onClick(View view, int position) {

                //Menu option selected is filter or create_outfit
                if(menuOptionSelected.equals("filter") || menuOptionSelected.equals("create_outfit")) {
                    //Gets reference to current image data
                    SharedPrefManager.getInstance(getActivity()).setCurrentImagePath(imagePaths.get(position));
                    SharedPrefManager.getInstance(getActivity()).setCurrentImageId(imageId.get(position));
                    SharedPrefManager.getInstance(getActivity()).setCurrentLocation(location.get(position));
                    //Fragment manipulation
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

    /** Method retrieves images based on filters
     */
    private void getImagesFromServerFilter(View current_view)
    {
        //Filter type
        final String filter_type = SharedPrefManager.getInstance(getActivity()).getFilterType();

        //Filter value
        final String filter_value = SharedPrefManager.getInstance(getActivity()).getFilterValue();

        //Current view
        final View view = current_view;

        //New StringRequest for query
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_BROWSEWARDROBE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error"))
                        displayImages(view, obj);
                    else
                        Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
            }})

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("filter_type",filter_type);
                params.put("filter_value",filter_value);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        //Add to request queue
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /** Method populates RecyclerView based on list selected
     */
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_type",list_type);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        //Add to request queue
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /** Method populates RecyclerView based on Outfit selected
     */
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation","read_outfit_items");
                params.put("outfit_name",SharedPrefManager.getInstance(getActivity()).getCurrentOutfit());
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        //Add to request queue
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}

