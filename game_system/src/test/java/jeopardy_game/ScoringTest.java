package jeopardy_game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringTest {

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
    void playerAddAndSubtractPoints() {
        Player player = new Player(0, "Alice");
        assertEquals(0, player.getScore());

        player.addPoints(150);
        assertEquals(150, player.getScore());

        player.subtractPoints(50);
        assertEquals(100, player.getScore());

        player.subtractPoints(200);
        assertEquals(-100, player.getScore(), "Points can go negative");
    }

    @Test
    void gameScoring_correctAnswer_increasesPlayerScore() throws Exception {
        resetSingleton();
        Game game = Game.getGame();

        Player player = new Player(0, "Bob");
        game.addPlayer(player);
        game.setCurrentPlayer(0);

        // Building GameData with one category and one question
        Category category = new Category("General");
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("A", "int num");
        Question question = new Question("Which of the following declares an integer variable in C++?", 100, optionsMap, "A");
        category.addQuestion(question);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        GameData gameData = new GameData(categories);

        // inject GameData and GameBoard into Game class
        Field dataField = Game.class.getDeclaredField("gameData");
        dataField.setAccessible(true);
        dataField.set(game, gameData);

        Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, new GameBoard(gameData));

        // simulate answering correctly
        Question questionFound = game.getBoard().getQuestion(category, 100);
        assertNotNull(questionFound);
        assertTrue(questionFound.checkAnswer("A"));
        if (questionFound.checkAnswer("A")) {
            game.getCurrentPlayer().addPoints(questionFound.getPoints());
        }
        questionFound.setAnswered(true);

        assertEquals(100, player.getScore());
        assertTrue(questionFound.isAnswered());
    }

    @Test
    void gameScoring_incorrectAnswer_decreasesPlayerScore() throws Exception {
        resetSingleton();
        Game game = Game.getGame();
        Player player = new Player(0, "Carol");
        game.addPlayer(player);
        game.setCurrentPlayer(0);

        Category category = new Category("General");
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("A", "int num");
        optionsMap.put("B", "float num");
        Question question = new Question("Which of the following declares an integer variable in C++?", 200, optionsMap, "A");
        category.addQuestion(question);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        GameData gameData = new GameData(categories);

        Field dataField = Game.class.getDeclaredField("gameData");
        dataField.setAccessible(true);
        dataField.set(game, gameData);

        Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, new GameBoard(gameData));

        // incorrect answer test
        Question questionFound = game.getBoard().getQuestion(category, 200);
        assertNotNull(questionFound);
        assertFalse(questionFound.checkAnswer("B"));
        if (!questionFound.checkAnswer("B")) {
            game.getCurrentPlayer().subtractPoints(questionFound.getPoints());
        }
        questionFound.setAnswered(true);

        assertEquals(-200, player.getScore());
        assertTrue(questionFound.isAnswered());
    }
}
