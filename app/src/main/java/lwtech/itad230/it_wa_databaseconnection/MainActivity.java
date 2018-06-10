package lwtech.itad230.it_wa_databaseconnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Main Activity class that runs upon start of the app
 * It holds all base functions for the app
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUsername, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private ProgressDialog progressDialog;

    /**
     * When Activity is created it proceeds to set the layout,
     * and set up image and text views.
     * It also sets on click listeners onto the buttons for
     * logging in and registering
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If user is already logged in go to menu activity
        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
            return;
        }
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(this);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        buttonLogin  = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    /**
     * registerUser: Method that takes information given by the user
     * calls to the online database and registers the user
     * as a member of the app
     */
    private void registerUser()
    {

        final String username = editTextUsername.getText().toString()/*.trim()*/;
        final String password = editTextPassword.getText().toString()/*.trim()*/;

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    /* Method: Provide success message if successful */
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                /* Provide error message if unsuccessful */
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            /* Method: adds parameters into Map for access later*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name",username);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * userLogin: Method that checks if username and password are correct,
     * and if so starts the MainMenuActivity giving the user
     * access to the app
     */
    private void userLogin()
    {
        final String username = editTextUsername.getText().toString()/*.trim()*/;
        final String password = editTextPassword.getText().toString()/*.trim()*/;

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    /* Method: Provide success message if successful */
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error"))
                            {
                                SharedPrefManager.getInstance(getApplicationContext()).userlogin(
                                        obj.getInt("user_id"),
                                        obj.getString("user_name")
                                );

                                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                                //Finish the activity because on clicking screen should not show the login screen
                                finish();
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
                    /* Provide error message if unsuccessful */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        )
        {
            /* Method: adds parameters into Map for access later*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_name", username);
                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * onClick: Method that runs when a button is clicked.
     * It calls a method to perform the request action,
     * depending on which button is clicked (determined by the id given
     */
    @Override
    public void onClick(View view)
    {
        if(view == buttonRegister) {
            registerUser();
        }
        if(view == buttonLogin) {
            userLogin();
        }
    }
}
