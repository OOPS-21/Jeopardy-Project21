package jeopardy_game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayTest {

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
    void testSingletonReturnsSameInstance() throws Exception {
        resetSingleton();
        Game g1 = Game.getGame();
        Game g2 = Game.getGame();
        assertSame(g1, g2);
    }

    @Test
    void testPlayerLimitEnforced() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        game.addPlayer(new Player(0, "P1"));
        game.addPlayer(new Player(1, "P2"));
        game.addPlayer(new Player(2, "P3"));
        game.addPlayer(new Player(3, "P4"));
        assertThrows(IllegalStateException.class, () -> game.addPlayer(new Player(4, "P5")));
    }

    @Test
    void testCannotStartGameWithNoPlayers() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        InputHandler input = new InputHandler();
        assertThrows(IllegalStateException.class, () -> game.start(input));
    }

    @Test
    void testSetAndGetCurrentPlayer() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        game.addPlayer(new Player(0, "Alice"));
        game.addPlayer(new Player(1, "Bob"));

        game.setCurrentPlayer(1);
        assertEquals("Bob", game.getCurrentPlayerName());
        assertEquals("Bob", game.getCurrentPlayer().getName());
    }

    @Test
    void testSubscribeNotifyCallsSubscriber() throws Exception {
        resetSingleton();
        Game game = Game.getGame();

        StringBuilder received = new StringBuilder();

        Subscriber s = new Subscriber() {
            @Override
            public void update(Event event) {
                received.append(event.getActivity());
            }
        };

        game.subscribe(s);

        Event testEvent = new Event.Builder(
            "T120",
            "System",
            "Testing",
            java.time.Instant.now().toString()
        ).build();

        game.notifySubscribers(testEvent);
        assertEquals("Testing", received.toString());

        game.unsubscribe(s);
        received.setLength(0);
        game.notifySubscribers(testEvent);
        assertEquals("", received.toString()); 
    }

    @Test
    void testLoadGameUsesFactoryAndSetsBoard() throws Exception {
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
    void testAnsweredQuestionCannotBeReused() {
        Map<String, String> opts1 = new HashMap<>();
        opts1.put("A", "Option A");
        opts1.put("B", "Option B");

        Map<String, String> opts2 = new HashMap<>();
        opts2.put("A", "Option A");
        opts2.put("B", "Option B");

        Question q1 = new Question("Q1", 100, opts1, "A");
        Question q2 = new Question("Q2", 200, opts2, "B");

        q1.setAnswered(true);

        Category c = new Category("Test");
        c.addQuestion(q1);
        c.addQuestion(q2);

        long count = c.getQuestions().stream().filter(q -> !q.isAnswered()).count();
        assertEquals(1, count, "Only one question should be selectable");
    }
}
