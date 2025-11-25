package jeopardy_game;

public class GameBoard {
    private GameData gameData;
    //private int currentRound;

    public GameBoard(GameData gameData) {
        this.gameData = gameData;
    }


    public void displayBoard() {
        System.out.println("Jeopardy Game Board:");

    
        try {
            for(Category category : gameData.getCategories()) {
                System.out.print("| " + category.getName() + " | "); // Display category names

                for(Question question : category.getQuestions()) {
                    if (!question.isAnswered()) {
                        System.out.print("| " + question.getValue() + " | "); // Display point value if not answered
                    }                   
                }
                System.out.println();
            } 
        } catch (Exception e) {
            System.err.println("Error displaying game board: " + e.getMessage());
        }
    }




    public Question getQuestion(Category category, int pointValue) {
        return gameData.getQuestion(category, pointValue);
    }
    public void markQuestion(Category category, int pointValue) {
        Question question = gameData.getQuestion(category, pointValue);
        if (question != null) {
            question.setAnswered(true);
        }
    }

    /*
    Check if all questions have been answered.
    @return true if all questions are answered, false otherwise
    Potentially used to determine end of game
     */
    public boolean allQuestionsAnswered() { 
        for (Category category : gameData.getCategories()) { // iterate through all categories
            for (Question question : category.getQuestions()) { // iterate through all questions in category
                if (!question.isAnswered() == false) { // if any question is not answered
                    return false; // not all questions are answered
                }
            }
        }
        return true; // all questions are answered
    }
}
