package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
	
	public SettingsFragment(){}

    TextView txtName;
    TextView txtEmail;
    Button btnLogout;

    private SessionManager session;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);


        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);

        session = new SessionManager(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        String name = AppController.getString(getActivity().getApplicationContext(), "name");
        String email = AppController.getString(getActivity().getApplicationContext(), "uid"); //email is currently blank so print uid instead
        txtName.setText(name);
        txtEmail.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        //btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        //btnLogout.setOnClickListener(this);

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
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }
}

