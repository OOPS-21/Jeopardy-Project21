package jeopardy_game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category in the Jeopardy game, containing a name
 * and a list of questions belonging to this category.
 */
public class Category {
    private String name;
    private List<Question> questions;

    /**
     * Constructs a new Category with the given name.
     *
     * @param name the name of the category
     */
    public Category(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    /**
     * Returns the name of this category.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of questions in this category.
     *
     * @return the questions list
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Adds a new question to this category.
     *
     * @param newQuestion the Question object to add
     */
    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion); 
    }
}
