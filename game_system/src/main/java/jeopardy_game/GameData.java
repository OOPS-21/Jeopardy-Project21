package jeopardy_game;
import java.util.List;

public class GameData {
    private List <Category> categories;

    public GameData(List<Category> categories) {
        this.categories = categories;
    }

    // Getters
    public List<Category> getCategories() {
        return categories;
    }
    public Question getQuestion(Category category, int pointValue) { // pull question from category based on point value
        for (Category cat : categories) {
            if (cat.equals(category)) {
                for (Question question : cat.getQuestions()) {
                    if (question.getValue() == pointValue) {
                        return question;
                    }
                }
            }
        }
        System.err.println("Question not found for category: " + category.getName() + " with point value: " + pointValue);
        return null; // Question not found
    }
    

    // Not sure if this class will be used in the set up so I'm just adding these methods to populate the game data
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    public void addCategory(Category category) {
        this.categories.add(category);
    }
    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
    public void addQuestionToCategory(Category category, Question newQuestion) {
        if (this.categories.contains(category)) {
            int index = this.categories.indexOf(category);
            this.categories.get(index).addQuestion(newQuestion);
        }

    }

}
