package scot.wildcamping.wildswap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.CreateUser;

public class Register extends AppCompatActivity implements View.OnClickListener, LocationListener {

    Button tvLogin;
    TextInputLayout fullName;
    TextInputLayout emailRegister;
    TextInputLayout passwordRegister;
    EditText etFullName;
    EditText etEmailRegister;
    EditText etPasswordRegister;
    Button registerButton;
    String name;
    String token;
    String email;
    String password;
    Intent intent;
    SessionManager session;
    LocationManager manager = null;
    GoogleApiClient googleApiClient;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        //initializing Views
        tvLogin = (Button) findViewById(R.id.tv_signin);
        registerButton = (Button) findViewById(R.id.register_button);
        fullName = (TextInputLayout) findViewById(R.id.fullname_registerlayout);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        etFullName = (EditText) findViewById(R.id.fullname_register);
        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);

        tvLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in
        if (session.isLoggedIn()) {

            // User is already logged in. Move to main activity
            Intent intent = new Intent(this,
                    LogoutActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_signin:

                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            case R.id.register_button:
                name = etFullName.getText().toString();
                email = etEmailRegister.getText().toString();
                password = etPasswordRegister.getText().toString();
                token = AppController.getString(getBaseContext(), "token");



                String country = getUserCountry(this);
                System.out.println("Country" + country);
                Locale loc = new Locale ("", country);
                loc.getDisplayCountry();
                String countryName = loc.getDisplayName();
                System.out.println("locale country "+loc.getDisplayCountry());

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(isNetworkAvailable()) {
                        new CreateUser(this, name, email, password, countryName, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                if(!output.equals("null")){
                                    //do nothing
                                    Snackbar.make(v, output, Snackbar.LENGTH_LONG).show();

                                } else {
                                    session.setLogin(true);
                                    intent = new Intent(Register.this, CreateProfileActivity.class);
                                    intent.putExtra("update", false);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).execute();
                    }
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
}
