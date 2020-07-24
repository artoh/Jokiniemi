package artoh.jokiniemi.game;

import artoh.jokiniemi.algorithm.RandomizeInterface;

import java.lang.UnsupportedOperationException;

/**
 * Get the start places for Mister X and the players
 * 
 * When beginning a new game, ask first the place for Mr X using
 * startNewGameAndGetStartPlaceForMisterX() and then ask
 * start places for the detectives.
 * 
 * @author ahyvatti
 */
public class StartPlaceRandomizer {
    
    protected RandomizeInterface randomizer;
   
    /**
     * Initialize randomizer 
     * 
     * @param randomizer Random number algorithm 
     */
    public StartPlaceRandomizer(RandomizeInterface randomizer) {
        this.randomizer = randomizer;
    }
    
    /**
     * 
     * Add a possible start place
     * 
     * @param misterX True if it start place for Mister X, false if for Detective
     * @param square  Square number
     */
    public void addStartPlace(boolean misterX, int square) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Start a new game and get start place for mister X
     * 
     * @return Square number 
     */
    public int startNewGameAndGetStartPlaceForMisterX() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 
     * Get start place for detective
     * 
     * @return Square number
     */
    public int getStartPlaceForDetective() {
        throw new UnsupportedOperationException();
    }
    
}
