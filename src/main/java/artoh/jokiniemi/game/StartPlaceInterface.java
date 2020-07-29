package artoh.jokiniemi.game;

/**
 * Interface for start places randomizer
 * 
 * When beginning a new game, ask first the place for Mr X using
 * startNewGameAndGetStartPlaceForMisterX() and then ask
 * start places for the detectives.
 * 
 * @author arto
 */
public interface StartPlaceInterface {
    
    /**
     * Start a new game and get start place for mister X
     * 
     * @return Square number 
     */    
    public int startNewGameAndGetStartPlaceForMisterX();

    /**
     * 
     * Get start place for detective
     * 
     * @return Square number
     */
    public int getStartPlaceForDetective();    
    
}
