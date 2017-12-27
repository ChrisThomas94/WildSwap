package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.wildswap.wildswapapp.Adapters.QuestionListAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.SubmitQuiz;
import com.wildswap.wildswapapp.Objects.Question;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 09-Apr-16.
 *
 */
public class QuizActivity extends AppCompatActivity {

    QuestionListAdapter adapter;
    ListView mDrawerList;
    SparseArray<Question> allQuestions;
    Question thisQuestion;
    boolean update;
    int progressValue;

    ProgressBar progress;
    TextView progressText;
    RelativeLayout progressLayout;
    RelativeLayout frame;

    StoredData inst = new StoredData();
    User user = inst.getLoggedInUser();
    ArrayList<Integer> answers = user.getAnswers();
    Boolean thisUser = true;
    Boolean complete = false;

    Boolean updateAns0 = true;
    Boolean updateAns1 = true;
    Boolean updateAns2 = true;
    Boolean updateAns3 = true;
    Boolean updateAns4 = true;
    Boolean updateAns5 = true;
    Boolean updateAns6 = true;
    Boolean updateAns7 = true;
    Boolean updateAns8 = true;


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

        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        mDrawerList = (ListView)findViewById(R.id.question_listview);
        progress = (ProgressBar)findViewById(R.id.progressBar);
        progressText = (TextView)findViewById(R.id.progressText);
        frame = (RelativeLayout) findViewById(R.id.frame);

        if(!update) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("One Last Thing!");
            builder1.setMessage(getResources().getString(R.string.quizText));
            builder1.setPositiveButton("LET ME IN!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }

        allQuestions = new SparseArray<>();

        for(int i=1;  i<=9; i++){
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

        adapter = new QuestionListAdapter(this, allQuestions, thisUser, false);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setClickable(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("CLICK");
            }
        });

        final Handler h = new Handler();
        final int delay = 500;

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswers();
            }
        });

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

                if(answers.get(7) != 0 && updateAns7){
                    updateAns7 = false;
                    updateProgress();
                }

                if(answers.get(8) != 0 && updateAns8){
                    updateAns8 = false;
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

                finish();
                return true;
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

        if(!updateAns8){
            progressValue = progress.getProgress()+12;
        } else {
            progressValue = progress.getProgress() + 11;
        }
        progress.setProgress(progressValue);

        if(progressValue >= 100){
            complete = true;
            progressText.setText("GO");
        } else {
            progressText.setText(progressValue + "% Complete");
        }

    }

    public void submitAnswers(){
        final Intent intent;

        if(!update) {
            //AppController.setString(QuizActivity.this, "newCamper", Integer.toString(answers.get(1)));
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("update", false);
            intent.putExtra("new", true);
            intent.putExtra("data", true);
            intent.putExtra("quizComplete", complete);

        } else {
            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("this_user", true);
            intent.putExtra("quizComplete", complete);
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
        } else {
            //Snackbar;
        }
    }

}
