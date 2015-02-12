package com.example.victor.payback;

/**
 * Created by Victor on 2/11/2015.
 */
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Application extends android.app.Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "AnyWall";

    // Used to pass location from MainActivity to PostActivity
    public static final String INTENT_EXTRA_LOCATION = "location";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;


    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "a2DlMumhUo2cXhX1UnY9nG7PxDUCfZf3glxTa7Ps", "bF6kYXauBnQMI1YhwRFcF0qInpibEBCnxj0YMoWU");


    }



}