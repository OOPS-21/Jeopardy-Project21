package jeopardy_game;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private static Game gameInstance;
    private final String caseId;
    private GameData gameData;
    private List<Subscriber> subscribers;
    private List<Player> players;
    private GameBoard board;
    private GameLoaderFactory loaderFactory;
    private int currentPlayer;

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

}