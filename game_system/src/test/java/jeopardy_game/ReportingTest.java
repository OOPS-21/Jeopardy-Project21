package jeopardy_game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void loggerIsSingleton() {
        Logger l1 = Logger.getLogger();
        Logger l2 = Logger.getLogger();
        assertSame(l1, l2);
    }

    @Test
    void loggerUpdatePrintsLogMessage() {
        Logger logger = Logger.getLogger();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            logger.update("Test message");
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertEquals("LOG: Test message", output);
    }

    @Test
    void gameNotifySubscribersUsingLoggerOutputsLog() throws Exception {
        resetGameSingleton();
        Game game = Game.getGame();

        // subscribe the singleton logger
        Logger logger = Logger.getLogger();
        game.subscribe(logger);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            game.notifySubscribers("Game started");
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertEquals("LOG: Game started", output);
    }


    // Need to add tests to confirm that the logger is updating the CSV file correctly

}
