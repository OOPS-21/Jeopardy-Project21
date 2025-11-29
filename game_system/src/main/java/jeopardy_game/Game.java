package jeopardy_game;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final String reportFile = "game_report.txt";


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

    public String getCaseId() {
        return this.caseId;
    }

    public void addPlayer(Player p) {
        players.add(p);

        this.notifySubscribers(
            new Event.Builder(
                this.getCaseId(),
                "Enter Player Name",
                java.time.Instant.now().toString()
            )
            .result(p.getName() + " added")
            .playerId(p.getName())
            .build()
        );
    }

    public void setCurrentPlayer(int index) {
        this.currentPlayer = index;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameBoard getBoard() {
        return this.board;
    }

    public void start() {
        manager.startSession();
    }

    public void end() {
        manager.endSession();
    }
    
    public void displayScores() {
        System.out.println("Current Scores:");
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getScore() + " points");
        }
    }

    public void generateReport() {
        Logger logger = Logger.getLogger();
        List<Event> events = logger.getEvents();
        if (events.isEmpty()) {
            System.out.println("No events to report.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write("JEOPARDY PROGRAMMING GAME REPORT\n");
            writer.write("================================\n\n");

            writer.write("Case ID: " + events.get(0).getCaseId() + "\n\n");

            writer.write("Players: ");
            String playerList = players.stream()
                    .map(Player::getName)
                    .collect(Collectors.joining(", "));
            writer.write(playerList + "\n\n");

            writer.write("Gameplay Summary:\n-----------------\n");

            Map<String, Category> categoryMap = board.getGameData().getCategories()
                .stream()
                .collect(Collectors.toMap(Category::getName, c -> c));

            int turn = 1;
            for (Event event : events) {
                if (!event.getActivity().equals("Score Updated")) {
                    continue;
                }

                Category cat = categoryMap.get(event.getCategory());
                Question q = null;
                if (cat != null) {
                    q = board.getQuestion(cat, event.getQuestionValue());
                }

                String questionText = q.getQuestionStr();
                String points = event.getQuestionValue().toString();
                String answerLetter = event.getAnswerGiven();
                String answerText = q.getOptions().get(answerLetter);
                String correctness = event.getResult().trim();
                String playerName = event.getPlayerId();

                writer.write("Turn " + turn + ": " + playerName + " selected " +
                        event.getCategory() + " for " + points + " pts\n");
                writer.write("Question: " + questionText + "\n");
                writer.write("Answer: " + answerText +
                        " — " + correctness + " (" + (correctness.equals("Correct") ? "+" : "-") + points + " pts)\n");
                writer.write("Score after turn: " + playerName + " = " + event.getScoreAfterPlay().toString() + "\n\n");

                turn++;
            }

            // Final scores
            writer.write("Final Scores:\n");
            for (Player p : players) {
                writer.write(p.getName() + ": " + p.getScore() + "\n");
            }

            System.out.println("\n>>Game Report generated: " + reportFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

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