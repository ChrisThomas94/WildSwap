package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import scot.wildcamping.wildscotland.adapter.QuestionListAdapter;
import scot.wildcamping.wildscotland.adapter.SiteListAdapter;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class QuestionFragment extends Fragment {

    public QuestionFragment() {
    }

    TextView txtName;
    TextView txtEmail;
    Button btnLogout;
    private QuestionListAdapter adapter;
    private ListView mDrawerList;
    knownSite inst;
    private SessionManager session;
    SparseArray<Site> knownSites;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.question_listview);

        inst = new knownSite();
        knownSites = new SparseArray<>();
        knownSites = inst.getKnownSitesMap();

        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);

        session = new SessionManager(getContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        String name = AppController.getString(getContext(), "name");
        String email = AppController.getString(getContext(), "email"); //email is currently blank so print uid instead
        txtName.setText(name);
        txtEmail.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        adapter = new SiteListAdapter(getActivity(), knownSites);
        mDrawerList.setAdapter(adapter);

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


