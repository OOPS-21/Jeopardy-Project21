package jeopardy_game;

/**
 * Manages the gameplay session for a Jeopardy game.
 * Handles turns, question selection, answer evaluation, and score updates.
 */
public class GameManager {
    private Game game;

    /**
     * Constructs a GameManager for a given Game instance.
     * @param game the Game to manage
     */
    public GameManager(Game game) {
        this.game = game;
    }

    /**
     * Starts the gameplay session.
     * Handles player turns, category/question selection, answer evaluation,
     * score updates, and notifies subscribers of game events.
     */
    public void startSession(InputHandler input) {
        System.out.println("\n" +
        "*******************************************\n" +
        "*           WELCOME TO JEOPARDY!          *\n" +
        "*******************************************\n");

        System.out.println("Choosing a random player to start...");
        game.setCurrentPlayer((int)(Math.random() * game.getPlayers().size()));
        pause(2000);

        System.out.println("Player " + game.getCurrentPlayerName() + " goes first!\n");
        
        GameBoard board = game.getBoard();

        while (!board.allQuestionsAnswered()) {
            board.displayBoard();

            Player player = game.getCurrentPlayer();
            System.out.println("It's " + player.getName() + "'s turn!");

            Category c = player.selectCategory(board, input);
            if (c == null) break;

            game.notifySubscribers(
                    new Event.Builder(
                        game.getCaseId(),
                        player.getName(),
                        "Select Category",
                        java.time.Instant.now().toString()
                    )
                    .category(c.getName())
                    .build()
            );

            Question q = player.selectQuestion(board, c, input);
            if (q == null) break;
            
            game.notifySubscribers(
                    new Event.Builder(
                        game.getCaseId(),
                        player.getName(),
                        "Select Question",
                        java.time.Instant.now().toString()
                    )
                    .questionValue(q.getPoints())
                    .questionText(q.getQuestionStr())
                    .category(c.getName())
                    .build()
            );

            q.display();
            String answer = player.getAnswer(q, input);
            if (answer == null) break;
            
            game.notifySubscribers(
                new Event.Builder(
                    game.getCaseId(),
                    player.getName(),
                    "Answer Question",
                    java.time.Instant.now().toString()
                )
                .questionValue(q.getPoints())
                .questionText(q.getQuestionStr())
                .category(c.getName())
                .answerGiven(answer)
                .build()
            );

            if (q.checkAnswer(answer)) {
                System.out.println(">> Correct!");
                sendScoreUpdate(player, c, q, answer, true);
                player.addPoints(q.getPoints());
                
            } else {
                System.out.println(">> Wrong! Correct answer: " + q.getCorrectAnswer());
                player.subtractPoints(q.getPoints());
                sendScoreUpdate(player, c, q, answer, false);
            }
            
            q.setAnswered(true);

            game.displayScores();
            System.out.println("\n");

            int nextIndex = (game.getPlayers().indexOf(player) + 1) % game.getPlayers().size();
            game.setCurrentPlayer(nextIndex);

            pause(2000);
        }  
    }

    /**
     * Ends the gameplay session.
     * Displays final scores and notifies subscribers that the game has ended.
     */
    public void endSession() {
        System.out.println("\n" +
        "*******************************************\n" +
        "*                Game Over!               *\n" +
        "*******************************************\n");
        System.out.println("Thanks for Playing!");
        game.displayScores();

        game.notifySubscribers(
            new Event.Builder(
                game.getCaseId(),
                "System",
                "Exit Game",
                java.time.Instant.now().toString()
            )
            .build()
        );
    }

    /**
     * Pauses the game for a specified number of milliseconds.
     * @param millis the duration to pause in milliseconds
     */
    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a score update event to all subscribers.
     * @param p the player who answered
     * @param c the category of the question
     * @param q the question answered
     * @param ans the answer given by the player
     * @param isCorrect true if the answer was correct, false otherwise
     */
    private void sendScoreUpdate(Player p, Category c, Question q, String ans, boolean isCorrect) {
        String result = isCorrect ? "Correct" : "Wrong";

        game.notifySubscribers(
            new Event.Builder(
                game.getCaseId(),
                p.getName(),
                    "Score Updated",
                    java.time.Instant.now().toString()
            )
            .questionValue(q.getPoints())
            .category(c.getName())
            .answerGiven(ans)
            .result(result)
            .scoreAfterPlay(p.getScore())
            .build()
        );
    }
}
