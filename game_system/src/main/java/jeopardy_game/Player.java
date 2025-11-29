package jeopardy_game;

import java.util.List;

/**
 * Represents a player in the Jeopardy game.
 * Each player has a name, an index, and a score.
 * Handles the selection of categories, questions and answering using an InputHandler.
 */
public class Player {
    private int index;
    private String name;
    private int score;

    /**
     * Constructs a Player with the given index and name.
     * Score is initialized to 0.
     *
     * @param index the index of the player in the game
     * @param name the name of the player
     */
    public Player(int index, String name) {
        this.index = index;
        this.name = name;
        this.score = 0;
    }

    /**
     * Adds points to the player's score.
     *
     * @param p the number of points to add
     */
    public void addPoints(int p) {
        this.score += p;
    }

    /**
     * Subtracts points from the player's score.
     *
     * @param p the number of points to subtract
     */
    public void subtractPoints(int p) {
        this.score -= p;
    }

    /**
     * Returns the current score of the player.
     *
     * @return the player's score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the index of the player.
     *
     * @return the player's index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Prompts the player to select a category from the game board using the InputHandler.
     * Displays only categories that have unanswered questions.
     *
     * @param board the game board containing categories and questions
     * @param input the input handler to read user input
     * @return the selected Category or null if the player chooses to end
     */
    public Category selectCategory(GameBoard board, InputHandler input) {
        List<Category> categories = board.getGameData().getCategories();
        Category selectedCategory;

        while (true) {
            for (int i = 0; i < categories.size(); i++) {
                Category c = categories.get(i);
                boolean hasUnanswered = false;
                for (Question q : c.getQuestions()) {
                    if (!q.isAnswered()) {
                        hasUnanswered = true;
                        break;
                    }
                }

                if (hasUnanswered) {
                    System.out.println((i + 1) + ". " + c.getName());
                }
            }

            selectedCategory = input.getCategoryInput(categories);
            if (selectedCategory == null) return null;

            if (!selectedCategory.getName().equals("INVALID")) {
                return selectedCategory;
            }
        }
    }

    /**
     * Prompts the player to select a question from a given category using the InputHandler.
     * Displays only unanswered questions.
     *
     * @param board the game board
     * @param selectedCategory the category from which to select a question
     * @param input the input handler to read user input
     * @return the selected Question or null if the player chooses to end
     */
    public Question selectQuestion(GameBoard board, Category selectedCategory, InputHandler input) {        
        if (selectedCategory == null) {
            return null;
        }

        List<Question> questions = selectedCategory.getQuestions();
        Question selectedQuestion;

        while (true) {
            System.out.println("Select a question:");
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                if (!q.isAnswered()) {
                    System.out.println((i + 1) + ". " + q.getPoints() + " points");
                }
            }

            selectedQuestion = input.getQuestionInput(questions);
            if (selectedQuestion == null) return null;
            if (!selectedQuestion.getQuestionStr().equals("INVALID")) {
                return selectedQuestion;
            }
        }
    }

    /**
     * Prompts the player to provide an answer for a given question using the InputHandler.
     *
     * @param q the question to answer
     * @param input the input handler to read user input
     * @return the answer as a letter string or null if the player chooses to end
     */
    public String getAnswer(Question q, InputHandler input) {
        return input.getAnswerInput(q);
    }
}