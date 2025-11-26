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

    public Category selectCategory(GameBoard board) {
        Scanner sc = new Scanner(System.in);
        List<Category> categories = board.getGameData().getCategories();
        Category selectedCategory = null;

        while (selectedCategory == null) {
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                boolean hasUnanswered = false;
                for (Question q : cat.getQuestions()) {
                    if (!q.isAnswered()) {
                        hasUnanswered = true;
                        break;
                    }
                }

                if (hasUnanswered) {
                    System.out.println((i + 1) + ". " + cat.getName());
                }
            }

            System.out.print("Choose a category by number: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("end")) {
                return null;
            }

            int catIndex;
            try {
                catIndex = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (catIndex >= 0 && catIndex < categories.size()) {
                Category cat = categories.get(catIndex);

                boolean hasUnanswered = false;
                for (Question q : cat.getQuestions()) {
                    if (!q.isAnswered()) {
                        hasUnanswered = true;
                        break;
                    }
                }

                if (hasUnanswered) {
                    selectedCategory = cat;
                } else {
                    System.out.println("All questions in that category have been answered. Choose another.");
                }
            } else {
                System.out.println("Invalid category number. Try again.");
            }
        }

        return selectedCategory;
    }

    public Question selectQuestion(GameBoard board, Category selectedCategory) {        
        if (selectedCategory == null) {
            return null;
        }

        Scanner sc = new Scanner(System.in);
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
            String input = sc.nextLine().trim();
            
            if (input.equalsIgnoreCase("end")) {
                return null;
            }

            int qIndex;
            try {
                qIndex = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (qIndex >= 0 && qIndex < questions.size() && !questions.get(qIndex).isAnswered()) {
                selectedQuestion = questions.get(qIndex);
            } else {
                System.out.println("Invalid choice or question already answered. Try again.");
            }
        }

        return selectedQuestion;
    }

    public String getAnswer(Question q) {
        Scanner sc = new Scanner(System.in);
        String answer = "";
        while (true) {
            System.out.print("Choose an answer by letter: ");
            answer = sc.next().trim().toUpperCase();

            if (answer.equalsIgnoreCase("end")) {
                return null;
            }

            if (q.getOptions().containsKey(answer)) {
                break;
            } else {
                System.out.println("Invalid input. Please choose one of the available options: " 
                    + q.getOptions().keySet());
            }
        }
        return answer;
    }

    @Override
    public String toString() {
        return "Player{" + "index=" + index + ", name='" + name + '\'' + ", score=" + score + '}';
    }

}