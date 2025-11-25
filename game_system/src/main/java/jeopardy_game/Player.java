package jeopardy_game;

public class Player {
    private int index;
    private String name;
    private int score;

    public Player(int index, String name, int score) {
        this.index = index;
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void addPoints(int p) {
        this.score += p;
    }

    public void subtractPoints(int p) {
        this.score -= p;
    }

    //extra getters
     public int getIndex() {
        return this.index;
    }
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Player{" + "index=" + index + ", name='" + name + '\'' + ", score=" + score + '}';
    }

}