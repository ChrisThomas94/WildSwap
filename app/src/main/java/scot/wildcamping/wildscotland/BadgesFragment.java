package scot.wildcamping.wildscotland;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import scot.wildcamping.wildscotland.Adapters.QuestionListAdapter;
import scot.wildcamping.wildscotland.Objects.Question;
import scot.wildcamping.wildscotland.Objects.Quiz;

public class BadgesFragment extends Fragment {

    public BadgesFragment() {
    }

    private QuestionListAdapter adapter;
    private ListView mDrawerList;
    Quiz inst;
    SparseArray<Question> question;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.question_listview);

        inst = new Quiz();
        question = new SparseArray<>();
        question = inst.getQuestions();

        adapter = new QuestionListAdapter(getActivity(), question, false, false);
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


