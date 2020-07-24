package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;

/**
 * Algorithm counting distance between squares
 * 
 * @author ahyvatti
 */
public interface BoardDistanceInterface {

    /**
     * Initialize the algorithm to be used with game board
     * 
     * Depending on the algorithm, this function may calculate
     * the distance matrix or initalize the distance cache
     * 
     * @param gameboard 
     */
    public void init(GameBoardInterface gameboard);
    
    
    /**
     * Get the distance between two squares
     * 
     * Using ferry is not counted because of the detectives is not allowed
     * to traver on ferry.
     * 
     * @param from First square
     * @param to Second square
     * @return Distance in turns
     */
    public int distance(int from, int to);
    
}
