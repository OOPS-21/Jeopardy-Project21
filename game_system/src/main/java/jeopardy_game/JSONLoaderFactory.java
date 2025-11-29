package jeopardy_game;

/**
 * Factory class for creating JSONLoader instances.
 * This class implements GameLoaderFactory and produces
 * JSON-specific loaders for loading game data from JSON files.
 */
public class JSONLoaderFactory implements GameLoaderFactory {

    /**
     * Creates and returns a new JSONLoader instance.
     * @return a JSONLoader for loading game data from JSON files
     */
    @Override
    public GameLoader createLoader() {
        return new JSONLoader();
    }
}
