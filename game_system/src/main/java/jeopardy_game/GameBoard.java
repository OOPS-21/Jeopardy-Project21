package jeopardy_game;

/**
 * Represents the game board in a Jeopardy game.
 * Holds the categories and questions, and provides methods
 * to display the board and access questions or categories.
 */
public class GameBoard {
    private final GameData gameData;

    /**
     * Constructs a GameBoard with the given game data.
     *
     * @param gameData the data containing categories and questions for this game
     */
    public GameBoard(GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Displays the current state of the game board in the console.
     * Unanswered questions show their point value; answered questions are marked with "---".
     */
    public void displayBoard() {
        System.out.println("Jeopardy Game Board:");
    
        int colWidth = 25;

        try {
            for(Category category : gameData.getCategories()) {
                System.out.printf("| %-"+colWidth+"s", category.getName());// Display category names

                for(Question question : category.getQuestions()) {
                    if (!question.isAnswered()) {
                        System.out.print("| " + question.getPoints() + " | "); // Display point value if not answered
                    }   
                    else {
                        System.out.print("| --- | ");
                    }                
                }
                System.out.println();
            } 
        } catch (Exception e) {
            System.err.println("Error displaying game board: " + e.getMessage());
        }
        System.out.println("\n");
    }

    /**
     * Returns the game data associated with this board.
     *
     * @return the GameData object
     */
    public GameData getGameData() {
        return this.gameData;
    } 

    /**
     * Returns the Category object matching the given name.
     *
     * @param name the name of the category
     * @return the Category if found, otherwise null
     */
    public Category getCategory(String name) {
        for (Category c : gameData.getCategories()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Returns the Question object for a given category and point value.
     *
     * @param category the category of the question
     * @param pointValue the point value of the question
     * @return the Question object if found, otherwise null
     */
    public Question getQuestion(Category category, int pointValue) {
        return gameData.getQuestion(category, pointValue);
    }

    /**
     * Marks a question in the specified category and point value as answered.
     *
     * @param category the category of the question
     * @param pointValue the point value of the question
     */
    public void markQuestion(Category category, int pointValue) {
        Question question = gameData.getQuestion(category, pointValue);
        if (question != null) {
            question.setAnswered(true);
        }
    }

    /**
     * Checks if all questions on the board have been answered.
     *
     * @return true if all questions are answered, false otherwise
     */
    public boolean allQuestionsAnswered() { 
        for (Category category : gameData.getCategories()) {
            for (Question question : category.getQuestions()) {
                if (!question.isAnswered()) {
                    return false;
                }
            }
        }
        return true;
    }
}