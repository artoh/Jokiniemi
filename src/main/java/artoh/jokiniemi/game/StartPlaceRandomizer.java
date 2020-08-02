package artoh.jokiniemi.game;

import artoh.jokiniemi.algorithm.IntegerArray;
import artoh.jokiniemi.algorithm.RandomizeInterface;

/**
 * Get the start places for Mister X and the players
 * 
 * @author ahyvatti
 */
public class StartPlaceRandomizer implements StartPlaceInterface {
    
    protected RandomizeInterface randomizer;
   
    /**
     * Initialize randomizer 
     * 
     * @param randomizer Random number algorithm 
     */
    public StartPlaceRandomizer(RandomizeInterface randomizer) {
        this.randomizer = randomizer;
        this.detectivePlaces = new IntegerArray(DEFAULT_PLACES_CAPACITY);
        this.mrXPlaces = new IntegerArray(DEFAULT_PLACES_CAPACITY);        
    }
    
    /**
     * 
     * Add a possible start place
     * 
     * @param misterX True if it start place for Mister X, false if for Detective
     * @param square  Square number
     */
    public void addStartPlace(boolean misterX, int square) {
        if (misterX) {
            mrXPlaces.push(square);
        } else {
            detectivePlaces.push(square);
        }
    }    
    
    @Override
    public int startNewGameAndGetStartPlaceForMisterX() {
        this.usedDetectivePlaces = new boolean[detectivePlaces.count()];
        this.detectivesPlaced = 0;                       
        
        return mrXPlaces.at(this.randomizer.next(this.mrXPlaces.count()) - 1);
    }
        
    @Override
    public int getStartPlaceForDetective() {
        
        int random = this.randomizer.next(this.detectivePlaces.count() - this.detectivesPlaced);
        int index = -1;
        
        for (int i=0; i < random; i++) {
            index++;
            while (usedDetectivePlaces[index]) {
                index++;
            }
        }
        this.detectivesPlaced++;
        this.usedDetectivePlaces[index] = true;
        return this.detectivePlaces.at(index);
    }
    
    private static final int DEFAULT_PLACES_CAPACITY = 16;
    
    private IntegerArray detectivePlaces;
    private IntegerArray mrXPlaces;
    private boolean[] usedDetectivePlaces;
    private int detectivesPlaced;    
}
