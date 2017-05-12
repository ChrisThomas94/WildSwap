package scot.wildcamping.wildswap.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import scot.wildcamping.wildswap.Objects.Quiz;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;
import scot.wildcamping.wildswap.R;
import scot.wildcamping.wildswap.Objects.Question;

/**
 * Created by Chris on 18-Mar-16.
 *
 */
public class QuestionListAdapter extends BaseAdapter {

    private Context context;
    private Quiz quiz = new Quiz();
    private SparseArray<Question> questions;
    private boolean update;
    private boolean display;
    private StoredData inst = new StoredData();
    private User thisUser = inst.getLoggedInUser();

    public QuestionListAdapter(Context context, SparseArray<Question> questions, boolean update, boolean display){
        this.context = context;
        this.questions = questions;
        this.update = update;
        this.display = display;
    }

    private static final class ViewHolder {
        TextView question;
        RadioGroup answers;
        RadioButton answer1;
        RadioButton answer2;
        RadioButton answer3;
        RadioButton answer4;
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

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_question, null);
            holder = new ViewHolder();
            holder.question = (TextView)convertView.findViewById(R.id.question);
            holder.answers = (RadioGroup)convertView.findViewById(R.id.answer_group);
            holder.answer1 = (RadioButton)convertView.findViewById(R.id.answer1);
            holder.answer2 = (RadioButton)convertView.findViewById(R.id.answer2);
            holder.answer3 = (RadioButton)convertView.findViewById(R.id.answer3);
            holder.answer4 = (RadioButton)convertView.findViewById(R.id.answer4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(questions.get(position) != null){
            holder.question.setText(questions.get(position).getQuestion());
            holder.answer1.setText(questions.get(position).getAnswer1());
            holder.answer2.setText(questions.get(position).getAnswer2());
            holder.answer3.setText(questions.get(position).getAnswer3());
            holder.answer4.setText(questions.get(position).getAnswer4());

        } else {
            holder.question.setText("");
        }

        holder.answers.setOnCheckedChangeListener(null);
        holder.answers.clearCheck();

        //if(thisUser.getAnswers().get(position) > -1){
        if(questions.get(position).getAnswer()>-1){
            holder.answers.check(questions.get(position).getAnswer());
            //holder.answers.check(thisUser.getAnswers().get(position));
        } else {
            holder.answers.clearCheck();
        }

        holder.answers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                questions = quiz.getQuestions();

                if (checkedId > -1) {
                    //thisUser.getAnswers().get(position);
                    questions.get(position).setAnswer(checkedId);
                    quiz.setQuestions(questions);
                    System.out.println("adapter click");

                } else {
                    if (questions.get(position).getAnswer() > -1) {
                        questions.removeAt(questions.get(position).getAnswer());
                    }
                }
            }
        });

        if(!display){
            holder.answer1.setClickable(true);
            holder.answer2.setClickable(true);
            holder.answer3.setClickable(true);
            holder.answer4.setClickable(true);
        }

        return convertView;
    }

}
