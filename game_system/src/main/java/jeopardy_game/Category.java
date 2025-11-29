package jeopardy_game;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Question> questions;

    public Category(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    // Getters
    public String getName() {return name;}
    public List<Question> getQuestions() {return questions;}

    // Setters
    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion); 
    }
}
