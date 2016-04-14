package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.adapter.QuestionListAdapter;
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Quiz;

/**
 * Created by Chris on 09-Apr-16.
 */
public class BioActivity extends AppCompatActivity {

    private QuestionListAdapter adapter;
    private ListView mDrawerList;
    Quiz inst;
    SparseArray<Question> question;
    boolean update = false;

    String newName;
    String newEmail;
    String newBio;
    EditText name;
    EditText email;
    EditText bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            update = extras.getBoolean("update");
        }

        if(update){
            name = (EditText) findViewById(R.id.name);
            email = (EditText) findViewById(R.id.email);

            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
        }


        bio = (EditText) findViewById(R.id.bio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                finish();
                return true;

            case R.id.action_submit:

                if(name.getText().toString().equals("")) {
                    newName = name.getText().toString();
                }

                if(email.getText().toString().equals("")) {
                    newEmail = email.getText().toString();
                }

                if(bio.getText().toString().equals("")) {
                    newBio = bio.getText().toString();
                    AppController.setString(this, "bio", newBio);
                }

                //asynk task updating bio
                try {
                    String update = new UpdateProfile(this, newName, newEmail, newBio).execute().get();
                } catch (InterruptedException e){

                } catch(ExecutionException e){

                }

                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("update", true);
                startActivity(intent);
                intent = new Intent(this, MainActivity_Spinner.class);
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
