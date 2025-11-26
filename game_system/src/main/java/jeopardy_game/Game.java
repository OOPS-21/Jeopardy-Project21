package jeopardy_game;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game gameInstance;
    private GameData gameData;
    private List<Subscriber> subscribers;
    private List<Player> players;
    private GameBoard board;
    private GameLoaderFactory loaderFactory;
    private int currentPlayer;

    private Game() {
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

    public void notifySubscribers(String log) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(log);
        }
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

            // 1. Ask player to select category & value
            Question q = player.selectQuestion(board); // can implement selection in Player or via CLI input

            // 2. Show the question and options
            q.display(); // maybe prints question & options
            String answer = player.getAnswer(q); // input via Scanner

            // 3. Check if correct and update score
            if (q.checkAnswer(answer)) {
                System.out.println("Correct!");
                player.addPoints(q.getPoints());
            } else {
                System.out.println("Wrong! Correct answer: " + q.getCorrectAnswer());
                player.subtractPoints(q.getPoints());
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

        // Game over
        System.out.println("All questions answered! Final scores:");
        displayScores();
    }
    
    public void displayScores() {
        System.out.println("Current Scores:");
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getScore() + " points");
        }
    }

}