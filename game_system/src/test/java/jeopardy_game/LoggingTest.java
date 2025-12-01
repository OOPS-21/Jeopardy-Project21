package jeopardy_game;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

public class LoggingTest {

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
    void testEventLogging() {
        Logger logger = Logger.getLogger();
        Game game = Game.getGame();
        game.subscribe(logger);
        game.notifySubscribers(new Event.Builder("case1", "Alice", "Answered", "timestamp").build());
        assertFalse(logger.getEvents().isEmpty(), "Logger should record the event");
    }
}
