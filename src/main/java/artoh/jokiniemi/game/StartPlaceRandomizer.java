package artoh.jokiniemi.game;

import artoh.jokiniemi.algorithm.RandomizeInterface;

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
        this.detectivePlaces = new IntegerArray(DEFAULT_PLACE_CAPACITY);
        this.mrXPlaces = new IntegerArray(DEFAULT_PLACE_CAPACITY);        
    }
    
    /**
     * 
     * Add a possible start place
     * 
     * @param misterX True if it start place for Mister X, false if for Detective
     * @param square  Square number
     */
    public void addStartPlace(boolean misterX, int square) {
        if( misterX ) {
            mrXPlaces.push(square);
        } else {
            detectivePlaces.push(square);
        }
    }
    
    /**
     * Start a new game and get start place for mister X
     * 
     * @return Square number 
     */
    public int startNewGameAndGetStartPlaceForMisterX() {
        this.usedDetectivePlaces = new boolean[detectivePlaces.count()];
        this.detectivesPlaced = 0;
        
        return mrXPlaces.at( this.randomizer.next( this.mrXPlaces.count()) -1 );
    }
    
    /**
     * 
     * Get start place for detective
     * 
     * @return Square number
     */
    public int getStartPlaceForDetective() {
        int index = this.randomizer.next( this.detectivePlaces.count() - this.detectivesPlaced ) - 1;
        for(int i=0; i<index; i++) {
            if( usedDetectivePlaces[i]) {
                index++;
            }
        }
        this.usedDetectivePlaces[index] = true;
        return this.detectivePlaces.at(index);
    }
    
    private final int DEFAULT_PLACE_CAPACITY = 16;
    
    private IntegerArray detectivePlaces;
    private IntegerArray mrXPlaces;
    private boolean[] usedDetectivePlaces;
    private int detectivesPlaced;    
}
