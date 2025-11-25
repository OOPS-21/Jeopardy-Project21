package jeopardy_game;

public class JSONLoaderFactory implements GameLoaderFactory {
    // Implement the createLoader method to return a JSONLoader instance
    @Override
    public GameLoader createLoader() {
        return new JSONLoader();
    }
}
