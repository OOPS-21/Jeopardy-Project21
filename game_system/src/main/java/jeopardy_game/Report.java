package jeopardy_game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Generates a Jeopardy-style text report summarising the full gameplay session.
 * 
 * The report includes:
 * - Case ID
 * - Player list
 * - Turn-by-turn breakdown (category, question, answer, correctness, score updates)
 * - Final scores
 *
 * Output is written to "game_report.txt" in the project directory.
 */
public class Report {
    private final String reportFile = "game_report.txt";

    /**
     * Generates a full text-based game report using the final game state and the
     * list of logged events.
     *
     * @param game    the completed game instance containing players and the board
     * @param events  the list of gameplay events recorded by the Logger
     */
    public void generate(Game game, List<Event> events) {
        if (events.isEmpty()) {
            System.out.println("No events to report.");
            return;
        }

        List<Player> players = game.getPlayers();
        GameBoard board = game.getBoard();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write("JEOPARDY PROGRAMMING GAME REPORT\n");
            writer.write("================================\n\n");

            writer.write("Case ID: " + events.get(0).getCaseId() + "\n\n");

            writer.write("Players: ");
            for (int i = 0; i < players.size(); i++) {
                writer.write(players.get(i).getName());
                if (i < players.size() - 1) {
                    writer.write(", ");
                }
            }
            writer.write("\n\n");

            writer.write("Gameplay Summary:\n-----------------\n");

            int turn = 1;
            for (Event event : events) {
                if (!event.getActivity().equals("Score Updated")) continue;
                
                String playerName = event.getPlayerId();
                String categoryName = event.getCategory();
                int points = event.getQuestionValue();

                Category c = board.getCategory(categoryName);
                Question q = board.getQuestion(c, points);

                String questionText = q.getQuestionStr();
                String answerLetter = event.getAnswerGiven();
                String answerText = q.getOptions().get(answerLetter);
                String result = event.getResult();
    

                writer.write("Turn " + turn + ": " + playerName + " selected " 
                        + event.getCategory() + " for " + points + " pts\n");

                writer.write("Question: " + questionText + "\n");

                writer.write("Answer: " + answerText + " — " 
                        + result + " (" + (result.equals("Correct") ? "+" : "-") 
                        + points + " pts)\n");

                writer.write("Score after turn: " + playerName + " = " 
                        + event.getScoreAfterPlay().toString() + "\n\n");

                turn++;
            }

            writer.write("Final Scores:\n");
            for (Player p : players) {
                writer.write(p.getName() + ": " + p.getScore() + "\n");
            }

            System.out.println("\n>> Game Report generated: " + reportFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
