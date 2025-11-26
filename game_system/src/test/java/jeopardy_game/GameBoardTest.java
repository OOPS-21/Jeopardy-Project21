package jeopardy_game;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private void initQuestionsList(Category cat) throws NoSuchFieldException, IllegalAccessException {
        Field f = Category.class.getDeclaredField("questions");
        f.setAccessible(true);
        List<Question> list = new ArrayList<>();
        f.set(cat, list);
    }

    @Test
    void getQuestionDelegatesToGameData() throws Exception {
        // set up category with questions
        Category cat = new Category("General");
        initQuestionsList(cat);

        Map<String, String> opts = new HashMap<>();
        opts.put("A", "int num");
        Question q = new Question("Which of the following declares an integer variable in C++?", 100, opts, "A");
        cat.addQuestion(q);

        List<Category> cats = new ArrayList<>();
        cats.add(cat);
        GameData gd = new GameData(cats);

        GameBoard board = new GameBoard(gd);

        Question found = board.getQuestion(cat, 100);
        assertSame(q, found);
    }

    @Test
    void markQuestionSetsAnswered() throws Exception {
        Category cat = new Category("General");
        initQuestionsList(cat);

        Map<String, String> opts = new HashMap<>();
        opts.put("A", "char");
        Question q = new Question("Which data type is used to store a single character?", 200, opts, "A");
        cat.addQuestion(q);

        List<Category> cats = new ArrayList<>();
        cats.add(cat);
        GameData gd = new GameData(cats);
        GameBoard board = new GameBoard(gd);

        assertFalse(q.isAnswered());
        board.markQuestion(cat, 200);
        assertTrue(q.isAnswered());
    }

    @Test
    void allQuestionsAnswered_behaviorDocumented() throws Exception {
        // Note: current implementation of allQuestionsAnswered is inverted (returns true when none answered,
        // and false when questions are answered). This test documents the current behavior.
        Category cat = new Category("General");
        initQuestionsList(cat);

        Map<String, String> opts1 = new HashMap<>();
        opts1.put("A", "int num");
        Question q1 = new Question("Which of the following declares an integer variable in C++?", 100, opts1, "A");

        Map<String, String> opts2 = new HashMap<>();
        opts2.put("A", "char");
        Question q2 = new Question("Which data type is used to store a single character?", 200, opts2, "A");

        cat.addQuestion(q1);
        cat.addQuestion(q2);

        List<Category> cats = new ArrayList<>();
        cats.add(cat);
        GameData gd = new GameData(cats);
        GameBoard board = new GameBoard(gd);

        // Initially none answered -> should return false
        assertFalse(board.allQuestionsAnswered(), "No questions answered -> allQuestionsAnswered should be false");

        // Mark both questions answered -> should return true
        q1.setAnswered(true);
        q2.setAnswered(true);
        assertTrue(board.allQuestionsAnswered(), "All questions answered -> allQuestionsAnswered should be true");
    }

    @Test
    void displayBoardDoesNotThrowWhenCategoriesNull() {
        // Create GameData with null categories reference to ensure displayBoard handles exceptions gracefully
        GameData gd = new GameData(null);
        GameBoard board = new GameBoard(gd);

        // Should not throw (method catches exceptions internally)
        board.displayBoard();
    }
}
