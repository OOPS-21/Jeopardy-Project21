package jeopardy_game;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user input for the Jeopardy game.
 * Provides methods to select files, players, categories, questions, and answers.
 * All input is validated and reprompted if invalid.
 */
public class InputHandler {
    private final Scanner sc;

    /**
     * Constructs a new InputHandler with a Scanner for reading console input.
     */
    public InputHandler() {
        sc = new Scanner(System.in);
    }

    /**
     * Prompts the user to select a file from a list of options.
     *
     * @param fileOptions a list of available file names
     * @return the file name selected by the user
     */
    public String getFileInput(List<String> fileOptions) {
        System.out.println("Choose a file to load:");

        for (int i = 0; i < fileOptions.size(); i++) {
            System.out.println((i + 1) + ". " + fileOptions.get(i));
        }

        int fileChoice = -1;

        while (true) {
            System.out.print("Enter the number of your choice: ");
            String input = sc.nextLine().trim();

            try {
                fileChoice = Integer.parseInt(input);

                if (fileChoice >= 1 && fileChoice <= fileOptions.size()) {
                    break;
                } 
                else {
                    System.out.println("Invalid choice. Please enter a number from 1 to " + fileOptions.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        String selectedFile = fileOptions.get(fileChoice - 1);
        System.out.println("Extracting data from " + selectedFile);
        return selectedFile;
    }

    /**
     * Returns the appropriate GameLoaderFactory based on the file extension.
     *
     * @param selectedFile the file name
     * @return a GameLoaderFactory instance for the file type
     * @throws IllegalArgumentException if the file format is unknown
     */
    public GameLoaderFactory selectFactory(String selectedFile) {
        GameLoaderFactory factory;

        if (selectedFile.endsWith("csv")) {
            factory = new CSVLoaderFactory();
        }
        else if (selectedFile.endsWith("json")) {
            factory = new JSONLoaderFactory();
        }
        else if (selectedFile.endsWith("xml")) {
            factory = new XMLLoaderFactory();
        }
        else {
            throw new IllegalArgumentException("Unknown format");
        }

        return factory;
    }

    /**
     * Prompts the user to enter the number of players.
     *
     * @return the number of players (1-4)
     */
    public int getPlayerInput() {
        int numPlayers = 0;
        while (true) {
            System.out.print("Enter number of players (1-4): ");
            String input = sc.nextLine().trim();
            
            try {
                numPlayers = Integer.parseInt(input);

                if (numPlayers >= 1 && numPlayers <= 4) {
                    break;
                }
                else {
                    System.out.println("Invalid input. Please enter a number from 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return numPlayers;
    }

    /**
     * Prompts the user to enter a player's name.
     *
     * @param index the player's index in the game
     * @return the entered player name
     */
    public String getPlayerNameInput(int index) {
        String name;

        while (true) {
            System.out.print("Enter name for Player " + index + ": ");
            name = sc.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be empty. Enter name for Player " + index + ": ");
        }
    }

    /**
     * Prompts the user to select a category from a list.
     * Only returns categories with unanswered questions.
     *
     * @param categories the list of categories
     * @return the selected Category or null if "end" is entered
     */
    public Category getCategoryInput(List<Category> categories) {
        System.out.print("Choose a category by number: ");
        String input = sc.nextLine().trim();

        if (input.equalsIgnoreCase("end")) return null;

        int cIndex;
        try {
            cIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return new Category("INVALID");
        }

        if (cIndex >= 0 && cIndex < categories.size()) {
            Category c = categories.get(cIndex);

            boolean hasUnanswered = false;
            for (Question q : c.getQuestions()) {
                if (!q.isAnswered()) {
                    hasUnanswered = true;
                    break;
                }
            }

            if (hasUnanswered) {
                return c;
            } else {
                System.out.println("All questions in that category have been answered. Choose another.");
                return new Category("INVALID");
            }
        
        } else {
            System.out.println("Invalid category number. Try again.");
            return new Category("INVALID");
        }
    }

    /**
     * Prompts the user to select a question from a list.
     * Only returns unanswered questions.
     *
     * @param questions the list of questions
     * @return the selected Question or null if "end" is entered
     */
    public Question getQuestionInput(List<Question> questions) {
        System.out.print("Choose a question by number: ");
        String input = sc.nextLine().trim();
            
        if (input.equalsIgnoreCase("end")) return null;
            
        int qIndex;
        try {
            qIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return new Question("INVALID", 0, null, null);
        }

        if (qIndex >= 0 && qIndex < questions.size() && !questions.get(qIndex).isAnswered()) {
            return questions.get(qIndex);
        } else {
            System.out.println("Invalid choice or question already answered. Try again.");
            return new Question("INVALID", 0, null, null);
        }
    }

    /**
     * Prompts the user to select an answer for a given question.
     *
     * @param q the Question to answer
     * @return the chosen answer letter or null if "end" is entered
     */
    public String getAnswerInput(Question q) {
        while (true) {
            System.out.print("Choose an answer by letter: ");
            String answer = sc.nextLine().trim().toUpperCase();

            if (answer.equalsIgnoreCase("end")) return null;

            if (q.getOptions().containsKey(answer)) {
                return answer;
            } 
            else {
                System.out.println("Invalid input. Please choose one of the available options: " 
                    + q.getOptions().keySet());
            }
        }
    }
}
