package scot.wildcamping.wildscotland.model;

import android.util.SparseArray;

/**
 * Created by Chris on 09-Apr-16.
 */
public class Quiz {

    private static SparseArray<Question> questions = new SparseArray<>();

    public void setQuestions(SparseArray<Question> answers){
        questions = answers;
    }

    public SparseArray<Question> getQuestions(){
        return questions;
    }

}
