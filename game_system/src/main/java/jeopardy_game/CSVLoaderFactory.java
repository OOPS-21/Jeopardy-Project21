package jeopardy_game;

/**
 * Factory class for creating CSVLoader instances.
 * This class implements GameLoaderFactory and produces
 * CSV-specific loaders for loading game data from CSV files.
 */
public class CSVLoaderFactory implements GameLoaderFactory {

    /**
     * Creates and returns a new CSVLoader instance.
     * @return a CSVLoader for loading game data from CSV files
     */
    @Override
    public GameLoader createLoader() {
        return new CSVLoader();
    }
}
