package com.kecepret.myhome.model;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kecepret.myhome.LoginActivity;
import com.kecepret.myhome.MainActivity;

public class UserSession {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "Name";

    // password (make variable public to access from outside)
    public static final String PASSWORD = "txtPassword";

    // Full name (make variable public to access from outside)
    public static final String FULL_NAME = "Full Name";

    // Email address (make variable public to access from outside)
    public static final String EMAIL = "sample@example.com";

    // Phone (make variable public to access from outside)
    public static final String PHONE_NUMBER = "+6281234567890";

    // Address (make variable public to access from outside)
    public static final String ADDRESS = "Jl. Ganesha no 10";

    // Constructor
    public UserSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String uName, String uPassword, String uFullName, String uEmail, String uPhoneNumber, String uAddress){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in preferences
        editor.putString(KEY_NAME, uName);

        // Storing password in preferences
        editor.putString(PASSWORD, uPassword);

        // Storing full name in preferences
        editor.putString(FULL_NAME, uFullName);

        // Storing email in preferences
        editor.putString(EMAIL, uEmail);

        // Storing phone number in preferences
        editor.putString(PHONE_NUMBER, uPhoneNumber);

        // Storing address in preferences
        editor.putString(ADDRESS, uAddress);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    public String getFullName() {
        return (pref.getString(FULL_NAME, null));
    }

    public String getUsername(){
        return (pref.getString(KEY_NAME, null));
    }

    public String getEmail() {
        return (pref.getString(EMAIL, null));
    }

    public String getPhoneNumber() {
        return (pref.getString(PHONE_NUMBER, null));
    }

    public String getAddress() {
        return (pref.getString(ADDRESS, null));
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(_context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}