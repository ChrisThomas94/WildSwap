package scot.wildcamping.wildscotland.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import scot.wildcamping.wildscotland.AppController;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    private static final String TAG = "MyFirebaseIIDService";
    String errorMsg;
    Boolean error;


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String rToken = getToken();
        System.out.println("get token " + rToken);
        System.out.println("refreshed token " + refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        AppController.setString(getApplicationContext(), "token", refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        //packageToken p = new packageToken();

    }

    private String getToken(){
        String currentToken = FirebaseInstanceId.getInstance().getToken();

        return currentToken;
    }

}

