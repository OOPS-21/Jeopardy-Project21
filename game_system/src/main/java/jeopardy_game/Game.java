package jeopardy_game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents the main Jeopardy game.
 * Implements a singleton pattern to ensure only one game instance exists.
 * Handles game setup, player management, session management, observers, 
 * and report/log generation.
 */
public class Game {
    private static Game gameInstance;
    private GameManager manager;
    private final String caseId;
    private GameData gameData;
    private List<Subscriber> subscribers;
    private List<Player> players;
    private GameBoard board;
    private GameLoaderFactory loaderFactory;
    private int currentPlayer;

    /**
     * Private constructor for singleton pattern.
     * Initializes subscriber and player lists and the GameManager.
     */
    private Game() {
        this.caseId = UUID.randomUUID().toString();
        this.subscribers =  new ArrayList<>();
        this.players =  new ArrayList<>();
        this.gameData = null;
        this.manager = new GameManager(this);
    }
    
    /**
     * Returns the single instance of the Game.
     * @return the singleton Game instance
     */
    public static Game getGame() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    //Setup Methods

    /**
     * Initialises the game and notifies subscribers that the game has started.
     */
    public void startUp() {
        notifySubscribers(
            new Event.Builder(
                caseId,
                "System",
                "Start Game",
                java.time.Instant.now().toString()
            )
            .build()
        );
    }

    /**
     * Sets the factory used to load game data.
     * @param factory the GameLoaderFactory to use
     */
    public void setLoaderFactory(GameLoaderFactory factory) {
        this.loaderFactory = factory;
    }

    /**
     * Loads the game data from a file and initialises the game board.
     * Notifies subscribers before and after loading.
     * @param filename the name of the game file to load
     */
    public void loadGame(String filename) {
        notifySubscribers(
            new Event.Builder(
                caseId,
                "Load File",
                "System",
                java.time.Instant.now().toString()
            )
            .build()
        );

        GameLoader loader = loaderFactory.createLoader();
        this.gameData = loader.load(filename);
        this.board = new GameBoard(this.gameData);

        notifySubscribers(
            new Event.Builder(
                caseId,
                "System",
                "File Loaded Successfully", 
                java.time.Instant.now().toString()
            )
            .build()
        );
    }

    /**
     * Notifies subscribers of the number of players selected.
     * @param numPlayers the number of players chosen
     */
    public void setPlayerCount(int numPlayers) {
        notifySubscribers(
            new Event.Builder(
                caseId,
                "System",
                "Select Player Count",
                java.time.Instant.now().toString()
            )
            .result(numPlayers + " selected")
            .build()
        );   
    }

    /**
     * Returns the unique identifier for this game session.
     * @return the game case ID
     */
    public String getCaseId() {
        return this.caseId;
    }

    //Observer methods

    /**
     * Subscribes a new observer to the game events.
     * @param subscriber the subscriber to add
     */
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribes an observer from the game events.
     * @param subscriber the subscriber to remove
     */
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribers of a game event.
     * @param event the event to send
     */
    public void notifySubscribers(Event event) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(event);
        }
    }

    //Player & Board methods

    /**
     * Adds a player to the game and notifies subscribers.
     * @param p the player to add
     */
    public void addPlayer(Player p) {
        players.add(p);

        notifySubscribers(
            new Event.Builder(
                caseId,
                p.getName(),
                "Enter Player Name",
                java.time.Instant.now().toString()
            )
            .result(p.getName() + " added")
            .build()
        );
    }

    /**
     * Returns the list of players in the game.
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the current player whose turn it is.
     * @return the current Player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    /**
     * Returns the name of the current player.
     * @return the current player's name
     */
    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    /**
     * Sets the index of the current player.
     * @param index the player index
     */
    public void setCurrentPlayer(int index) {
        this.currentPlayer = index;
    }

    /**
     * Returns the game board.
     * @return the GameBoard
     */
    public GameBoard getBoard() {
        return this.board;
    }

    //Session methods

    /**
     * Starts the game session using GameManager.
     */
    public void start(InputHandler input) {
        manager.startSession(input);
    }

    /**
     * Ends the game session using GameManager.
     */
    public void end() {
        manager.endSession();
    }
    
    //Output and Logging

    /**
     * Displays the current scores of all players.
     */
    public void displayScores() {
        System.out.println("Current Scores:");
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getScore() + " points");
        }
    }

    /**
     * Generates a gameplay report using the Report class and notifies subscribers.
     */
    public void generateReport() {
        List<Event> events = Logger.getLogger().getEvents();
        new Report().generate(this, events);
        
        notifySubscribers(
            new Event.Builder(
                caseId,
                "System",
                "Generate Report",
                java.time.Instant.now().toString()
            )
            .build()
        );
    }

    /**
     * Generates the event log for the game and notifies subscribers.
     */
    public void generateEventLogs() {
        notifySubscribers(
            new Event.Builder(
                caseId,
                "System",
                "Generate Event Log",
                java.time.Instant.now().toString()
            )
            .build()
        );
        
        Logger.getLogger().generateEventLogs();
    }
}