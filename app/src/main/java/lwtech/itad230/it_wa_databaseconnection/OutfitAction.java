package lwtech.itad230.it_wa_databaseconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class: Options given to the user when viewing an outfit
 * And Methods for Actions performed when user clicks on buttons
 */
public class OutfitAction extends Fragment implements View.OnClickListener {

    TextView outfitName;
    Button viewOutfit,addOutfit,deleteOutfit;
    String outfit_name;

    /**
     * Method: Inflate Layout for page when on creation
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.outfit_action,null);
    }

    /**
     * Method: Sets up all buttons with the views
     * And adds On Click Listener to buttons
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        outfit_name = SharedPrefManager.getInstance(getActivity()).getCurrentOutfit();
        outfitName = view.findViewById(R.id.outfit_name);
        outfitName.setText(outfit_name);

        viewOutfit = view.findViewById(R.id.view_outfit);
        viewOutfit.setOnClickListener(this);

        addOutfit = view.findViewById(R.id.add_outfit);
        addOutfit.setOnClickListener(this);

        deleteOutfit = view.findViewById(R.id.delete_outfit);
        deleteOutfit.setOnClickListener(this);
    }

    /**
     * OnClick Method: Calls methods to perform users action request
     * depending on which button is clicked
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view == viewOutfit)
        {
            SharedPrefManager.getInstance(getActivity()).setMenuOptionSelected("view_outfit");
            viewOutfit();
        }

        if(view == addOutfit)
        {
            SharedPrefManager.getInstance(getActivity()).setMenuOptionSelected("create_outfit");
            SharedPrefManager.getInstance(getActivity()).setCurrentOutfitName(SharedPrefManager.getInstance(getActivity()).getCurrentOutfit());
            addToOutfit();
        }

        if(view == deleteOutfit)
        {
            deleteOutfit();
        }

    }

    /**
     * Method: Allows user to view outfit
     * Replaces current fragment with BrowseWardrobe class as a fragment
     */
    private void viewOutfit()
    {
        Fragment fragment = new BrowseWardrobe();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.addToBackStack("my_fragment");
        fragmentTransaction.commit();
    }

    /**
     * Method: Allows user to go to wardrobe and add new items to outfit
     * Replaces current fragment with BrowseWardrobe class as a fragment
     */
    private void addToOutfit()
    {
        SharedPrefManager.getInstance(getActivity()).setFilterType("None");
        SharedPrefManager.getInstance(getActivity()).setFilterValue("");
        Fragment fragment = new BrowseWardrobe();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.addToBackStack("my_fragment");
        fragmentTransaction.commit();
    }

    /**
     * Method: Allows user to delete outfit
     * Connects with online database and deletes outfit
     */
    private void deleteOutfit()
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
                params.put("delete_type","delete_outfit");
                params.put("outfit_name",outfit_name);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

