package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import scot.wildcamping.wildscotland.model.User;

public class BioFragment extends Fragment {

    public BioFragment() {
    }

    TextView txtName;
    TextView txtEmail;
    TextView txtBio;
    Button btnLogout;
    TextView update;
    Boolean this_user = false;

    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bio, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            this_user = extras.getBoolean("this_user");
        }

        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
        txtBio = (TextView) rootView.findViewById(R.id.bio);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        update = (TextView) rootView.findViewById(R.id.updateProfile);

        session = new SessionManager(getContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        //User user = new User();

        //String name = user.getName();
        //String email = user.getEmail();
        //String bio = user.getBio();

        //txtName.setText(name);
        //txtEmail.setText(email);
        //txtBio.setText(bio);

        if(this_user){
            txtName.setText(AppController.getString(getContext(), "name"));
            txtEmail.setText(AppController.getString(getContext(), "email"));
            txtBio.setText(AppController.getString(getContext(), "bio"));
        } else {
            txtName.setText(AppController.getString(getContext(), "user_name"));
            txtEmail.setText(AppController.getString(getContext(), "user_email"));
            txtBio.setText(AppController.getString(getContext(), "user_bio"));

            update.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),
                        BioActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                goToAttract(v);
        }
    }*/
/*
    public void goToAttract(View v){
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }*/

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                getActivity().finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}


