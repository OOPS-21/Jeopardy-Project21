package jeopardy_game;

/**
 * Interface for game data loaders.
 * Implementations should provide a method to load game data from a file.
 */
public interface GameLoader {

    /**
     * Loads game data from the specified file.
     *
     * @param filename the name of the file to load
     * @return a GameData object containing the loaded categories and questions
     */
    GameData load(String filename);
}
