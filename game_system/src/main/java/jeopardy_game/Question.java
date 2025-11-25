package jeopardy_game;

public class Question {
    private String questionStr;
    private String options[];
    private char answer;
    private int points;
    private boolean answered;

    public Question(String questionStr, String[] options, char answer, int points) {
        this.questionStr = questionStr;
        this.options = options;
        this.answer = answer;
        this.points = points;
        this.answered = false;
    }

    // Getters
    public boolean isAnswered() {
        return answered;
    }
    public String getQuestionStr() {
        return questionStr;
    }
    public String[] getOptions() {
        return options;
    }
    public char getAnswer() {
        return answer;
    }
    public int getPoints() {
        return points;
    }


    // Setters
    public void setQuestionStr(String questionStr) {
        this.questionStr = questionStr;
    }
    public void setOptions(String[] options) {
        this.options = options;
    }
    public void setAnswer(char answer) {
        this.answer = answer;
    }
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    
}
