package scot.wildcamping.wildswap.Objects;

/**
 * Created by Chris on 09-Apr-16.
 */
public class Question {

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int answer;

    public void setQuestion(String question){
        this.question = question;
    }

    public String getQuestion(){
        return this.question;
    }

    public void setAnswer1(String answer1){
        this.answer1 = answer1;
    }

    public String getAnswer1(){
        return this.answer1;
    }

    public void setAnswer2(String answer2){
        this.answer2 = answer2;
    }

    public String getAnswer2(){
        return this.answer2;
    }

    public void setAnswer3(String answer3){
        this.answer3 = answer3;
    }

    public String getAnswer3(){
        return this.answer3;
    }

    public void setAnswer4(String answer4){
        this.answer4 = answer4;
    }

    public String getAnswer4(){
        return this.answer4;
    }

    public void setAnswer(int answer){
        this.answer = answer;
    }

    public int getAnswer(){
        return answer;
    }
}
