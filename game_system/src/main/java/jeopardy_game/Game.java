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
    }
    
    public static Game getGame() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    public void setLoaderFactory(GameLoaderFactory factory) {
        this.loaderFactory = factory;
    }

    public void loadGame(String filename) {
        GameLoader loader = loaderFactory.createLoader();
        this.gameData = loader.load(filename);
        this.board = new GameBoard(this.gameData);
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

    public void addPlayer(Player player) {
        if (players.size() >= 4) {
            throw new IllegalStateException("Cannot have more than 4 players.");
        }
        players.add(player);
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
        while (!board.allQuestionsAnswered()) {
            board.displayBoard();

            Player player = getCurrentPlayer();
            System.out.println("It's " + player.getName() + "'s turn!");

            Category c = player.selectCategory(board);
            if (c == null) {
                break;
            }
            this.notifySubscribers(
                    new Event.Builder(
                        this.getCaseId(),
                        "Select Category",
                        java.time.Instant.now().toString()
                    )
                    .playerId(String.valueOf(player.getIndex()))
                    .category(c.getName())
                    .build()
            );

            Question q = player.selectQuestion(board, c);
            if (q == null) {
                break;
            }
            this.notifySubscribers(
                    new Event.Builder(
                        this.getCaseId(),
                        "Select Question",
                        java.time.Instant.now().toString()
                    )
                    .playerId(String.valueOf(player.getIndex()))
                    .questionValue(q.getPoints())
                    .questionText(q.getQuestionStr())
                    .category(c.getName())
                    .build()
            );

            q.display();
            String answer = player.getAnswer(q);

            if (answer == null) {
                break;
            }
            else {
                this.notifySubscribers(
                    new Event.Builder(
                        this.getCaseId(),
                        "Answer Question",
                        java.time.Instant.now().toString()
                    )
                    .playerId(String.valueOf(player.getIndex()))
                    .questionValue(q.getPoints())
                    .questionText(q.getQuestionStr())
                    .category(c.getName())
                    .answerGiven(answer)
                    .build()
                );

                if (q.checkAnswer(answer)) {
                    System.out.println("Correct!");
                    player.addPoints(q.getPoints());
                    this.notifySubscribers(
                        new Event.Builder(
                            this.getCaseId(),
                            "Score Updated",
                            java.time.Instant.now().toString()
                        )
                        .playerId(String.valueOf(player.getIndex()))
                        .questionValue(q.getPoints())
                        .category(c.getName())
                        .answerGiven(answer)
                        .result("Correct")
                        .scoreAfterPlay(player.getScore())
                        .build()
                    );
                } else {
                    System.out.println("Wrong! Correct answer: " + q.getCorrectAnswer());
                    player.subtractPoints(q.getPoints());
                    this.notifySubscribers(
                        new Event.Builder(
                            this.getCaseId(),
                            "Score Updated",
                            java.time.Instant.now().toString()
                        )
                        .playerId(String.valueOf(player.getIndex()))
                        .questionValue(q.getPoints())
                        .category(c.getName())
                        .answerGiven(answer)
                        .result("Wrong")
                        .scoreAfterPlay(player.getScore())
                        .build()
                    );
                }
            }
            q.setAnswered(true);

            displayScores();
            System.out.println("\n");

            currentPlayer = (currentPlayer + 1) % players.size();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }    
    }

    public void end() {
        System.out.println("Game Over! Thanks for Playing!");
        displayScores();
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

                String playerName = players.stream()
                        .filter(p -> String.valueOf(p.getIndex()).equals(event.getPlayerId()))
                        .map(Player::getName)
                        .findFirst()
                        .orElse("Unknown");

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

            System.out.println("Game Report generated: " + reportFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}