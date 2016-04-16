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
import android.widget.ListView;

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
    Quiz inst;
    SparseArray<Question> question;
    boolean update;

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
        }

        mDrawerList = (ListView)findViewById(R.id.question_listview);

        inst = new Quiz();
        question = new SparseArray<>();
        question = inst.getQuestions();

        adapter = new QuestionListAdapter(this, question, update);
        mDrawerList.setAdapter(adapter);

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

                question = inst.getQuestions();
                int answer1 = question.get(0).getAnswer();
                System.out.println("new asnwer " + answer1);
                int answer2 = question.get(1).getAnswer();
                System.out.println("new asnwer " + answer2);

                int answer3 = 0; //question.get(2).getAnswer();
                int answer4 = 0; //question.get(3).getAnswer();
                int answer5 = 0; //question.get(4).getAnswer();

                //asynk task updating answers
                if(isNetworkAvailable()) {
                    try {
                        String answers = new SubmitQuiz(this,answer1, answer2, answer3, answer4, answer5).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }

                    try {
                        String questions = new FetchQuestions(this, AppController.getString(this, "email")).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent intent = new Intent(this, MainActivity_Spinner.class);
                startActivity(intent);
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
