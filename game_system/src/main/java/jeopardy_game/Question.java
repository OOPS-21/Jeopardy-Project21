package jeopardy_game;

import java.util.Map;

public class Question {
    private String questionStr;
    private int value;
    private Map<String, String> options;
    private String answer;
    private boolean answered;

    public Question(String questionStr, int value, Map<String, String> options, String answer) {
        this.questionStr = questionStr;
        this.value = value;
        this.options = options;
        this.answer = answer;
        this.answered = false;
    }

    // Getters
    public boolean isAnswered() {return answered;}
    public String getQuestionStr() {return questionStr;}
    public Map<String, String> getOptions() {return options;}
    public String getAnswer() {return answer;}
    public int getValue() {return value;}

    // Setters
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }  
}
