package lwtech.itad230.it_wa_databaseconnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog progressDialog;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;

    private RadioGroup mFilterType, mFilterValue;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private EditText editOutfitName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    /**
     * @Name OnCreate
     * @desc the oncreate method instantiates all that is required for method operation,
     * the displayRecommendations method is called to begin basic operation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);

        //Default display recommendations
         displayRecommendations();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @Name onNavigationItemSelected
     * @param item the destination the user selected
     * @return true
     * # desc this method is executed upon selection of a menu option,
     * the necessary methods are executed that will send the user to the their desired location in the app
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add)
        {
            fragment = new AddItem();
        }
        else if (id == R.id.browseWardrobe)
        {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("filter");
            popUpWindowForFilterType();
        }
        else if (id == R.id.createOutfit) {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("create_outfit");
            popUpWindowForOutfitName();

        } else if (id == R.id.viewOutfit) {
            fragment = new BrowseOutfits();

        } else if (id == R.id.ManageDonation) {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("donation_list");
            fragment = new BrowseWardrobe();


        } else if (id == R.id.manageTravel) {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("travel_list");
            fragment = new BrowseWardrobe();

        } else if (id == R.id.moreOptions) {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("more_options");
            popUpWindowForMoreOptions();

        }
        else if (id == R.id.version2_0) {
            SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("version2.0");
            fragment = new Version2_0();

        }else if (id == R.id.buttonLogOut) {

            logout();
        }
        if(fragment != null)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer =  findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @Name popUpWindowForFilterType
     * @desc This method generates a pop up window for selecting filter types,
     * the filter types are displayed via radio button,
     * upon selection of the ok button, the filter type will be set and the method for value type will be called and that pop up will execute
     * the cancel button closes the pop up
     */
    private void popUpWindowForFilterType()
    {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_filter_type, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = popupView.findViewById(R.id.btnOk);
        Button btnCancel =  popupView.findViewById(R.id.btnCancel);

        mFilterType = popupView.findViewById(R.id.radioScreenMode);
        mFilterType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.filterNone: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("None");
                    }break;
                    case R.id.filterColor: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("color");
                    }break;
                    case R.id.filterApparelType: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("apparel_type");
                    }    break;

                    case R.id.filterSeason: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("season");
                    }    break;

                    default:
                        break;
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(!SharedPrefManager.getInstance(getApplicationContext()).getFilterType().equals("None")) {
                    Toast.makeText(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getFilterType(), Toast.LENGTH_SHORT).show();
                    popUpWindowForFilterValue();
                }
                else {
                    fragment = new BrowseWardrobe();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.screen_area, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * @Name popUpWindowsForFilterValue
     * @desc This method generates a pop up window allowing the user to set a filter in the display options,
     * The pop generates with the radio selection of filter values and an ok and cancel button,
     * upon selection of the type, the respective values to further thin the scope of the filter,
     * on final confirmation the method replaces the current fragment with the new filter values and close the popup window,
     * the Cancel button closes the pop up
     */
    private void popUpWindowForFilterValue()
    {
        String filter_type = SharedPrefManager.getInstance(getApplicationContext()).getFilterType();
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.filter_color, null);
            switch (filter_type) {
                case "color":
                    popupView = layoutInflater.inflate(R.layout.filter_color, null);
                   break;
                case "apparel_type":
                    popupView = layoutInflater.inflate(R.layout.filter_apparel_type, null);
                    break;
                case "season":
                    popupView = layoutInflater.inflate(R.layout.filter_season, null);
                    break;
                default:
                    break;
            }
            popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
            popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setIgnoreCheekPress();

            Button btnOk = popupView.findViewById(R.id.btnOk);
            Button btnCancel = popupView.findViewById(R.id.btnCancel);
            switch (filter_type) {
                case "color":
                    mFilterValue = popupView.findViewById(R.id.filterColorSelected);
                    break;
                case "apparel_type":
                    mFilterValue = popupView.findViewById(R.id.filterApparelTypeSelected);
                    break;
                case "season":
                    mFilterValue = popupView.findViewById(R.id.filterSeasonSelected);
                    break;
            }
            mFilterValue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (checkedId) {
                        case R.id.colorBlack: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Black");
                        }    break;

                        case R.id.colorWhite: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("White");
                        }    break;

                        case R.id.colorPurple: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Purple");
                        }    break;

                        case R.id.colorViolet: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Violet");
                        }    break;

                        case R.id.colorRed: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Red");
                        }    break;

                        case R.id.colorPink: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Pink");
                        }    break;

                        case R.id.colorOrange: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Orange");
                        }    break;

                        case R.id.colorYellow: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Yellow");
                        }    break;

                        case R.id.colorGreen: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Green");
                        }    break;

                        case R.id.colorBlue: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Blue");
                        }    break;

                        case R.id.typeHat: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Hat");
                        }    break;

                        case R.id.typeDress: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Dress");
                        }    break;

                        case R.id.typeSkirt: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Skirt");
                        }    break;

                        case R.id.typePants: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Pants");
                        }    break;

                        case R.id.typeShirt: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Shirt");
                        }    break;

                        case R.id.typeCoat: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Coat");
                        }    break;

                        case R.id.typeShoes: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Shoes");
                        }    break;

                        case R.id.typeAccessory: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Accessory");
                        }    break;

                        case R.id.seasonSpringSummer: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Spring/Summer");
                        }    break;

                        case R.id.seasonFallWinter: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Fall/Winter");
                        }    break;

                        default:
                            break;
                    }
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    //After selecting filter value display items
                    fragment = new BrowseWardrobe();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.screen_area, fragment);
                    fragmentTransaction.commit();

                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
    }

    /**
     * @Name displayRecommendations
     * @desc This method produces the default display first shown upon log in,
     * creates the fragment and displays tne selected filter, loops through when selection is reached
     */
    public void displayRecommendations()
    {

        //read weather
        SharedPrefManager.getInstance(getApplicationContext()).setMenuOptionSelected("filter");
        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("season");
        SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Fall/Winter");

        fragment = new BrowseWardrobe();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @Name popUpWindowForOutfitName
     * @desc This method generates a popup window that takes user input for outfitName,
     * The pop up allows the user to edit outfit name, confirm or cancel the page,
     * on Ok selection, the string is tested, if empty, displays it cannot be empty, upon parameter being met,
     * window closes and calls validateOutfitName method for check and execution,
     * On cancel, window closes
     */
    public void popUpWindowForOutfitName()
    {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_outfit_name, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = popupView.findViewById(R.id.btnOk);
        Button btnCancel = popupView.findViewById(R.id.btnCancel);

         editOutfitName = popupView.findViewById(R.id.editOutfitName);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String outfitName = editOutfitName.getText().toString().trim();

                if(outfitName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Outfit name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    popupWindow.dismiss();
                    validateOutfitName(outfitName);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * @Name validateOutfitName
     * @param outfitName string produced by user input on the name for the item selection
     * @desc This method generates a string JsonObject request to test if a user genrerated name is valid,
     * Posting to the createOutfit php via HashMap containing validity, user created outfit name, and user identification,
     * if an error is not produced, the outfit_name will be set as current outfit name,
     * replacing with new fragment.
     */
    private void validateOutfitName(String outfitName)
    {
        final String  outfit_name = outfitName;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CREATEOUTFIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                SharedPrefManager.getInstance(getApplicationContext()).setCurrentOutfitName(outfit_name);
                                SharedPrefManager.getInstance(getApplicationContext()).setFilterType("None");
                                SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("");
                                fragment = new BrowseWardrobe();
                                fragmentManager = getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.screen_area, fragment);
                                fragmentTransaction.commit();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("operation","validity");
                params.put("outfit_name", outfit_name);
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * @Name popUpWindowForMoreOptions
     * @desc This method generates a pop up window centered on the screen upon call with options to clear the current travel list or
     * donation list through radio button selection, the pop up contains two buttons, 'Ok' and 'Cancel' Ok will execute a one of the clearing methods
     * and close the pop up window. Cancel will close the pop up window.
     */
    public void popUpWindowForMoreOptions()
    {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_more_options, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = popupView.findViewById(R.id.btnOk);
        Button btnCancel = popupView.findViewById(R.id.btnCancel);

        mFilterType = popupView.findViewById(R.id.radioScreenMode);
        mFilterType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.clearTravelList: {
                        clearTravelList();
                    }break;
                    case R.id.clearDonationList: {
                        clearDonationList();
                    }    break;
                    default:
                        break;
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * @Name clearTravelList
     * @desc this method generates a string JsonObject request,
     * posting information via HashMap to the php, clearing he current travel list based on user identification.
     */
    private void clearTravelList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("delete_type","clear_travel_list");
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    /**
     * @Name clearDonationList
     * @desc This method generates a string JsonObject request,
     *posting information via Hashmap to the php clearing the current donation list by user identification.
     */
    private void clearDonationList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("delete_type","clear_donation_list");
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getApplicationContext()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    /**
     * @Name logout
     * @desc generates a string jsonObject request to the php to disconnect the user from the database
     */
    private void logout()
    {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                SharedPrefManager.getInstance(getApplicationContext()).logOut();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
