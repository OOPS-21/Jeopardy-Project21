package jeopardy_game;

import java.util.Map;

/**
 * Represents a single question in the Jeopardy game,
 * including its text, point value, options, correct answer
 * and whether it has been answered.
 */
public class Question {
    private String questionStr;
    private int points;
    private Map<String, String> options;
    private String correctAnswer;
    private boolean answered;

    /**
     * Constructs a new Question with the specified parameters.
     *
     * @param questionStr   the text of the question
     * @param points        the point value of the question
     * @param options       a map of answer options, keyed by a letter (e.g., "A", "B")
     * @param correctAnswer the letter corresponding to the correct answer
     */
    public Question(String questionStr, int points, Map<String, String> options, String correctAnswer) {
        this.questionStr = questionStr;
        this.points = points;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.answered = false;
    }

    /**
     * Displays the question and its answer options to the console.
     */

    public void display() {
        System.out.println(questionStr);
        for (Map.Entry<String, String> option : options.entrySet()) {
            System.out.println(option.getKey() + ": " + option.getValue());
        }
    }

    /**
     * Checks whether the provided answer is correct.
     *
     * @param answer the player's answer (letter key)
     * @return true if the answer is correct; false otherwise
     */
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }

    /**
     * Marks this question as answered or unanswered.
     *
     * @param answered true to mark as answered, false otherwise
     */
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }  

    /**
     * Returns the question text.
     *
     * @return the question string
     */
    public String getQuestionStr() {
        return questionStr;
    }

    /**
     * Returns the point value of this question.
     *
     * @return the points
     */
    public int getPoints() {
        return points;
    }   

    /**
     * Returns the map of answer options.
     *
     * @return a map of options keyed by letters
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * Returns the letter of the correct answer.
     *
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Returns whether the question has already been answered.
     *
     * @return true if answered; false otherwise
     */
    public boolean isAnswered() {
        return answered;
    }
}
