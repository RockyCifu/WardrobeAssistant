package lwtech.itad230.it_wa_databaseconnection;

import android.app.ProgressDialog;
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

public class AddTags extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextImagePath, editTextColor,editTextSeason,editTextApparelType,editTextDrawer;
    private Button buttonAddTags;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

        editTextImagePath = (EditText)findViewById(R.id.editTextImagePath);
        editTextColor = (EditText)findViewById(R.id.editTextColor);
        editTextSeason = (EditText)findViewById(R.id.editTextSeason);
        editTextApparelType = (EditText)findViewById(R.id.editTextApparelType);
        editTextDrawer = (EditText)findViewById(R.id.editTextDrawer);

        buttonAddTags = (Button) findViewById(R.id.buttonAddTags);
        buttonAddTags.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
    }

    private void addTags()
    {
        final String image_path = editTextImagePath.getText().toString().trim();
        final String color = editTextColor.getText().toString().trim();
        final String season = editTextSeason.getText().toString().trim();
        final String apparel_type = editTextApparelType.getText().toString().trim();
        //final int drawer = Integer.parseInt(editTextDrawer.getText().toString().trim());
        final String drawer = editTextDrawer.getText().toString().trim();

        progressDialog.setMessage("Adding tags...");
        progressDialog.show();

        //Toast.makeText(getApplicationContext(),"AAAAAAAAAA",Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADDTAG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject1.getString("message"),Toast.LENGTH_LONG).show();
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
                params.put("user_id","1");
                params.put("image_path",image_path);
                params.put("color",color);
                params.put("apparel_type",apparel_type);
                params.put("season",season);
                params.put("drawer",drawer);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonAddTags)
        {
            addTags();
        }
    }
}
