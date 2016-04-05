package scot.wildcamping.wildscotland;

/**
 * Created by Chris on 23-Dec-15.
 */
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /*
    function to write string to preference file
     */
    public static void setString(Context context, String key, String value) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        //apply commit
        sp.edit().putString(key, value).apply();
    }

    /*
    function to get the data written to preference file
     */
    public static String getString(Context context, String key) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
