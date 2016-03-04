package scot.wildcamping.wildscotland;

/**
 * Created by Chris on 23-Dec-15.
 */
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    static Map<Integer, knownSite> map = new HashMap<Integer, knownSite>();
    static int siteSize = 0;
    List<LatLng> unknownSites = new ArrayList<>();
    List<LatLng> knownSites = new ArrayList<>();

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static void addSite(Context context, Integer siteNo, knownSite site){
        map.put(siteNo, site);
        siteSize++;
    }

    public static knownSite getSite(Context context, Integer siteNo){
        return map.get(siteNo);
    }

    public void setUnknownSites(Context context, List<LatLng> unknownSites){
        this.unknownSites = unknownSites;
    }

    public List<LatLng> getUnknownSites(Context context){
        return this.unknownSites;
    }

    public void setKnownSites(Context context, List<LatLng> knownSites){
        this.knownSites = knownSites;
    }

    public List<LatLng> getKnownSites(Context context){
        return this.knownSites;
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

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
