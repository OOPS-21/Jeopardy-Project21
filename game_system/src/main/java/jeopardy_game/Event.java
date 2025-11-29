package jeopardy_game;

/**
 * Represents a single logged event that occurs during a Jeopardy game.
 * 
 * Events capture information such as the player involved, the action taken,
 * the question details and the score after the action.
 * Instances of this class are immutable and are constructed using the Builder.
 */
public class Event {
    private final String caseId;
    private final String playerId;
    private final String activity;
    private final String timestamp;
    private final String category;        
    private final Integer questionValue; 
    private final String questionText;
    private final String answerGiven;     
    private final String result;         
    private final Integer scoreAfterPlay; 

    /**
     * Private constructor used by the Builder to create an Event instance.
     */
    private Event(Builder builder) {
        this.caseId = builder.caseId;
        this.playerId = builder.playerId;
        this.activity = builder.activity;
        this.timestamp = builder.timestamp;
        this.category = builder.category;
        this.questionValue = builder.questionValue;
        this.questionText = builder.questionText;
        this.answerGiven = builder.answerGiven;
        this.result = builder.result;
        this.scoreAfterPlay = builder.scoreAfterPlay;
    }

     /** Returns the case identifier for the game session. */
    public String getCaseId() { return this.caseId; }
    /** Returns the ID of the player associated with this event. */
    public String getPlayerId() { return this.playerId; }
    /** Returns the type of activity recorded in this event. */
    public String getActivity() { return this.activity; }
    /** Returns a timestamp indicating when the event occurred. */
    public String getTimestamp() { return this.timestamp; }
    /** Returns the category of the question involved in the event, if any. */
    public String getCategory() { return this.category; }
    /** Returns the point value of the question involved, if any. */
    public Integer getQuestionValue() { return this.questionValue; }
    /** Returns the text of the question involved in the event. */
    public String getQuestionText() { return this.questionText; }
    /** Returns the player's submitted answer, if applicable. */
    public String getAnswerGiven() { return this.answerGiven; }
    /** Returns the result of the event (such as correct or incorrect). */
    public String getResult() { return this.result; }
    /** Returns the player's score immediately after the event. */
    public Integer getScoreAfterPlay() { return this.scoreAfterPlay; }

    /**
     * Builder for constructing immutable Event objects.
     * The builder requires a case ID, player ID, an activity type and a timestamp
     * but all other event fields are optional and can be specified using
     * the builder's setter-style methods.
     */
    public static class Builder {
        private final String caseId;
        private String playerId;
        private final String activity;
        private final String timestamp;
        private String category;
        private Integer questionValue;
        private String questionText;
        private String answerGiven;
        private String result;
        private Integer scoreAfterPlay;

        /**
         * Creates a new Builder with the required event fields.
         * @param caseId an identifier for the game session
         * @param playerId an identifier for the player
         * @param activity the action performed
         * @param timestamp the time at which the event occurred
         */
        public Builder(String caseId, String playerId, String activity, String timestamp) {
            this.caseId = caseId;
            this.playerId = playerId;
            this.activity = activity;
            this.timestamp = timestamp;
        }

        /** Sets the category associated with this event. */
        public Builder category(String category) { this.category = category; return this; }
        /** Sets the point value of the question involved in the event. */
        public Builder questionValue(Integer questionValue) { this.questionValue = questionValue; return this; }
        /** Sets the question text for this event. */
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        /** Sets the answer given by the player. */
        public Builder answerGiven(String answerGiven) { this.answerGiven = answerGiven; return this; }
        /** Sets the result of the player's action, such as "correct" or "incorrect". */
        public Builder result(String result) { this.result = result; return this; }
        /** Sets the player's score immediately after the event occurred. */
        public Builder scoreAfterPlay(Integer scoreAfterPlay) { this.scoreAfterPlay = scoreAfterPlay; return this; }

        /**
         * Builds and returns a fully constructed Event object.
         * @return a new immutable Event instance
         */
        public Event build() { 
            return new Event(this); 
        }
    }
}
