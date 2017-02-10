package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.adapter.QuestionListAdapter;
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Quiz;

/**
 * Created by Chris on 09-Apr-16.
 */
public class QuizActivity extends AppCompatActivity {

    private QuestionListAdapter adapter;
    private ListView mDrawerList;
    TextView info;
    Quiz inst;
    SparseArray<Question> question;
    boolean update;
    boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            update = extras.getBoolean("update");
            isNew = extras.getBoolean("new");
        }

        mDrawerList = (ListView)findViewById(R.id.question_listview);
        info = (TextView)findViewById(R.id.info);

        if(!update){
            info.setVisibility(View.GONE);
        }

        inst = new Quiz();
        question = new SparseArray<>();
        question = inst.getQuestions();

        adapter = new QuestionListAdapter(this, question, update, isNew);
        mDrawerList.setAdapter(adapter);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.outdooraccess-scotland.com/public/camping";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

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

                question = inst.getQuestions();
                question.clear();

                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                finish();
                return true;

            case R.id.action_submit:

                question = inst.getQuestions();
                int answer1 = question.get(0).getAnswer();
                System.out.println("new asnwer " + answer1);
                int answer2 = question.get(1).getAnswer();
                System.out.println("new asnwer " + answer2);
                int answer3 = question.get(2).getAnswer();
                int answer4 = question.get(3).getAnswer();
                int answer5 = question.get(4).getAnswer();
                int answer6 = question.get(5).getAnswer();
                int answer7 = question.get(6).getAnswer();
                int answer8 = question.get(7).getAnswer();
                int answer9 = question.get(8).getAnswer();

                //asynk task updating answers
                if(isNetworkAvailable()) {
                    try {
                        String answers = new SubmitQuiz(this,answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }

                    /*try {     unnecessary??
                        String questions = new FetchQuestions(this, AppController.getString(this, "email")).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }*/
                }

                if(isNew) {
                    Intent intent = new Intent(this, MainActivity_Spinner.class);
                    intent.putExtra("new", true);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    intent.putExtra("this_user", true);
                    startActivity(intent);
                    finish();
                }
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
