package jeopardy_game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // Helper to reset singleton between tests
    private void resetSingleton() throws Exception {
        Field f = Game.class.getDeclaredField("gameInstance");
        f.setAccessible(true);
        f.set(null, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        resetSingleton();
    }

    @Test
    void singletonReturnsSameInstance() throws Exception {
        resetSingleton();
        Game g1 = Game.getGame();
        Game g2 = Game.getGame();
        assertSame(g1, g2);
    }

    @Test
    void addPlayerLimitEnforced() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        game.addPlayer(new Player(0, "P1"));
        game.addPlayer(new Player(1, "P2"));
        game.addPlayer(new Player(2, "P3"));
        game.addPlayer(new Player(3, "P4"));
        assertThrows(IllegalStateException.class, () -> game.addPlayer(new Player(4, "P5")));
    }

    @Test
    void subscribeNotifyCallsSubscriber() throws Exception {
        resetSingleton();
        Game game = Game.getGame();

        final StringBuilder received = new StringBuilder();
        Subscriber s = new Subscriber() {
            @Override
            public void update(String log) {
                received.append(log);
            }
        };

        game.subscribe(s);
        game.notifySubscribers("hello");
        assertEquals("hello", received.toString());

        game.unsubscribe(s);
        received.setLength(0);
        game.notifySubscribers("x");
        assertEquals("", received.toString());
    }

    @Test
    void setAndGetCurrentPlayerWorks() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        game.addPlayer(new Player(0, "Alice"));
        game.addPlayer(new Player(1, "Bob"));

        game.setCurrentPlayer(1);
        assertEquals("Bob", game.getCurrentPlayerName());
        assertEquals("Bob", game.getCurrentPlayer().getName());
    }

    @Test
    void loadGameUsesFactoryAndSetsBoard() throws Exception {
        resetSingleton();
        Game game = Game.getGame();

        // Prepare fake loader that returns GameData with one category
        GameLoader fakeLoader = new GameLoader() {
            @Override
            public GameData load(String filename) {
                Category cat = new Category("G");
                Map<String, String> opts = new HashMap<>();
                opts.put("A", "a");
                Question q = new Question("Q", 100, opts, "A");
                cat.addQuestion(q);
                List<Category> cats = new ArrayList<>();
                cats.add(cat);
                return new GameData(cats);
            }
        };

        GameLoaderFactory factory = new GameLoaderFactory() {
            @Override
            public GameLoader createLoader() {
                return fakeLoader;
            }
        };

        game.setLoaderFactory(factory);
        game.loadGame("unused");

        assertNotNull(game.getBoard());
        assertEquals(1, game.getBoard().getGameData().getCategories().size());
    }

    @Test
    void scoringFlowSimulatedCorrectAndIncorrect() throws Exception {
        resetSingleton();
        Game game = Game.getGame();

        Player p1 = new Player(0, "Alice");
        Player p2 = new Player(1, "Bob");
        game.addPlayer(p1);
        game.addPlayer(p2);

        // Build GameData and set into game
        Category cat = new Category("General");
        Map<String, String> opts = new HashMap<>();
        opts.put("A", "int num");
        opts.put("B", "float num");
        Question q = new Question("Which of the following declares an integer variable in C++?", 100, opts, "A");
        cat.addQuestion(q);
        List<Category> cats = new ArrayList<>();
        cats.add(cat);

        GameData data = new GameData(cats);

        // inject GameData into Game instance via reflection
        Field dataField = Game.class.getDeclaredField("gameData");
        dataField.setAccessible(true);
        dataField.set(game, data);

        // also set board
        Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, new GameBoard(data));

        // Set current player to Alice
        game.setCurrentPlayer(0);

        // Simulate correct answer for Alice
        Question found = game.getBoard().getQuestion(cat, 100);
        assertNotNull(found);
        assertTrue(found.checkAnswer("A"));
        if (found.checkAnswer("A")) {
            game.getCurrentPlayer().addPoints(found.getPoints());
        } else {
            game.getCurrentPlayer().subtractPoints(found.getPoints());
        }
        found.setAnswered(true);

        assertEquals(100, p1.getScore());
        assertTrue(found.isAnswered());

        // Next player's incorrect answer
        game.setCurrentPlayer(1);
        Question found2 = game.getBoard().getQuestion(cat, 100);
        // question already answered; simulate Bob answers incorrectly on same question (score should subtract)
        if (found2 != null) {
            if (found2.checkAnswer("B")) {
                game.getCurrentPlayer().addPoints(found2.getPoints());
            } else {
                game.getCurrentPlayer().subtractPoints(found2.getPoints());
            }
        }
        assertEquals(-100, p2.getScore());
    }
}
