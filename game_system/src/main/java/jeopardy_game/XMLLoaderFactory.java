package jeopardy_game;

public class XMLLoaderFactory implements GameLoaderFactory {
    // Implement the createLoader method to return a XMLLoader instance
    @Override
    public GameLoader createLoader() {
        return new XMLLoader();
    }
}
