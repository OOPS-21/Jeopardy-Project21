package jeopardy_game;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void constructorAndGetters() {
        Category cat = new Category("General");

        assertEquals("General", cat.getName());
        // questions list is not initialized by the constructor in the current implementation
        assertNull(cat.getQuestions(), "questions should be null until initialized");
    }

    @Test
    void addQuestionWithoutInitializationThrowsNPE() {
        Category cat = new Category("NullCat");
        Map<String, String> opts = new HashMap<>();
        opts.put("A", "A");
        assertThrows(NullPointerException.class, () -> cat.addQuestion(new Question("X", 1, opts, "A")));
    }

    @Test
    void initializeQuestionsViaReflectionAndAddQuestion() {
        Category cat = new Category("OldName");

        try {
            Field f = Category.class.getDeclaredField("questions");
            f.setAccessible(true);
            List<Question> list = new ArrayList<>();
            f.set(cat, list);

            Map<String, String> opts = new HashMap<>();
            opts.put("A", "X");
            Question q = new Question("NewQ", 50, opts, "A");
            cat.addQuestion(q);
            assertEquals(1, cat.getQuestions().size());
            assertSame(q, cat.getQuestions().get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to initialize questions: " + e.getMessage());
        }
    }

    @Test
    void getQuestionsIsMutableThroughReference() {
        Category cat = new Category("T");
        List<Question> original = new ArrayList<>();

        try {
            Field f = Category.class.getDeclaredField("questions");
            f.setAccessible(true);
            f.set(cat, original);

            Map<String, String> m1 = new HashMap<>();
            m1.put("A", "L");
            Question q = new Question("Later", 10, m1, "A");
            original.add(q);

            assertEquals(1, cat.getQuestions().size());
            assertSame(q, cat.getQuestions().get(0));

            Map<String, String> m2 = new HashMap<>();
            m2.put("A", "V");
            Question q2 = new Question("ViaGet", 20, m2, "A");
            cat.getQuestions().add(q2);
            assertEquals(2, original.size());
            assertSame(q2, original.get(1));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to initialize questions: " + e.getMessage());
        }
    }

    @Test
    void addNullQuestionAllowedForArrayList() {
        Category cat = new Category("NullAllowed");

        try {
            Field f = Category.class.getDeclaredField("questions");
            f.setAccessible(true);
            List<Question> list = new ArrayList<>();
            f.set(cat, list);

            cat.addQuestion(null);
            assertEquals(1, cat.getQuestions().size());
            assertNull(cat.getQuestions().get(0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to initialize questions: " + e.getMessage());
        }
    }

    @Test
    void addQuestionOnUnmodifiableListThrows() {
        List<Question> unmod = List.of(); // immutable list
        Category cat = new Category("Immutable");

        try {
            Field f = Category.class.getDeclaredField("questions");
            f.setAccessible(true);
            f.set(cat, unmod);

            Map<String, String> opts = new HashMap<>();
            opts.put("A", "Z");
            assertThrows(UnsupportedOperationException.class, () -> cat.addQuestion(new Question("Y", 5, opts, "A")));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to initialize questions: " + e.getMessage());
        }
    }
}
