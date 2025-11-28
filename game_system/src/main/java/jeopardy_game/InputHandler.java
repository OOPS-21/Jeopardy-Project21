package jeopardy_game;

import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private Scanner sc;

    public InputHandler() {
        sc = new Scanner(System.in);
    }

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

}
