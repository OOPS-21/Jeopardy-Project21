package jeopardy_game;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.util.*;

public class XMLLoader implements GameLoader {
    // Implement the load method to load game data from an XML file
    @Override
    public GameData load(String filename) {
        List<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();

        try {
            XmlMapper xmlMapper = new XmlMapper();
            Map<String, List<Map<String, Object>>> root = xmlMapper.readValue(
                new File(filename),
                Map.class
            );

            List<Map<String, Object>> data = root.get("QuestionItem");

                for (Map<String, Object> row : data) {
                    String categoryName = (String) row.get("Category");
                    int value = (int) row.get("Value");
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
                    optionsMap.put("A", (String) optionsObj.get("A"));
                    optionsMap.put("B", (String) optionsObj.get("B"));
                    optionsMap.put("C", (String) optionsObj.get("C"));
                    optionsMap.put("D", (String) optionsObj.get("D"));
                  
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
