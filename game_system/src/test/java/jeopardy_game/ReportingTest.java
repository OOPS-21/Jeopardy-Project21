package jeopardy_game;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ReportingTest {
    private void resetGameSingleton() throws Exception {
        Field f = Game.class.getDeclaredField("gameInstance");
        f.setAccessible(true);
        f.set(null, null);
    }

    private void resetLoggerSingleton() throws Exception {
        Field f = Logger.class.getDeclaredField("loggerinstance");
        f.setAccessible(true);
        f.set(null, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        resetGameSingleton();
        resetLoggerSingleton();

        new File("game_report.txt").delete();
        new File("game_log.csv").delete();
    }

    @Test
    void testReportFileCreated() throws Exception {
        resetGameSingleton();
        resetLoggerSingleton();

        Game game = Game.getGame();
        Player p = new Player(0, "Alice");
        game.addPlayer(p);

        Map<String, String> options = new HashMap<>();
        options.put("A", "Option A");
        options.put("B", "Option B");
        Question q = new Question("Sample Q", 100, options, "A");
        Category cat = new Category("General");
        cat.addQuestion(q);
        GameBoard board = new GameBoard(new GameData(List.of(cat)));

        Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);

        Logger logger = Logger.getLogger();
        Event e = new Event.Builder("CASE123", "Alice", "Score Updated", "2025-11-30T23:00:00Z")
                .category("General")
                .questionValue(100)
                .answerGiven("A")
                .result("Correct")
                .scoreAfterPlay(100)
                .build();
        logger.update(e);

        game.generateReport();

        File report = new File("game_report.txt");
        assertTrue(report.exists(), "Report file should be created");
    }

    @Test
    void testReportContainsPlayerScore() throws Exception {
        resetGameSingleton();
        resetLoggerSingleton();

        Game game = Game.getGame();
        Player p = new Player(0, "Alice");
        game.addPlayer(p);

        Map<String, String> options = new HashMap<>();
        options.put("A", "Option A");
        options.put("B", "Option B");
        Question q = new Question("Sample Q", 100, options, "A");
        Category cat = new Category("General");
        cat.addQuestion(q);
        GameBoard board = new GameBoard(new GameData(List.of(cat)));

        Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);

        Logger logger = Logger.getLogger();
        Event e = new Event.Builder("CASE123", "Alice", "Score Updated", "2025-11-30T23:00:00Z")
                .category("General")
                .questionValue(100)
                .answerGiven("A")
                .result("Correct")
                .scoreAfterPlay(100)
                .build();
        logger.update(e);

        game.generateReport();

        String content = Files.readString(Path.of("game_report.txt"));
        assertTrue(content.contains("Alice"), "Report should mention the player");
        assertTrue(content.contains("100"), "Report should include the player's score");
    }
}
