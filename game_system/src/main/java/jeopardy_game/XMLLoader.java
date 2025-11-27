package jeopardy_game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XMLLoader implements GameLoader {
    // Implement the load method to load game data from an XML file
    @Override
    public GameData load(String filename) {
        List<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + filename);
            }

            XmlMapper xmlMapper = new XmlMapper();
            Map<String, Object> root = xmlMapper.readValue(inputStream, Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) root.get("QuestionItem");

                for (Map<String, Object> row : data) {
                    String categoryName = (String) row.get("Category");
                    int value = Integer.parseInt(row.get("Value").toString());
                    String questionStr = (String) row.get("QuestionText");
                    Map<String,Object> optionsObj = (Map<String,Object>) row.get("Options");
                    String correctAnswer = (String) row.get("CorrectAnswer");

                    Category category = categoryMap.get(categoryName);
                    if (category == null) {
                        category = new Category(categoryName);
                        categoryMap.put(categoryName, category);
                        categories.add(category);
                    }

                    Map<String, String> optionsMap = new HashMap<>();
                    optionsMap.put("A", (String) optionsObj.get("OptionA"));
                    optionsMap.put("B", (String) optionsObj.get("OptionB"));
                    optionsMap.put("C", (String) optionsObj.get("OptionC"));
                    optionsMap.put("D", (String) optionsObj.get("OptionD"));
                    
                    Question question = new Question(questionStr, value, optionsMap, correctAnswer);
                    category.addQuestion(question);
                }
        } 
        
        catch (Exception e) {
            throw new RuntimeException("Error loading game data from XML file: " + filename, e);
        }
    
        return new GameData(categories);
    }
}
