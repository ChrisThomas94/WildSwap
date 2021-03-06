package com.wildswap.wildswapapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wildswap.wildswapapp.Objects.Question;
import com.wildswap.wildswapapp.Objects.Quiz;

import com.wildswap.wildswapapp.R;

public class QuestionFragment extends Fragment {

    public QuestionFragment() {
    }

    private com.wildswap.wildswapapp.Adapters.QuestionListAdapter adapter;
    private NonScrollListView mDrawerList;
    Quiz inst;
    SparseArray<Question> allQuestions;
    Question thisQuestion;
    Boolean thisUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            thisUser = extras.getBoolean("this_user");
        }

        mDrawerList = (NonScrollListView) rootView.findViewById(R.id.question_listview);

        inst = new Quiz();
        allQuestions = new SparseArray<>();

        for(int i=1;  i<=9; i++){
            thisQuestion = new Question();
            String question = "@string/question" + (i);
            String answer1 = "@string/question" + (i) + "_answer1";
            String answer2 = "@string/question" + (i) + "_answer2";
            String answer3 = "@string/question" + (i) + "_answer3";
            String answer4 = "@string/question" + (i) + "_answer4";
            thisQuestion.setQuestion(getResources().getString(getResources().getIdentifier(question, null, getActivity().getPackageName())));
            thisQuestion.setAnswer1(getResources().getString(getResources().getIdentifier(answer1, null, getActivity().getPackageName())));
            thisQuestion.setAnswer2(getResources().getString(getResources().getIdentifier(answer2, null, getActivity().getPackageName())));
            thisQuestion.setAnswer3(getResources().getString(getResources().getIdentifier(answer3, null, getActivity().getPackageName())));
            thisQuestion.setAnswer4(getResources().getString(getResources().getIdentifier(answer4, null, getActivity().getPackageName())));
            allQuestions.put(i-1, thisQuestion);
        }
        inst.setQuestions(allQuestions);

        adapter = new com.wildswap.wildswapapp.Adapters.QuestionListAdapter(getActivity(), allQuestions, thisUser, true);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(intent);
                getActivity().finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}


