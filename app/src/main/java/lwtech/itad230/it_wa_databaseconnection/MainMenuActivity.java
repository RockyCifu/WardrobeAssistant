package lwtech.itad230.it_wa_databaseconnection;

import android.app.ActionBar;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog progressDialog;

    private static final String TAG = "MyActivity";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;

    private RadioGroup mFilterType, mFilterValue;

    private int displayStatus = 0;

    private Fragment fragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.add)
        {
            fragment = new AddItem();
        }
        else if (id == R.id.browseWardrobe)
        {
            popUpWindowForFilterType();
        }
        else if (id == R.id.assembleOutfit) {

        } else if (id == R.id.browseWishList) {

        } else if (id == R.id.ManageDonation) {

        } else if (id == R.id.manageTravel) {

        }
        else if (id == R.id.buttonLogOut) {
            SharedPrefManager.getInstance(getApplicationContext()).logOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(fragment != null)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void popUpWindowForFilterType()
    {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_filter_type, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();

        Button btnOk = (Button) popupView.findViewById(R.id.btnOk);
        Button btnCancel = (Button) popupView.findViewById(R.id.btnCancel);

        mFilterType = (RadioGroup) popupView.findViewById(R.id.radioScreenMode);
        mFilterType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.filterNone: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("None");
                        Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                    }break;
                    case R.id.filterColor: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("color");
                        //SharedPrefManager.getInstance(getApplicationContext()).getFilterType();
                        Toast.makeText(getApplicationContext(), "Filter color", Toast.LENGTH_SHORT).show();
                    }break;
                    case R.id.filterApparelType: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("apparel_type");
                        Toast.makeText(getApplicationContext(), "Filter type", Toast.LENGTH_SHORT).show();
                    }    break;

                    case R.id.filterSeason: {
                        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("season");
                        Toast.makeText(getApplicationContext(), "Filter season", Toast.LENGTH_SHORT).show();
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
    private void popUpWindowForFilterValue()
    {
        String filter_type = SharedPrefManager.getInstance(getApplicationContext()).getFilterType();
        //if(filter_type != "None")
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.filter_color, null);
            switch (filter_type) {
                case "color": {
                    // Toast.makeText(getApplicationContext(), "Filter read color", Toast.LENGTH_SHORT).show();
                    popupView = layoutInflater.inflate(R.layout.filter_color, null);
                }    break;

                case "apparel_type":
                    popupView = layoutInflater.inflate(R.layout.filter_apparel_type, null);
                    break;
                case "season":
                    popupView = layoutInflater.inflate(R.layout.filter_season, null);
                    break;
                default:
                    break;

            }
            //View popupView = layoutInflater.inflate(R.layout.filter_color, null);
            popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
            popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setIgnoreCheekPress();

            Button btnOk = (Button) popupView.findViewById(R.id.btnOk);
            Button btnCancel = (Button) popupView.findViewById(R.id.btnCancel);
            //mFilterValue = (RadioGroup) popupView.findViewById(R.id.filterColorSelected);
            switch (filter_type) {
                case "color":
                    mFilterValue = (RadioGroup) popupView.findViewById(R.id.filterColorSelected);
                    break;
                case "apparel_type":
                    mFilterValue = (RadioGroup) popupView.findViewById(R.id.filterApparelTypeSelected);
                    break;
                case "season":
                    mFilterValue = (RadioGroup) popupView.findViewById(R.id.filterSeasonSelected);
                    break;
            }
            mFilterValue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (checkedId) {
                        case R.id.colorBlack: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Black");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorWhite: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("White");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorPurple: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Purple");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorViolet: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Violet");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorRed: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Red");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorPink: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Pink");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorOrange: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Orange");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorYellow: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Yellow");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorGreen: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Green");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.colorBlue: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Blue");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeHat: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Hat");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeDress: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Dress");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeSkirt: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Skirt");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typePants: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Pants");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeShirt: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Shirt");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeCoat: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Coat");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeShoes: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Shoes");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.typeAccessory: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Accessory");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.seasonSpringSummer: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Spring/Summer");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
                        }    break;

                        case R.id.seasonFallWinter: {
                            SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Fall/Winter");
                            // Toast.makeText(getApplicationContext(), "FilterNone", Toast.LENGTH_SHORT).show();
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
    }

    public void displayRecommendations()
    {

        //read weather
        SharedPrefManager.getInstance(getApplicationContext()).setFilterType("season");
        SharedPrefManager.getInstance(getApplicationContext()).setFilterValue("Fall/Winter");

        fragment = new BrowseWardrobe();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screen_area, fragment);
        fragmentTransaction.commit();
    }
}
