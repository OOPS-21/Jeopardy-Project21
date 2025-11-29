package jeopardy_game;

/**
 * Factory interface for creating GameLoader instances.
 * Implementations of this interface should provide a specific
 * loader for a given file type (CSV, JSON, XML).
 */
public interface GameLoaderFactory {
    
    /**
     * Creates and returns a new GameLoader instance.
     *
     * @return a GameLoader for loading game data
     */
    GameLoader createLoader();
}
