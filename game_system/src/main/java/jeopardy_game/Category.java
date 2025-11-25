package jeopardy_game;

import java.util.List;

public class Category {
    private String name;
    private List<Question> questions;

    public Category(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {return name;}
    public List<Question> getQuestions() {return questions;}

    // Setters
    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion); 
    }

}
