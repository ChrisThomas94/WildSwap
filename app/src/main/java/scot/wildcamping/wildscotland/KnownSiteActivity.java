package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Chris on 26-Feb-16.
 */
public class KnownSiteActivity extends Activity implements View.OnClickListener {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_known_site);

        Button back = (Button)findViewById(R.id.leaveSite);
        Button edit = (Button)findViewById(R.id.editSite);

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.leaveSite:
                intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.editSite:
                intent = new Intent(getApplicationContext(),MainActivity.class);
                //bundle all current details into "add site"
                startActivity(intent);
                finish();
                break;

        }

    }

}
