package jeopardy_game;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class ParsingTest {

    @Test
    void csvLoader_resourceNotFound_throws() {
        CSVLoader loader = new CSVLoader();
        assertThrows(RuntimeException.class, () -> loader.load("nonexistent_file.csv"));    
    }

    @Test
    void jsonLoader_resourceNotFound_throws() {
        JSONLoader loader = new JSONLoader();
        assertThrows(RuntimeException.class, () -> loader.load("nonexistent_file.json"));    
    }

    @Test
    void xmlLoader_resourceNotFound_throws() {
        XMLLoader loader = new XMLLoader();
        assertThrows(RuntimeException.class, () -> loader.load("nonexistent_file.xml"));    
    }

    @Test
    void csvLoaderParsesSampleFile() {
        CSVLoader loader = new CSVLoader();
        GameData data = loader.load("sample_game_CSV.csv");

        List<Category> categories = data.getCategories();
        assertNotNull(categories);
        assertEquals(5, categories.size());

        Category c = categories.get(0);
        assertEquals("Variables & Data Types", c.getName());
        assertEquals(5, c.getQuestions().size());

        Question q200 = c.getQuestions().stream().filter(q -> q.getPoints() == 200).findFirst().orElse(null);
        assertNotNull(q200);
        assertEquals("Which data type is used to store a single character?", q200.getQuestionStr());
        assertEquals("B", q200.getCorrectAnswer());
    }

    @Test
    void jsonLoaderParsesSampleFile() {
        JSONLoader loader = new JSONLoader();
        GameData data = loader.load("sample_game_JSON.json");

        List<Category> categories = data.getCategories();
        assertNotNull(categories);
        assertEquals(5, categories.size());

        Category c = categories.get(0);
        assertEquals("Variables & Data Types", c.getName());
        assertEquals(5, c.getQuestions().size());

        Question q200 = c.getQuestions().stream().filter(q -> q.getPoints() == 200).findFirst().orElse(null);
        assertNotNull(q200);
        assertEquals("Which data type is used to store a single character?", q200.getQuestionStr());
        assertEquals("B", q200.getCorrectAnswer());
    }

    @Test
    void xmlLoaderParsesSampleFile() {
        XMLLoader loader = new XMLLoader();
        GameData data = loader.load("sample_game_XML.xml");

        List<Category> categories = data.getCategories();
        assertNotNull(categories);
        assertEquals(5, categories.size());

        Category c = categories.get(0);
        assertEquals("Variables & Data Types", c.getName());
        assertEquals(5, c.getQuestions().size());

        Question q200 = c.getQuestions().stream().filter(q -> q.getPoints() == 200).findFirst().orElse(null);
        assertNotNull(q200);
        assertEquals("Which data type is used to store a single character?", q200.getQuestionStr());
        assertEquals("B", q200.getCorrectAnswer());
    }
}
