package lwtech.itad230.it_wa_databaseconnection;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class that contains all methods that are needed
 */
public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_FILTERTYPE = "filter_type";
    private static final String KEY_FILTERVALUE= "filter_value";
    private static final String KEY_DISPLAYSTATUS = "set_display";
    private static final String KEY_IMAGEPATH = "image_path";
    private static final String KEY_IMAGEID= "image_id";
    private static final String KEY_MENUOPTION = "menu_option";
    private static final String KEY_OUTFITNAME = "outfit_name";
    private static final String KEY_OUTFIT = "outfit";
    private static final String KEY_LOCATION = "location";

    /**
     * SharedPrefManager Method; sets context for class
     * @param context
     */
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    /**
     * userlogin - save the user info
     * @param user_id
     * @param user_name
     * @return true
     */
    public boolean userlogin(int user_id, String user_name)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USERID, user_id);
        editor.putString(KEY_USERNAME, user_name);
        editor.apply();
        return true;
    }

    /**
     * isLoggedIn - checks if the user logged in
     * @return true - if logged in
     *          false - if not logged in
     */
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null)!= null)
        {
            return true;
        }
        return false;
    }

    /**
     * logOut - clear the data
     * @return true
     */
    public boolean logOut()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    /**
     * getUserId - get user id
     * @return int - id
     */
    public int getUserId()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USERID, 0);
    }

    /**
     * setFilterType - set the filter type
     * @param type
     */
    public void setFilterType(String type)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FILTERTYPE, type);
        editor.apply();
    }

    /**
     * setFilterValue - set the filter value
     * @param value
     */
    public void setFilterValue(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FILTERVALUE, value);
        editor.apply();
    }

    /**
     * getFilterType - get the filter type
     * @return - String
     */
    public String getFilterType()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FILTERTYPE,null);
    }

    /**
     * getFilterValue - get the filter value
     * @return - String
     */
    public String getFilterValue()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FILTERVALUE, null);
    }

    /**
     * setCurrentImagePath - set the current viewing image path
     * @param value
     */
    public void setCurrentImagePath(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGEPATH, value);
        editor.apply();
    }

    /**
     * setCurrentImageId - set the current viewing image id
     * @param value
     */
    public void setCurrentImageId(int value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGEID, String.valueOf(value));
        editor.apply();
    }

    /**
     * getCurrentImagePath - get the current viewing image path
     * @return String - path
     */
    public String getCurrentImagePath()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IMAGEPATH, null);
    }

    /**
     * getCurrentImageId - get the current viewing image id
     * @return String - id
     */
    public String getCurrentImageId()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IMAGEID, null);
    }

    /**
     * setMenuOptionSelected - set the menu option selected
     * @param value
     */
    public void setMenuOptionSelected(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MENUOPTION, String.valueOf(value));
        editor.apply();
    }

    /**
     * getMenuOptionSelected - get the menu option selected
     * @return String - menu option
     */
    public String getMenuOptionSelected()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MENUOPTION, null);
    }

    /**
     * setCurrentOutfitName - set the current creating outfit
     * @param value
     */
    public void setCurrentOutfitName(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OUTFITNAME, String.valueOf(value));
        editor.apply();
    }

    /**
     * getCurrentOutfitName - get the current creating outfit
     * @return String - outfit name
     */
    public String getCurrentOutfitName()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OUTFITNAME, null);
    }

    /**
     * setCurrentOutfit - set the current viewing outfit name
     * @param value
     */
    public void setCurrentOutfit(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OUTFIT, String.valueOf(value));
        editor.apply();
    }

    /**
     * getCurrentOutfit - get the current viewing outfit name
     * @return - String - outfit name
     */
    public String getCurrentOutfit()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OUTFIT, null);
    }

    /**
     * setCurrentLocation - set the location of current viewing image
     * @param value - location
     */
    public void setCurrentLocation(String value)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOCATION, String.valueOf(value));
        editor.apply();
    }

    /**
     * getCurrentLocation - get the location of current viewing image
     * @return String - location
     */
    public String getCurrentLocation()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOCATION, null);
    }

}