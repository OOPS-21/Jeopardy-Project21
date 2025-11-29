package jeopardy_game;

import java.util.List;

/**
 * Holds the categories and questions for a Jeopardy game.
 * Provides access to categories and allows retrieval of specific questions.
 */
public class GameData {
    private List <Category> categories;

    /**
     * Constructs a GameData object with the provided list of categories.
     *
     * @param categories the list of categories for this game
     */
    public GameData(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Returns the list of categories in this game data.
     *
     * @return a List of Category objects
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Retrieves a question from the specified category with the given point value.
     *
     * @param category the Category object to search within
     * @param pointValue the point value of the desired question
     * @return the Question object if found, otherwise null
     */
    public Question getQuestion(Category category, int pointValue) {
        for (Category cat : categories) {
            if (cat.equals(category)) {
                for (Question question : cat.getQuestions()) {
                    if (question.getPoints() == pointValue) {
                        return question;
                    }
                }
            }
        }
        System.err.println("Question not found for category: " + category.getName() + " with point value: " + pointValue);
        return null;
    }
}
