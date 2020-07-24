package artoh.jokiniemi.algorithm;

/**
 * Generator of (pseudo) random integers.
 * 
 * The generator should be initalized in the constructor, using
 * for example the time.
 * 
 * @author ahyvatti
 */
public interface RandomizeInterface {
    
    
    /**
     * Returns a (pseudo) random integer between 1..max
     * 
     * @param max Maximum of the integer
     * @return Randomized integer
     */
    public int next(int max);
    
}
