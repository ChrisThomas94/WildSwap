package scot.wildcamping.wildscotland;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.StoredData;

/**
 * Created by Chris on 17-Mar-16.
 */
public class ContactUser extends AppCompatActivity implements View.OnClickListener {

    SparseArray<Site> focused = new SparseArray<>();
    String contact;
    String date;

    StoredData inst = new StoredData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_user);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        EditText to = (EditText)findViewById(R.id.to);
        EditText subject = (EditText)findViewById(R.id.subject);
        EditText body = (EditText)findViewById(R.id.body);

        focused = inst.getUnknownSitesMap();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            contact = extras.getString("contact");
            date = extras.getString("date");
        }

        to.setText(contact);
        subject.setText("Wild Swap - Trade: " + date);


    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.confirmContact:

                break;

            case R.id.cancelContact:


                break;

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}