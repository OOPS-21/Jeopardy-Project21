package jeopardy_game;

public class GameManager {
    private Game game;

    public GameManager(Game game) {
        this.game = game;
    }

    public void startSession() {
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

            Category c = player.selectCategory(board);
            if (c == null) break;

            game.notifySubscribers(
                    new Event.Builder(
                        game.getCaseId(),
                        "Select Category",
                        java.time.Instant.now().toString()
                    )
                    .playerId(player.getName())
                    .category(c.getName())
                    .build()
            );

            Question q = player.selectQuestion(board, c);
            if (q == null) break;
            
            game.notifySubscribers(
                    new Event.Builder(
                        game.getCaseId(),
                        "Select Question",
                        java.time.Instant.now().toString()
                    )
                    .playerId(player.getName())
                    .questionValue(q.getPoints())
                    .questionText(q.getQuestionStr())
                    .category(c.getName())
                    .build()
            );

            q.display();
            String answer = player.getAnswer(q);
            if (answer == null) break;
            
            game.notifySubscribers(
                new Event.Builder(
                    game.getCaseId(),
                    "Answer Question",
                    java.time.Instant.now().toString()
                )
                .playerId(player.getName())
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
                "Exit Game",
                java.time.Instant.now().toString()
            )
            .playerId("System")
            .build()
        );
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendScoreUpdate(Player p, Category c, Question q, String ans, boolean isCorrect) {
        String result = isCorrect ? "Correct" : "Wrong";

        game.notifySubscribers(
            new Event.Builder(
                game.getCaseId(),
                    "Score Updated",
                    java.time.Instant.now().toString()
            )
            .playerId(p.getName())
            .questionValue(q.getPoints())
            .category(c.getName())
            .answerGiven(ans)
            .result(result)
            .scoreAfterPlay(p.getScore())
            .build()
        );
    }
}
