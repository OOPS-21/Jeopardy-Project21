package jeopardy_game;

import java.util.List;

public class Player {
    private int index;
    private String name;
    private int score;

    public Player(int index, String name) {
        this.index = index;
        this.name = name;
        this.score = 0;
    }

    public int getScore() {
        return this.score;
    }

    public void addPoints(int p) {
        this.score += p;
    }

    public void subtractPoints(int p) {
        this.score -= p;
    }

    public int getIndex() {
        return this.index;
    }
    public String getName() {
        return this.name;
    }

    public Category selectCategory(GameBoard board, InputHandler input) {
        List<Category> categories = board.getGameData().getCategories();
        Category selectedCategory = null;

        while (selectedCategory == null) {
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
        }

        return selectedCategory;
    }

    public Question selectQuestion(GameBoard board, Category selectedCategory, InputHandler input) {        
        if (selectedCategory == null) {
            return null;
        }

        List<Question> questions = selectedCategory.getQuestions();
        Question selectedQuestion = null;

        while (selectedQuestion == null) {
            System.out.println("Select a question:");
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                if (!q.isAnswered()) {
                    System.out.println((i + 1) + ". " + q.getPoints() + " points");
                }
            }

            selectedQuestion = input.getQuestionInput(questions);
        }

        return selectedQuestion;
    }

    public String getAnswer(Question q, InputHandler input) {
        return input.getAnswerInput(q);
    }

    @Override
    public String toString() {
        return "Player{" + "index=" + index + ", name='" + name + '\'' + ", score=" + score + '}';
    }
}