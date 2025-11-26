package jeopardy_game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingTest {

    @Test
    void csvLoaderParsesSampleFile() {
        CSVLoader loader = new CSVLoader();
        GameData data = loader.load("sample_game_CSV.csv");

        List<Category> cats = data.getCategories();
        assertNotNull(cats);
        assertEquals(1, cats.size());

        Category gen = cats.get(0);
        assertEquals("General", gen.getName());
        assertEquals(2, gen.getQuestions().size());

        Question q100 = gen.getQuestions().stream().filter(q -> q.getPoints() == 100).findFirst().orElse(null);
        assertNotNull(q100);
        assertEquals("Which of the following declares an integer variable in C++?", q100.getQuestionStr());
        assertEquals("A", q100.getCorrectAnswer());
    }

    @Test
    void jsonLoaderParsesSampleFile() {
        JSONLoader loader = new JSONLoader();
        GameData data = loader.load("sample_game_JSON.json");

        List<Category> cats = data.getCategories();
        assertNotNull(cats);
        assertEquals(1, cats.size());

        Category gen = cats.get(0);
        assertEquals("General", gen.getName());
        assertEquals(2, gen.getQuestions().size());

        Question q200 = gen.getQuestions().stream().filter(q -> q.getPoints() == 200).findFirst().orElse(null);
        assertNotNull(q200);
        assertEquals("Which data type is used to store a single character?", q200.getQuestionStr());
        assertEquals("B", q200.getCorrectAnswer());
    }

    @Test
    void xmlLoaderParsesSampleFile() {
        XMLLoader loader = new XMLLoader();
        GameData data = loader.load("sample_game_XML.xml");

        List<Category> cats = data.getCategories();
        assertNotNull(cats);
        assertEquals(1, cats.size());

        Category gen = cats.get(0);
        assertEquals("General", gen.getName());
        assertEquals(2, gen.getQuestions().size());

        Question q100 = gen.getQuestions().stream().filter(q -> q.getPoints() == 100).findFirst().orElse(null);
        assertNotNull(q100);
        assertEquals("Which of the following declares an integer variable in C++?", q100.getQuestionStr());
        assertEquals("A", q100.getCorrectAnswer());
    }
}
