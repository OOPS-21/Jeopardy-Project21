package jeopardy_game;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

public class CSVLoader implements GameLoader {
    // Implement the load method to load game data from a CSV file
    @Override
    public GameData load(String filename) {
        List<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();
            
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filename);
            }

            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] row;
            reader.readNext();
            
            while ((row = reader.readNext()) != null) {
                String categoryName = row[0].trim();
                int value = Integer.parseInt(row[1].trim());
                String questionStr = row[2].trim();
                String optionA = row[3].trim();
                String optionB = row[4].trim();
                String optionC = row[5].trim();
                String optionD = row[6].trim();
                String correctAnswer = row[7].trim();
    
                Category category = categoryMap.get(categoryName);
                if (category == null) {
                    category = new Category(categoryName);
                    categoryMap.put(categoryName, category);
                    categories.add(category);
                }

                Map<String, String> optionsMap = new HashMap<>();
                optionsMap.put("A", optionA);
                optionsMap.put("B", optionB);
                optionsMap.put("C", optionC);
                optionsMap.put("D", optionD);
    
                Question question = new Question(questionStr, value, optionsMap, correctAnswer);
                category.addQuestion(question);   
            }
        } 
        
        catch (Exception e) {
            throw new RuntimeException("Error loading game data from CSV file: " + filename, e);
        }
        
        return new GameData(categories);
    }
}
