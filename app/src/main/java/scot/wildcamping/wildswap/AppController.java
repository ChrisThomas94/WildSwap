package scot.wildcamping.wildswap;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 * Created by Chris on 23-Dec-15.
 *
 */

public class AppController extends Application {

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
