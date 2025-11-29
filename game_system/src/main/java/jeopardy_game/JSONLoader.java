package jeopardy_game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads game data from a JSON file and constructs the appropriate Category
 * and Question objects. The JSON must represent a list of objects where each
 * object contains category information, question text, point value, options
 * and the correct answer.
 */
public class JSONLoader implements GameLoader {

    /**
     * Loads game data from the specified JSON file.
     * Expected JSON structure is a list of items:
     * {
     *   "Category": "Science",
     *   "Value": 200,
     *   "Question": "What is H2O?",
     *   "Options": {"A": "Water", "B": "Oxygen", "C": "Hydrogen", "D": "Helium"},
     *   "CorrectAnswer": "A"
     * }
     *
     * @param filename the name of the JSON resource file to load
     * @return a GameData object containing all parsed categories and questions
     * @throws RuntimeException if the JSON cannot be read or parsed
     */
    @Override
    public GameData load(String filename) {
        List<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + filename);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(inputStream, List.class);

            for (Map<String, Object> row : data) {
                String categoryName = (String) row.get("Category");
                int value = (int) row.get("Value");
                String questionStr = (String) row.get("Question");
                Map<String, String> optionsMap = (Map<String, String>) row.get("Options");
                String correctAnswer = (String) row.get("CorrectAnswer");

                Category category = categoryMap.get(categoryName);
                if (category == null) {
                    category = new Category(categoryName);
                    categoryMap.put(categoryName, category);
                    categories.add(category);
                }

                Question question = new Question(questionStr, value, optionsMap, correctAnswer);
                category.addQuestion(question);
            }
        }

        catch (Exception e) {
            throw new RuntimeException("Error loading game data from JSON file: " + filename, e);
        }

        return new GameData(categories);
    }
}