package artoh.jokiniemi.game;

import artoh.jokiniemi.algorithm.RandomizeInterface;

/**
 * Keep a log of positons and vehicles of mr X and detectives
 * 
 * @author ahyvatti
 */
public class GameLog {
    
    private int turn = 0;
    private int turns = 0;
    
    static final private int TURNS_TOTAL = 25;
    
    public GameLog() {
        
    }
    
    /**
     * Is Mr X visible during this turn
     * @param turn Turn number
     * @return True if positon of Mr X is visibe
     */
    public boolean isVisibleTurn(int turn) {
        throw new UnsupportedOperationException();  
    }
    
    /**
     * Start a new game. Mark the initial positions of Mr X and detectives.
     * 
     * @param detectives Count of detectives
     * @param randomizer Randomizer object
     */
    public void newGame(int detectives, RandomizeInterface randomizer) {
        
    }
    
    /**
     * Number of the current turn
     * @return Number of turn
     */
    public int currentTurn() {
        return turn;
    }
    
    /**
     * Amount of turns total in the game
     * @return Amount of turn
     */
    public int turnsTotal() {
        return TURNS_TOTAL;
    }
    
    /**
     * Add a mark to the log
     * 
     * @param player Number of player (0: mr X, 1..n: detectives)
     * @param square Square number
     * @param vehicle Vehicle used to go here
     * @param doubled Mr X use the Double card
     * @return Turn number
     */
    public int logTurn(int player, int square, Vehicle vehicle, boolean doubled) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Return a position of a player
     * @param player Number a player (0: mrX, 1..n detectives)
     * @param turn Number of turn
     * @return Square of player position
     */
    public int position(int player, int turn) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Return a vehicle of plaer when moving to the square
     * @param player Number of a player (0: mrX 1..n detectives)
     * @param turn Number of turn
     * @return Vehicle used to move
     */
    public Vehicle vehicle(int player, int turn) {
        throw new UnsupportedOperationException();
    }
    
}
