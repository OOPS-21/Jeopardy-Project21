package jeopardy_game;

import java.util.List;
import java.util.Scanner;

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

    //extra getters
     public int getIndex() {
        return this.index;
    }
    public String getName() {
        return this.name;
    }

    public Question selectQuestion(GameBoard board) {
        Scanner sc = new Scanner(System.in);
        List<Category> categories = board.getGameData().getCategories();
        Category selectedCategory = null;

        // Select category
        while (selectedCategory == null) {
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }

            System.out.print("Choose a category by number: ");
            int catIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (catIndex >= 0 && catIndex < categories.size()) {
                selectedCategory = categories.get(catIndex);
            } else {
                System.out.println("Invalid category number. Try again.");
            }
        }

        // Select question
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

            System.out.print("Choose a question by number: ");
            int qIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (qIndex >= 0 && qIndex < questions.size() && !questions.get(qIndex).isAnswered()) {
                selectedQuestion = questions.get(qIndex);
            } else {
                System.out.println("Invalid choice or question already answered. Try again.");
            }
        }

        return selectedQuestion;
    }

    public String getAnswer() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose an answer by letter: ");
        String answer = sc.next();
        return answer.trim();
    }

    @Override
    public String toString() {
        return "Player{" + "index=" + index + ", name='" + name + '\'' + ", score=" + score + '}';
    }

}