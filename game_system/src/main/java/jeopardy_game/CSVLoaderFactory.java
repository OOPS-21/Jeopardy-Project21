package jeopardy_game;

public class CSVLoaderFactory implements GameLoaderFactory {
    // Implement the createLoader method to return a CSVLoader instance
    @Override
    public GameLoader createLoader() {
        return new CSVLoader();
    }
}
