package com.mychauffeurapp.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mychauffeurapp.activity.Booking;
import com.mychauffeurapp.activity.LoginScreen;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref, pref1;

    // Editor for Shared preferences
    Editor editor, editor1;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TourismPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // All Shared Preferences Keys
    private static final String IS_SERVICE_START = "IsServiceStart";

    //Notification Triggered
    private static final String IS_NOTIFY = "IsNotifyIn";


    // User name (make variable public to access from outside)
    public static final String KEY_REGID = "regid";

    // user name address (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBILE = "mobile";

    // User id  (make variable public to access from outside)
    public static final String KEY_USER_ID = "userid";

    // User id  (make variable public to access from outside)
    public static final String KEY_APP_VER = "appver";

    // POI id  (make variable public to access from outside)
    public static final String KEY_Notify_POI_ID = "poiid";

    // Poi name (make variable public to access from outside)
    public static final String KEY_Notify_POI_NAME = "poiname";

    // Lat  (make variable public to access from outside)
    public static final String KEY_Notify_LAN = "poilaN";

    // Lon (make variable public to access from outside)
    public static final String KEY_Notify_LON = "poilon";


    // user name address (make variable public to access from outside)
    public static final String KEY_NOTIFY_TIME = "00";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        pref1 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor1 = pref1.edit();
    }

    /**
     * Create login session
     */


    //Create ServiceLogin
    public void CreateServiceSession() {

        editor.putBoolean(IS_SERVICE_START, true);

        editor.commit();
    }


    //Create ServiceLogin

    public void CreateNotifyTime(String notify_time) {

        editor.putString(KEY_NOTIFY_TIME, notify_time);

        editor.commit();
    }

    //Create login session
    public void createUserLoginSession(String name, String userid, String email, String mobile) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing name in pref
        editor.putString(KEY_USER_ID, userid);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing mobile in pref
        editor.putString(KEY_MOBILE, mobile);


        // commit changes
        editor.commit();

    }


    //Create Notification Trigger

    //Create login session
    public void createNotificationSession(String POI_id, String POI_Name, String lan, String lon) {
        // Storing login value as TRUE
        editor1.putBoolean(IS_NOTIFY, true);

        // Storing poi id in pref
        editor1.putString(KEY_Notify_POI_ID, POI_id);

        // Storing poi name in pref
        editor1.putString(KEY_Notify_POI_NAME, POI_Name);

        // Storing lat in pref
        editor1.putString(KEY_Notify_LAN, lan);

        // Storing lon in pref
        editor1.putString(KEY_Notify_LON, lon);


        // commit changes
        editor1.commit();

    }


    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent loginiIntent = new Intent(_context, LoginScreen.class);

            // Closing all the Activities from stack
            loginiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(loginiIntent);


        } else {

            // user is not logged in redirect him to Login Activity
            Intent homei = new Intent(_context, Booking.class);

            // Closing all the Activities from stack
            homei.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            homei.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(homei);
        }

    }

    /**
     * Clear session details
     */
    /*public void logoutUser() {


        try{
        //Google Plus Logout
        if (LoginFragment.mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(LoginFragment.mGoogleApiClient);
            LoginFragment.mGoogleApiClient.disconnect();
            Log.d("GooglePus", "Google Plus logout");
        }

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            //Facebook Logout
            LoginManager.getInstance().logOut();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Clearing all user data from Shared Preferences
        //editor.clear();

        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

        // After logout redirect user to Login Activity
    }

 */
    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);

    }

    // Check for Service Start
    public boolean isServiceStart() {
        return pref.getBoolean(IS_SERVICE_START, false);

    }


    //Check Notify Trigger


    public boolean isNotifyTrigger() {
        return pref1.getBoolean(IS_NOTIFY, false);

    }

    public void VisitedNotify() {

        // Clearing all user data from Shared Preferences
        editor1.putBoolean(IS_NOTIFY, false);
        editor1.commit();

    }


    public void createGCMSession(String regid, String appver) {

        // Storing regid in pref
        editor.putString(KEY_REGID, regid);

        // Storing appver in pref
        editor.putString(KEY_APP_VER, appver);

        // commit changes
        editor.commit();
    }


    /**
     * Get stored User id data
     */
       public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user reg-id

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));


        // return user id
        return user;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getGCMDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user reg-id
        user.put(KEY_REGID, pref.getString(KEY_REGID, null));

        // user app version
        user.put(KEY_APP_VER, pref.getString(KEY_APP_VER, null));

        // return user
        return user;
    }

    /**
     * Get stored session data
     */
  /******  public HashMap<String, String> getNotifyDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user reg-id
        user.put(KEY_Notify_POI_ID, pref1.getString(KEY_Notify_POI_ID, null));

        // user app version
        user.put(KEY_Notify_POI_NAME, pref1.getString(KEY_Notify_POI_NAME, null));


        // user app version
        user.put(KEY_Notify_LAN, pref1.getString(KEY_Notify_LAN, null));


        // user app version
        user.put(KEY_Notify_LON, pref1.getString(KEY_Notify_LON, null));


        // return user
        return user;
    }


    /**
     * Get stored session Notification Time
     */
   /**** public HashMap<String, String> getNotifyTimeDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user notigy time
        user.put(KEY_NOTIFY_TIME, pref.getString(KEY_NOTIFY_TIME, null));


        // return user
        return user;
    }
*/

}
