package jeopardy_game;

/**
 * Factory class for creating XMLLoader instances.
 * This class implements GameLoaderFactory and produces
 * XML-specific loaders for loading game data from XML files.
 */
public class XMLLoaderFactory implements GameLoaderFactory {

    /**
     * Creates and returns a new XMLLoader instance.
     * @return a XMLLoader for loading game data from XML files
     */
    @Override
    public GameLoader createLoader() {
        return new XMLLoader();
    }
}
