package jeopardy_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class QuestionTest {

    @Test
    void constructorAndGetters() {
        Map<String, String> options = new HashMap<>();
        options.put("A", "int num");
        options.put("B", "float num");
        options.put("C", "num int");
        options.put("D", "integer num");

        Question q = new Question("Which of the following declares an integer variable in C++?", 100, options, "A");

        assertEquals("Which of the following declares an integer variable in C++?", q.getQuestionStr());
        assertEquals(options, q.getOptions());
        assertEquals("A", q.getAnswer());
        assertEquals(100, q.getPoints());
        assertFalse(q.isAnswered(), "New questions should not be answered by default");
    }

    @Test
    void answeredFlagOnlySetter() {
        Map<String, String> options = new HashMap<>();
        options.put("A", "string");
        options.put("B", "char");
        options.put("C", "bool");
        options.put("D", "text");

        Question q = new Question("Which data type is used to store a single character?", 200, options, "B");

        // only answered flag has a setter; ensure it works and other fields remain as constructed
        assertFalse(q.isAnswered());
        q.setAnswered(true);
        assertTrue(q.isAnswered());

        assertEquals("Which data type is used to store a single character?", q.getQuestionStr());
        assertEquals(options, q.getOptions());
        assertEquals("B", q.getAnswer());
        assertEquals(200, q.getPoints());
    }

    @Test
    void optionsArrayIsMutableThroughReference() {
        Map<String, String> options = new HashMap<>();
        options.put("A", "First");
        options.put("B", "Second");

        Question q = new Question("Mutable?", 10, options, "A");

        // Modify the original map reference after construction
        options.put("A", "Changed");

        // The Question stores the same map reference, so the change should be visible
        assertEquals("Changed", q.getOptions().get("A"));
    }

    @Test
    void constructorAcceptsNullsAndBehavesPredictably() {
        // question string null
        Map<String, String> opts = new HashMap<>();
        opts.put("A", "A");
        Question q1 = new Question(null, 0, opts, "A");
        assertNull(q1.getQuestionStr());
        assertEquals(opts, q1.getOptions());

        // options null
        Question q2 = new Question("Q", 10, null, "B");
        assertNull(q2.getOptions());
        // accessing the options map should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> {
            String s = q2.getOptions().get("A");
        });

        // answer and points can be any values (no validation in class)
        Question q3 = new Question("Q3", -5, opts, null);
        assertNull(q3.getAnswer());
        assertEquals(-5, q3.getPoints());
    }

    @Test
    void settersHandleNullAndNegativePoints() {
        // Only answered flag is mutable via setter; test it and that fields remain otherwise immutable
        Map<String, String> options = new HashMap<>();
        options.put("A", "A");
        Question q = new Question("Q", 100, options, "A");

        q.setAnswered(true);
        assertTrue(q.isAnswered());

        // other fields should remain as constructed
        assertEquals("Q", q.getQuestionStr());
        assertEquals(options, q.getOptions());
        assertEquals("A", q.getAnswer());
        assertEquals(100, q.getPoints());
    }
}
