package lwtech.itad230.it_wa_databaseconnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonAdd, buttonBrowseWardrobe, buttonAssembleOutfit, buttonBrowseOutfit,buttonBrowseWishList;
    private Button buttonManageDonation, buttonManageTravel, buttonBrowseFriends;

    private ProgressDialog progressDialog;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonAdd = (Button)findViewById(R.id.add);
        buttonBrowseWardrobe = (Button)findViewById(R.id.browseWardrobe);
        buttonAssembleOutfit = (Button)findViewById(R.id.assembleOutfit);
        buttonBrowseOutfit = (Button)findViewById(R.id.browseOutfit);
        buttonBrowseWishList = (Button)findViewById(R.id.browseWishList);
        buttonManageDonation = (Button)findViewById(R.id.ManageDonation);
        buttonManageTravel = (Button)findViewById(R.id.manageTravel);
        buttonBrowseFriends = (Button)findViewById(R.id.browseFriends);

        buttonAdd.setOnClickListener(this);
        buttonBrowseWardrobe.setOnClickListener(this);
        buttonAssembleOutfit.setOnClickListener(this);
        buttonBrowseOutfit.setOnClickListener(this);
        buttonBrowseWishList.setOnClickListener(this);
        buttonManageDonation.setOnClickListener(this);
        buttonManageTravel.setOnClickListener(this);
        buttonBrowseFriends.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonAdd)
        {
            startActivity(new Intent(getApplicationContext(), AddItem.class));
        }
        if(view == buttonBrowseWardrobe)
        {
            browseWardrobe();
        }
        if(view == buttonAssembleOutfit)
        {
        }
        if(view == buttonBrowseOutfit)
        {
        }
        if(view == buttonBrowseWishList)
        {
        }
        if(view == buttonManageDonation)
        {
        }
        if(view == buttonManageTravel)
        {
        }
        if(view == buttonBrowseFriends)
        {
        }
    }

    private void browseWardrobe()
    {
        //Read data from UI
        final String filter_type = "color";
        final String filter_value = "red";

        progressDialog.setMessage("Displaying.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_BROWSEWARDROBE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                //Call display function by Rocky and pass JSONObject as parameter(result set from database)
                                /*SharedPrefManager.getInstance(getApplicationContext()).userlogin(
                                        obj.getInt("user_id"),
                                        obj.getString("user_name")

                                );*/
                                //Rocky, You can make use of this code.. This code is about reading two dimensional array(JSONObject)
                                Log.v("Data: total rows: ", String.valueOf(obj.length()));
                                for(int i=1;i<=obj.length();i++)
                                {
                                    JSONObject temp = new JSONObject(obj.getString("row"+i));
                                    Log.v("Data: Path:", temp.getString("image_path"));
                                }
                                //Testing-Remove this later
                                //Toast.makeText(getApplicationContext(),"read success",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
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

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
