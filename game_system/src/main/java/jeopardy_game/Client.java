package jeopardy_game;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        Game game = Game.getGame();
        GameLoaderFactory factory;

        List<String> fileOptions = Arrays.asList(
            "sample_game_CSV.csv",
            "sample_game_JSON.json",
            "sample_game_XML.xml"
        );

        System.out.println("Choose a file to load:");
        for (int i = 0; i < fileOptions.size(); i++) {
            System.out.println((i + 1) + ". " + fileOptions.get(i));
        }

        Scanner sc = new Scanner(System.in);
        int fileChoice = -1;

        while (true) {
            System.out.print("Enter the number of your choice: ");
            String input = sc.nextLine();

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

        game.setLoaderFactory(factory);
        game.loadGame(selectedFile);

        int numPlayers = 0;
        while (true) {
            System.out.print("Enter number of players (1-4): ");

            try {
                numPlayers = sc.nextInt();
                sc.nextLine();

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

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            String name = sc.nextLine().trim();

            while (name.isEmpty()) {
                System.out.print("Name cannot be empty. Enter name for Player " + (i + 1) + ": ");
                name = sc.nextLine().trim();
            }

            Player p = new Player(i + 1, name);
            game.addPlayer(p); 
        }

        System.out.println("Welcome to the game of Jeopardy!");
        System.out.println("Choosing a random player to start...");

        game.setCurrentPlayer((int)(Math.random() * numPlayers));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Player " + game.getCurrentPlayerName() + " goes first!");

        game.start();
    }
}