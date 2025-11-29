package jeopardy_game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    //Singleton components
    private Game() {
        this.caseId = UUID.randomUUID().toString();
        this.subscribers =  new ArrayList<>();
        this.players =  new ArrayList<>();
        this.gameData = null;
        this.manager = new GameManager(this);
    }
    
    public static Game getGame() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    //Setup Methods
    public void startUp() {
        notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Start Game",
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );
    }

    public void setLoaderFactory(GameLoaderFactory factory) {
        this.loaderFactory = factory;
    }

    public void loadGame(String filename) {
        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Load File",
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );

        GameLoader loader = loaderFactory.createLoader();
        this.gameData = loader.load(filename);
        this.board = new GameBoard(this.gameData);

        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "File Loaded Successfully", 
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );
    }

    public void setPlayerCount(int numPlayers) {
        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Select Player Count",
                java.time.Instant.now().toString()
            )
            .result(numPlayers + " selected")
            .playerId("System")
            .build()
        );   
    }

    public String getCaseId() {
        return this.caseId;
    }

    //Observer components
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(Event event) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(event);
        }
    }

    //Player & Board methods
    public void addPlayer(Player p) {
        players.add(p);

        notifySubscribers(
            new Event.Builder(
                caseId,
                "Enter Player Name",
                java.time.Instant.now().toString()
            )
            .result(p.getName() + " added")
            .playerId(p.getName())
            .build()
        );
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    public void setCurrentPlayer(int index) {
        this.currentPlayer = index;
    }

    public GameBoard getBoard() {
        return this.board;
    }

    //Session methods
    public void start() {
        manager.startSession();
    }

    public void end() {
        manager.endSession();
    }
    
    //Output and Logging
    public void displayScores() {
        System.out.println("Current Scores:");
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getScore() + " points");
        }
    }

    public void generateReport() {
        List<Event> events = Logger.getLogger().getEvents();
        new Report().generate(this, events);
        
        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Generate Report",
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );
    }

    public void generateEventLogs() {
        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Generate Event Log",
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );
        
        Logger.getLogger().generateEventLogs();
    }
}