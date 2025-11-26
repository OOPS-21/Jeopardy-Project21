package jeopardy_game;

public class Event {
    private final String caseId;
    private final String playerId;
    private final String activity;
    private final String timestamp;
    private final String category;        
    private final Integer questionValue; 
    private final String answerGiven;     
    private final String result;         
    private final Integer scoreAfterPlay; 

    private Event(Builder builder) {
        this.caseId = builder.caseId;
        this.playerId = builder.playerId;
        this.activity = builder.activity;
        this.timestamp = builder.timestamp;
        this.category = builder.category;
        this.questionValue = builder.questionValue;
        this.answerGiven = builder.answerGiven;
        this.result = builder.result;
        this.scoreAfterPlay = builder.scoreAfterPlay;
    }

    public String getCaseId() {return this.caseId;}
    public String getPlayerId() {return this.playerId;}
    public String getActivity() {return this.activity;}
    public String getTimestamp() {return this.timestamp;}
    public String getCategory() {return this.category;}
    public Integer getQuestionValue() {return this.questionValue;}
    public String getAnswerGiven() {return this.answerGiven;}
    public String getResult() {return this.result;}
    public Integer getScoreAfterPlay() {return this.scoreAfterPlay;}

    public static class Builder {
        private final String caseId;
        private String playerId;
        private final String activity;
        private final String timestamp;
        private String category;
        private Integer questionValue;
        private String answerGiven;
        private String result;
        private Integer scoreAfterPlay;

        public Builder(String caseId, String activity, String timestamp) {
            this.caseId = caseId;
            this.activity = activity;
            this.timestamp = timestamp;
        }

        public Builder playerId(String playerId) { this.playerId = playerId; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder questionValue(Integer questionValue) { this.questionValue = questionValue; return this; }
        public Builder answerGiven(String answerGiven) { this.answerGiven = answerGiven; return this; }
        public Builder result(String result) { this.result = result; return this; }
        public Builder scoreAfterPlay(Integer scoreAfterPlay) { this.scoreAfterPlay = scoreAfterPlay; return this; }

        public Event build() { 
            return new Event(this); 
        }
    }
}
