package lwtech.itad230.it_wa_databaseconnection;

/**
 * Created by ashlyluse on 5/22/18.
 * Class that displays cardviews of each outfit
 * Allows the user to tap single cardviews to view the outfit's items
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
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
import java.util.List;
import java.util.Map;

public class BrowseOutfits extends android.support.v4.app.Fragment {

    private List<OutfitCards> outfitCards;
    ArrayList<String> outfitNames = new ArrayList<>();
    //the recyclerview
    RecyclerView outfitsView;

    /* Method: inflates XML layout display */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.outfits,null);
    }

    /* Method: calls generateOutfits method to populate Outfit Cards with data */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateOutfits(view);
    }

    /**
     * displayOutfits: Creates Outfit Cards
     * @param view
     *        obj - Json object has outfit information
    */
    public void displayOutfits(View view,  JSONObject obj) {
        //initializing the array of Outfit Cards
        ArrayList<OutfitCards> OUTFITS = new ArrayList<>();

        //getting the recyclerview from xml
        outfitsView = view.findViewById(R.id.recyclerViewOutfits);
        outfitsView.setHasFixedSize(true);
        outfitsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //for loop should run 1 less than total rows - first row is error status
        for(int i=1;i<obj.length();i++)
        {
            JSONObject temp = null;
            try {
                temp = new JSONObject(obj.getString("row"+i));
                //Add image path url to arraylist
                outfitNames.add(temp.getString("outfit_name"));
                //Create outfit cards
                OUTFITS.add(new OutfitCards(i+1, temp.getString("outfit_name"), R.drawable.layer4));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        outfitCards = OUTFITS;

        //creating recyclerview adapter
        OutfitsAdapter adapter = new OutfitsAdapter(getActivity(), outfitCards);

        //setting adapter to recyclerview
        outfitsView.setAdapter(adapter);

        /* Method that adds listener for when the user taps the card */
        outfitsView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), outfitsView, new RecyclerTouchListener.ClickListener() {
            /* Method: When the user clicks on an outfit card,
            it opens a new window that shows options for the outfit (view, delete, add) */
            @Override
            public void onClick(View view, int position) {
                SharedPrefManager.getInstance(getActivity()).setCurrentOutfit(outfitNames.get(position));
                Fragment fragment = new OutfitAction();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment);
                fragmentTransaction.addToBackStack("my_fragment");
                fragmentTransaction.commit();
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));
    }

    /**
     *  Method: populates Outfit Cards with data from the server
     *  by calling to PHP file URL_CREATEOUTFIT and getting a JSONObject.
     *  If successful, it passes the object to displayOutfit Method
     *  @param currentView
     */
    public void generateOutfits(View currentView) {

        final View view = currentView;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CREATEOUTFIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                displayOutfits(view, obj);
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
                    /* Method: Giving error message if error happens */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            /* Method: adds paramaters into Map for access later*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation","read_outfit_names");
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));

                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}
