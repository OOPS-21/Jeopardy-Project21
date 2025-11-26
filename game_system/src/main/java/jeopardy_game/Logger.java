package jeopardy_game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Subscriber {
    private static Logger loggerinstance;
    private final List<Event> events = new ArrayList<>();
    private final String logFile = "game_event_log.csv";

    private Logger() {
        try {
            File file = new File(logFile);
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play\n");
                }
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    public static Logger getLogger() {
        if (loggerinstance == null) {
            loggerinstance = new Logger();
        }
        return loggerinstance;
    }

    @Override
    public void update (Event event) {
        events.add(event);
    }

    public void generateReport() {

    }

    public void generateEventLogs() {
        for (Event event : events) {
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.append(formatEventAsCSV(event)).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatEventAsCSV(Event event) {
        return String.join(",",
            event.getCaseId(),
            event.getPlayerId(),
            event.getActivity(),
            event.getTimestamp(),
            event.getCategory() != null ? event.getCategory() : "",
            event.getQuestionValue() != null ? event.getQuestionValue().toString() : "",
            event.getAnswerGiven() != null ? event.getAnswerGiven() : "",
            event.getResult() != null ? event.getResult() : "",
            event.getScoreAfterPlay() != null ? event.getScoreAfterPlay().toString() : ""
        );
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }
}
