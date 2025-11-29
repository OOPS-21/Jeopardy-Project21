package jeopardy_game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton logger responsible for collecting and writing game events to a CSV file.
 * 
 * The Logger acts as a Subscriber in the Observer pattern. Whenever an Event
 * is published, the update method stores the event. Logs are later exported
 * to a CSV file for process mining.
 */
public class Logger implements Subscriber {
    private static Logger loggerinstance;
    private final List<Event> events = new ArrayList<>();
    private final String logFile = "game_event_log.csv";

    /**
     * Private constructor that initialises the log file. If the file does not exist,
     * it creates one and writes the header row.
     */
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

    /**
     * Returns the singleton instance of the Logger.
     *
     * @return the shared Logger instance
     */
    public static Logger getLogger() {
        if (loggerinstance == null) {
            loggerinstance = new Logger();
        }
        return loggerinstance;
    }

    /**
     * Receives an event from a Publisher and stores it in memory.
     *
     * @param event the event that occurred
     */
    @Override
    public void update (Event event) {
        events.add(event);
    }

    /**
     * Returns all recorded events as a defensive copy.
     *
     * @return a list of all events collected so far
     */
    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Writes all recorded events to the CSV log file. Each event is appended
     * to the file in CSV format.
     */
    public void generateEventLogs() {
        for (Event event : events) {
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.append(formatEventAsCSV(event)).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Converts an event object into a CSV-compatible string.
     *
     * @param event the event to format
     * @return a CSV row representing the event
     */
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
}
