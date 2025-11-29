package jeopardy_game;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point for the Jeopardy game application.
 * 
 * Handles:
 *  - Initialising the game
 *  - Selecting and loading the question file
 *  - Setting up players
 *  - Starting and ending the game session
 *  - Generating game report and event logs
 */
public class Client {
    /**
     * Main method to run the Jeopardy game.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        InputHandler input = new InputHandler();

        Game game = Game.getGame();
        game.subscribe(Logger.getLogger());
        game.startUp();

        //File selection
        List<String> fileOptions = Arrays.asList(
                    "sample_game_CSV.csv",
                    "sample_game_JSON.json",
                    "sample_game_XML.xml"
                );
        String selectedFile = input.getFileInput(fileOptions);
        
        //Factory selection
        GameLoaderFactory factory = input.selectFactory(selectedFile);
        game.setLoaderFactory(factory);
        game.loadGame(selectedFile);

        //Player setup
        int numPlayers = input.getPlayerInput();
        game.setPlayerCount(numPlayers);
        for (int i = 1; i <= numPlayers; i++) {
            String name = input.getPlayerNameInput(i);
            Player p = new Player(i, name);
            game.addPlayer(p);
        }

        //Game loop
        game.start();
        game.end();

        //Game Log and Report generation
        game.generateReport();
        game.generateEventLogs();
    }
}