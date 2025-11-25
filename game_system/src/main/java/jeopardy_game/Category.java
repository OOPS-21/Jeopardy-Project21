package jeopardy_game;

import java.util.List;

public class Category {
    private String name;
    private List<Question> questions;

    public Category(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }


    // Getters
    public String getName() {
        return name;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    // Setters
    public void setName(String name){
        this.name = name;
    }
    public void addQuestion(Question newQuestion){
        this.questions.add(newQuestion); 
    }

}
