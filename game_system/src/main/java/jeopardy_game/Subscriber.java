package jeopardy_game;

/**
 * Represents an observer that receives updates when an event occurs.
 */
public interface Subscriber {

    /**
     * Called when an event is published.
     *
     * @param event the event that occurred; never null
     */
    public void update(Event event);
}
