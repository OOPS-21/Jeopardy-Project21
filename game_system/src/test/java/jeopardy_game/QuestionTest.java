package jeopardy_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    void constructorAndGetters() {
        String[] options = {"int num", "float num", "num int", "integer num"};
        Question q = new Question("Which of the following declares an integer variable in C++?", options, 'A', 100);

        assertEquals("Which of the following declares an integer variable in C++?", q.getQuestionStr());
        assertArrayEquals(options, q.getOptions());
        assertEquals('A', q.getAnswer());
        assertEquals(100, q.getPoints());
        assertFalse(q.isAnswered(), "New questions should not be answered by default");
    }

    @Test
    void settersAndAnsweredFlag() { // test the setters' ability to change the question data.
        String[] options = {"int num", "float num", "num int", "integer num"};
        Question q = new Question("Which of the following declares an integer variable in C++?", options, 'A', 100);

        q.setQuestionStr("Which data type is used to store a single character?");
        q.setOptions(new String[]{"string","char","bool","text"});
        q.setAnswer('B');
        q.setPoints(200);
        q.setAnswered(true);

        assertEquals("Which data type is used to store a single character?", q.getQuestionStr());
        assertArrayEquals(new String[]{"string","char","bool","text"}, q.getOptions());
        assertEquals('B', q.getAnswer());
        assertEquals(200, q.getPoints());
        assertTrue(q.isAnswered());
    }

    @Test
    void optionsArrayIsMutableThroughReference() {
        String[] options = {"First", "Second"};
        Question q = new Question("Mutable?", options, 'A', 10);

        // Modify the original array reference after construction
        options[0] = "Changed";

        // The Question stores the same array reference, so the change should be visible
        assertEquals("Changed", q.getOptions()[0]);
    }

    @Test
    void constructorAcceptsNullsAndBehavesPredictably() {
        // question string null
        Question q1 = new Question(null, new String[]{"A"}, 'A', 0);
        assertNull(q1.getQuestionStr());
        assertArrayEquals(new String[]{"A"}, q1.getOptions());

        // options null
        Question q2 = new Question("Q", null, 'B', 10);
        assertNull(q2.getOptions());
        // accessing the options array should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> {
            String s = q2.getOptions()[0];
        });

        // answer and points can be any values (no validation in class)
        Question q3 = new Question("Q3", new String[]{"X"}, '\0', -5);
        assertEquals('\0', q3.getAnswer());
        assertEquals(-5, q3.getPoints());
    }

    @Test
    void settersHandleNullAndNegativePoints() {
        Question q = new Question("Q", new String[]{"A","B"}, 'A', 100);

        // setOptions to null
        q.setOptions(null);
        assertNull(q.getOptions());

        // setQuestionStr to null
        q.setQuestionStr(null);
        assertNull(q.getQuestionStr());

        // set negative points
        q.setPoints(-999);
        assertEquals(-999, q.getPoints());
    }
}
