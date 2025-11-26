package jeopardy_game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONLoader implements GameLoader {
    // Implement the load method to load game data from a JSON file
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
