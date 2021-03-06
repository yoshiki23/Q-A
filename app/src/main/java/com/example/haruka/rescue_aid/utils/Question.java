package com.example.haruka.rescue_aid.utils;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Tomoya on 8/29/2017 AD.
 * This is a question class
 */

public class Question implements Serializable {

    private static final long serialVersionUID = Utils.serialVersionUID_Question;

    public final static int MAX_URGENCY = Utils.MAX_URGENCY, MIN_URGENCY = Utils.MIN_URGNECY;

    private int index;
    private int yesIndex, noIndex;
    private int yesUrgency, noUrgency;
    private boolean[] yesCare, noCare;
    private String question;
    private boolean answer;
    public boolean isAnswered;
    public boolean isUnsure;

    public Question(){
        index = -1;
        yesIndex = -100;
        noIndex = -100;
        yesUrgency = MIN_URGENCY;
        noUrgency = MIN_URGENCY;
        yesCare = new boolean[Utils.NUM_CARE];
        noCare = new boolean[Utils.NUM_CARE];
        question = "This question is invalid";
        answer = false;
        isAnswered = false;
        isUnsure = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;
        this.yesUrgency = MIN_URGENCY;
        this.noUrgency = MIN_URGENCY;
        yesCare = new boolean[Utils.NUM_CARE];
        noCare = new boolean[Utils.NUM_CARE];

        answer = false;
        isAnswered = false;
        isUnsure = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex, int yesUrgency, int noUrgency){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;
        this.yesUrgency = yesUrgency; //Math.min(Math.max(yesUrgency, MIN_URGENCY), MAX_URGENCY);
        this.noUrgency = noUrgency; //Math.min(Math.max(noUrgency, MIN_URGENCY), MAX_URGENCY);
        yesCare = new boolean[Utils.NUM_CARE];
        noCare = new boolean[Utils.NUM_CARE];

        answer = false;
        isAnswered = false;
        isUnsure = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex, int yesUrgency, int noUrgency, boolean[] yesCare, boolean[] noCare){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;
        this.yesUrgency = yesUrgency; //Math.min(Math.max(yesUrgency, MIN_URGENCY), MAX_URGENCY);
        this.noUrgency = noUrgency; //Math.min(Math.max(noUrgency, MIN_URGENCY), MAX_URGENCY);
        this.yesCare = yesCare;
        this.noCare = noCare;

        answer = false;
        isAnswered = false;
        isUnsure = false;
    }

    public String toString(){
        String res = this.getClass().getSimpleName().toString();
        res += "  , index : " + Integer.toString(index);
        res += "  , text : " + question;
        res += "  , yes index : " + Integer.toString(yesIndex);
        res += "  , no index : " + Integer.toString(noIndex);
        res += "  , yes emergency urgency : " + Integer.toString(yesUrgency);
        res += "  , no emergency urgency : " + Integer.toString(noUrgency);
        return res;
    }

    public int getIndex(){
        return index;
    }

    public int getNextIndex(boolean answer){
        if (answer == InterviewAnswers.YES){
            return getYesIndex();
        }else{
            return getNoIndex();
        }
    }

    public int getNextIndex(){
        if (answer == InterviewAnswers.YES){
            return getYesIndex();
        }else{
            return getNoIndex();
        }
    }

    public int getYesIndex(){
        return yesIndex;
    }

    public int getNoIndex(){
        return noIndex;
    }

    public int getYesUrgency(){
        return Math.abs(yesUrgency);
    }

    public int getNoUrgency(){
        return Math.abs(noUrgency);
    }

    public int getUrgency(){
        Log.d("currentquestionUrgency", "unsure : " + Boolean.toString(isUnsure));
        if(isUnsure){
            Log.d("currentquestionUrgency", getYesUrgency() + ", " + getNoUrgency());
            return Math.max(getYesUrgency(), getNoUrgency());
        } else if (answer){
            return getYesUrgency();
        } else {
            return getNoUrgency();
        }
    }

    public boolean compareUrgency(){
        return yesUrgency > noUrgency;
    }

    public String getQuestion(){
        return question;
    }

    public void answer(boolean a){
        answer = a;
    }

    public boolean getAnswer() {
        return answer;
    }

    public String getAnswerString() {
        if (answer){
            return Utils.ANSWER_JP_YES;
        } else {
            return Utils.ANSWER_JP_NO;
        }
    }


    public boolean[] getCares(){
        if (answer){
            return yesCare;
        } else {
            return noCare;
        }
    }

    public String getCareString(){
        boolean[] cares = (answer) ? yesCare : noCare;
        String s = "";
        for (boolean c : cares){
            s += c ? "Y" : "N";
        }

        return s;
    }

}
