package jeopardy_game;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataTest {

    private void initQuestionsList(Category cat) throws NoSuchFieldException, IllegalAccessException {
        Field f = Category.class.getDeclaredField("questions");
        f.setAccessible(true);
        List<Question> list = new ArrayList<>();
        f.set(cat, list);
    }

    @Test
    void constructorAndGetters() throws Exception {
        List<Category> categories = new ArrayList<>();

        Category general = new Category("General");
        initQuestionsList(general);
        Map<String, String> opts = new HashMap<>();
        opts.put("A", "int num");
        Question q = new Question("Which of the following declares an integer variable in C++?", 100, opts, "A");
        general.addQuestion(q);

        categories.add(general);

        GameData data = new GameData(categories);

        assertSame(categories, data.getCategories());
        assertEquals(1, data.getCategories().size());
    }

    @Test
    void getQuestionFoundAndNotFound() throws Exception {
        List<Category> categories = new ArrayList<>();

        Category cat = new Category("General");
        initQuestionsList(cat);

        Map<String, String> opts1 = new HashMap<>();
        opts1.put("A", "int num");
        Question q100 = new Question("Which of the following declares an integer variable in C++?", 100, opts1, "A");

        Map<String, String> opts2 = new HashMap<>();
        opts2.put("A", "char");
        Question q200 = new Question("Which data type is used to store a single character?", 200, opts2, "A");

        cat.addQuestion(q100);
        cat.addQuestion(q200);

        categories.add(cat);
        GameData data = new GameData(categories);

        // Should find existing question
        Question found = data.getQuestion(cat, 100);
        assertSame(q100, found);

        // Non-existing point value returns null
        Question missing = data.getQuestion(cat, 500);
        assertNull(missing);
    }

    @Test
    void addAndRemoveCategory() throws Exception {
        List<Category> categories = new ArrayList<>();
        GameData data = new GameData(categories);

        Category cat = new Category("NewCat");
        initQuestionsList(cat);

        data.addCategory(cat);
        assertTrue(data.getCategories().contains(cat));

        data.removeCategory(cat);
        assertFalse(data.getCategories().contains(cat));
    }

    @Test
    void addQuestionToCategoryAddsWhenPresent() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category cat = new Category("General");
        initQuestionsList(cat);
        categories.add(cat);

        GameData data = new GameData(categories);

        Map<String, String> opts = new HashMap<>();
        opts.put("A", "sample");
        Question q = new Question("Sample Q", 50, opts, "A");

        data.addQuestionToCategory(cat, q);

        assertEquals(1, cat.getQuestions().size());
        assertSame(q, cat.getQuestions().get(0));
    }

    @Test
    void addQuestionToCategoryDoesNothingWhenCategoryNotPresent() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category catInData = new Category("InData");
        initQuestionsList(catInData);
        categories.add(catInData);

        GameData data = new GameData(categories);

        Category other = new Category("Other");
        initQuestionsList(other);

        Map<String, String> opts = new HashMap<>();
        opts.put("A", "sample");
        Question q = new Question("Sample Q", 75, opts, "A");

        data.addQuestionToCategory(other, q);

        // other was not in GameData.categories, so it should not have the question added
        assertEquals(0, other.getQuestions().size());
        // categories in data unchanged
        assertEquals(1, data.getCategories().size());
    }
}
