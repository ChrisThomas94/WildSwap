package scot.wildcamping.wildscotland.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import scot.wildcamping.wildscotland.R;
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Site;

/**
 * Created by Chris on 18-Mar-16.
 */
public class QuestionListAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<Question> questions;

    public QuestionListAdapter(Context context, SparseArray<Question> questions){
        this.context = context;
        this.questions = questions;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.question_list, null);
        }

        if(position == 0){
            TextView info = (TextView) convertView.findViewById(R.id.info);
            info.setVisibility(View.VISIBLE);
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

        answers.check(questions.get(position).getAnswer());

        //questions.get(position).setAnswer(answers.getCheckedRadioButtonId());

        return convertView;
    }

}
