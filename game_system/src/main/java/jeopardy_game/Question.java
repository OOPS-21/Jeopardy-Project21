package jeopardy_game;

import java.util.Map;

public class Question {
    private String questionStr;
    private Map<String, String> options;
    private String correctAnswer;
    private int points;
    private boolean answered;

    public Question(String questionStr, int points, Map<String, String> options, String correctAnswer) {
        this.questionStr = questionStr;
        this.points = points;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.answered = false;
    }

    // Getters
    public boolean isAnswered() {return answered;}
    public String getQuestionStr() {return questionStr;}
    public Map<String, String> getOptions() {return options;}
    public String getCorrectAnswer() {return correctAnswer;}
    public int getPoints() {return points;}

    // Setters
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }  

    public void display() {
        System.out.println(questionStr);
        for (Map.Entry<String, String> option : options.entrySet()) {
            System.out.println(option.getKey() + ": " + option.getValue());
        }
    }

    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }

}
