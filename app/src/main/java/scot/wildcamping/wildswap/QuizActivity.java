package scot.wildcamping.wildswap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import scot.wildcamping.wildswap.Adapters.QuestionListAdapter;
import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.SubmitQuiz;
import scot.wildcamping.wildswap.Objects.Question;
import scot.wildcamping.wildswap.Objects.Quiz;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 09-Apr-16.
 *
 */
public class QuizActivity extends AppCompatActivity {

    QuestionListAdapter adapter;
    ListView mDrawerList;
    TextView info;
    //Quiz inst;
    SparseArray<Question> allQuestions;
    Question thisQuestion;
    boolean update;
    int progressValue;
    ProgressBar progress;
    TextView progressText;
    RelativeLayout frame;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    ArrayList<Integer> answers = thisUser.getAnswers();


    Boolean updateAns0 = true;
    Boolean updateAns1 = true;
    Boolean updateAns2 = true;
    Boolean updateAns3 = true;
    Boolean updateAns4 = true;
    Boolean updateAns5 = true;
    Boolean updateAns6 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        /*ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);*/

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            update = extras.getBoolean("update");
        }

        mDrawerList = (ListView)findViewById(R.id.question_listview);
        info = (TextView)findViewById(R.id.info);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        progressText = (TextView)findViewById(R.id.progressText);
        frame = (RelativeLayout) findViewById(R.id.frame);

        if(update){
            info.setVisibility(View.GONE);
        }

        allQuestions = new SparseArray<>();

        for(int i=1;  i<=7; i++){
            thisQuestion = new Question();
            String question = "@string/question" + (i);
            String answer1 = "@string/question" + (i) + "_answer1";
            String answer2 = "@string/question" + (i) + "_answer2";
            String answer3 = "@string/question" + (i) + "_answer3";
            String answer4 = "@string/question" + (i) + "_answer4";
            thisQuestion.setQuestion(getResources().getString(getResources().getIdentifier(question, null, getPackageName())));
            thisQuestion.setAnswer1(getResources().getString(getResources().getIdentifier(answer1, null, getPackageName())));
            thisQuestion.setAnswer2(getResources().getString(getResources().getIdentifier(answer2, null, getPackageName())));
            thisQuestion.setAnswer3(getResources().getString(getResources().getIdentifier(answer3, null, getPackageName())));
            thisQuestion.setAnswer4(getResources().getString(getResources().getIdentifier(answer4, null, getPackageName())));
            allQuestions.put(i-1, thisQuestion);
        }

        adapter = new QuestionListAdapter(this, allQuestions, update, false);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setClickable(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("CLICK");
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.outdooraccess-scotland.com/public/camping";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        final Handler h = new Handler();
        final int delay = 500;

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressValue = 0;

                if(answers.get(0) != 0 && updateAns0){
                    updateAns0 = false;
                    updateProgress();
                }

                if(answers.get(1) != 0 && updateAns1){
                    updateAns1 = false;
                    updateProgress();
                }

                if(answers.get(2) != 0 && updateAns2){
                    updateAns2 = false;
                    updateProgress();
                }

                if(answers.get(3) != 0 && updateAns3){
                    updateAns3 = false;
                    updateProgress();
                }

                if(answers.get(4) != 0 && updateAns4){
                    updateAns4 = false;
                    updateProgress();
                }

                if(answers.get(5) != 0 && updateAns5){
                    updateAns5 = false;
                    updateProgress();
                }

                if(answers.get(6) != 0 && updateAns6){
                    updateAns6 = false;
                    updateProgress();
                }

                h.postDelayed(this, delay);
            }
        }, delay);
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

                //question = inst.getQuestions();
                //question.clear();

                finish();
                return true;

            case R.id.action_submit:

                //question = inst.getQuestions();
                //ArrayList<Integer> newAnswers = new ArrayList<>();

                /*for(int i=0; i<allQuestions.size(); i++){
                    newAnswers.add(i, allQuestions.get(i).getAnswer());
                }*/


                final Intent intent;

                if(!update) {
                    //AppController.setString(QuizActivity.this, "newCamper", Integer.toString(answers.get(1)));
                    intent = new Intent(this, MainActivity_Spinner.class);
                    intent.putExtra("update", false);
                    intent.putExtra("new", true);
                } else {
                    intent = new Intent(this, ProfileActivity.class);
                    intent.putExtra("this_user", true);
                }

                //asynk task updating answers
                if(isNetworkAvailable()) {
                    new SubmitQuiz(this, answers, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(intent);
                            finish();
                        }
                    }).execute();
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

    public void updateProgress(){

        if(!updateAns6){
            progressValue = progress.getProgress()+16;
        } else {
            progressValue = progress.getProgress() + 14;
        }
        progress.setProgress(progressValue);
        progressText.setText(progressValue+"% Complete");

    }

}
