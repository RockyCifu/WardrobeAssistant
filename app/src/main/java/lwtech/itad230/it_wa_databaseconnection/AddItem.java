package lwtech.itad230.it_wa_databaseconnection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/** AddItem captures the image of a clothing item, gathers tag information, and uploads the information to the server and database.
 */
public class AddItem extends android.support.v4.app.Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1; // Request code for onActivityResult
    private String phpUrl = Constants.URL_ADDITEM; // URL of the php page
    private Button submit,takePic; // Reference variables to the buttons in fragment.
    private Spinner type,season,color,location; // Reference variables to the spinners in the fragment.
    private static String imageFilePath; // Path to the image file.
    private ImageView imageView; // Reference variable to image view.

    @Nullable
    @Override
    /**Method constructs the layout immediately upon AddItem creation.
    */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_fragment,null);
    }

    @Override
    /**Method is called after onCreateView is finished.
     * Reference variables for views are set.
     * OnClick listeners are set.
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Always call super of overridden method.
        super.onViewCreated(view, savedInstanceState);

        // Initialize reference variable for the submit button
        submit = view.findViewById(R.id.btn_submit);
        // Set onClick listener for the button
        submit.setOnClickListener(this);

        // Initialize reference variable for the take picture button
        takePic = view.findViewById(R.id.btn_take_picture);
        // Set onClick listener for the button
        takePic.setOnClickListener(this);

        // Initialize reference variable for the image view displaying the captured image.
        imageView = view.findViewById(R.id.image_preview);

        // Initialize reference variable for the apparel type spinner
        type = view.findViewById(R.id.apparel_spinner);

        //Sets up ArrayAdapter to populate the spinner.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        // Initialize reference variable for the season spinner
        season = view.findViewById(R.id.season_spinner);

        //Sets up ArrayAdapter to populate the spinner.
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.season_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        season.setAdapter(adapter);

        // Initialize reference variable for the color spinner
        color = view.findViewById(R.id.item_color_spinner);

        //Sets up ArrayAdapter to populate the spinner.
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(adapter);

        // Initialize reference variable for the location spinner
        location = view.findViewById(R.id.location_spinner);

        //Sets up ArrayAdapter to populate the spinner.
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);
    }

    @Override
    /**Method is called for handling click events
     */
    public void onClick(View view) {
        //submit is clicked
        if(view == submit){

            //If the values are set, then upload item.
            if(valuesSet()){
                uploadItem();
                Toast.makeText(getActivity(), "Upload Successful",
                        Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_LONG).show();

        }

        //Take picture is clicked
        if(view == takePic)
            //Opens camera to capture image.
            openCameraIntent();

    }

    /** Method returns true if all user entries have been set in order to prevent database mismatch.
     */
    private boolean valuesSet() {
        return  imageView.getDrawable() != null && location.getSelectedItem().toString().length() > 0 &&
                color.getSelectedItem().toString().length() > 0 && type.getSelectedItem().toString().length() > 0 &&
                type.getSelectedItem().toString().length() > 0;
    }

    /**Method handles the capture of the image
     */
    private void openCameraIntent() {
        //Create new image capture intent
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //If the phone has something that can handle the intent
        if(pictureIntent.resolveActivity(getContext().getPackageManager()) != null){

            //Instantiate a file to null
            File photoFile = null;

            try {
                //returned image file is held in photoFile variable
                photoFile = createImageFile();

            } catch (IOException ex) {}

            if (photoFile != null) {
                //get Uri for the file
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"lwtech.itad230.it_wa_databaseconnection.provider", photoFile);

                //Put image into file location specified
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                //Calls method to handle the result
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**Method creates a temporary image file in the Picture directory of the device.
     */
    private File createImageFile() throws IOException {

        //Reference to picture directory
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Creates a file in the picture directory with the name tempPicture.jpg
        File image = File.createTempFile("tempPicture",".jpg",storageDir);

        //Stores absolute path to the created file
        imageFilePath = image.getAbsolutePath();

        //Return the created file.
        return image;
    }



    @Override
    /**Method handles the returning intent
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        //Checks request and result codes
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            //Use Glide to populate the image capture into the view
            Glide.with(this).load(imageFilePath).into(imageView);
        }
    }

    /**Method creates a bitmap from a file
     */
    private Bitmap getBitmapFromFile(){

        //Decodes the image into a bitmap variable
        Bitmap img = BitmapFactory.decodeFile(imageFilePath);

        //Fixes orientation problem caused by various devices
        img = fixOrientation(img);

        //Creates reference to temp file for deletion
        File file = new File(imageFilePath);

        //Delete file
        file.delete();

        //return resized bitmap after all image processing is done.
        return resizedBitmap(img, 480, 640);
    }

    /**Method fixes orientation problem caused by various devices
     */
    private static Bitmap fixOrientation(Bitmap image){

        ExifInterface ei = null;

        try {
            // Instantiates an ExifInterface with the current image
            ei = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gets orientation data from the ExifInterface
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        // Rotates image if necessary
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(image, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(image, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(image, 270);
            default:
                return image;
        }
    }

    /** Method rotates image bitmap to the proper orientation
     */
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    /**Method resizes Bitmap to dimensions passed as parameters
     */
    private static Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth(); //Current bitmap width
        int height = bm.getHeight(); //Current bitmap height
        float scaleWidth = ((float) newWidth) / width; //New width
        float scaleHeight = ((float) newHeight) / height; //New height

        // Create matrix to manipulate the data
        Matrix matrix = new Matrix();

        // Scales the matrix to new dimensions
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate a new bitmap from the modified matrix
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    /**Method converts a bitmap to a string
     */
    private String imageToString(Bitmap bitmap) {

        //New output stream object
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Sets compression quality at 100 percent in the output stream
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        //Creates a byte array from output stream
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        //Returns string encoded in base 64 from byte array
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    /**Method uses volley to upload data to server and database
     */
    private void uploadItem() {

        //Creates new string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, phpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Get data upon return
                    JSONObject jsonObject = new JSONObject(response);

                    String Response = jsonObject.getString("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            //Method sets all parameters into a Map for Post request
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user_id",Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserId()));
                params.put("color",color.getSelectedItem().toString());
                params.put("apparel_type",type.getSelectedItem().toString());
                params.put("season",season.getSelectedItem().toString());
                params.put("location",location.getSelectedItem().toString());
                params.put("image",imageToString(getBitmapFromFile()));
                return params;
            }
        };

        //Adds the request to the handler's request queue
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}


