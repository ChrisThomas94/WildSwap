package scot.wildcamping.wildscotland.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import scot.wildcamping.wildscotland.R;
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Site;

/**
 * Created by Chris on 18-Mar-16.
 */
public class QuestionListAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<Question> questions;
    private boolean update;
    private boolean isNew;

    public QuestionListAdapter(Context context, SparseArray<Question> questions, boolean update, boolean isNew){
        this.context = context;
        this.questions = questions;
        this.update = update;
        this.isNew = isNew;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.question_list, null);
        }

        final TextView question = (TextView)convertView.findViewById(R.id.question);

        RadioGroup answers = (RadioGroup)convertView.findViewById(R.id.answer_group);
        RadioButton answer1 = (RadioButton)convertView.findViewById(R.id.answer1);
        RadioButton answer2 = (RadioButton)convertView.findViewById(R.id.answer2);
        RadioButton answer3 = (RadioButton)convertView.findViewById(R.id.answer3);
        RadioButton answer4 = (RadioButton)convertView.findViewById(R.id.answer4);

        question.setText(questions.get(position).getQuestion());
        answer1.setText(questions.get(position).getAnswer1());
        answer2.setText(questions.get(position).getAnswer2());
        answer3.setText(questions.get(position).getAnswer3());
        answer4.setText(questions.get(position).getAnswer4());

        answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.answer1) {
                    questions.get(position).setAnswer(1);
                } else if (checkedId == R.id.answer2) {
                    questions.get(position).setAnswer(2);
                } else if (checkedId == R.id.answer3) {
                    questions.get(position).setAnswer(3);
                } else if (checkedId == R.id.answer4) {
                    questions.get(position).setAnswer(4);
                }
            }
        });

        if(!isNew) {

            if(questions.get(position).getAnswer() == 0){

            } else if (questions.get(position).getAnswer() == 1) {
                answers.check(R.id.answer1);
            } else if (questions.get(position).getAnswer() == 2) {
                answers.check(R.id.answer2);
            } else if (questions.get(position).getAnswer() == 3) {
                answers.check(R.id.answer3);
            } else if (questions.get(position).getAnswer() == 4) {
                answers.check(R.id.answer4);
            }
        }

        if(update || isNew){
            answer1.setClickable(true);
            answer2.setClickable(true);
            answer3.setClickable(true);
            answer4.setClickable(true);
        }

        return convertView;
    }

}
