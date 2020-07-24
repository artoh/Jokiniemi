package artoh.jokiniemi.game;

/**
 * Data stucture to the Game Board
 * 
 * @author ahyvatti
 */
public interface GameBoardInterface {
    
    /**
     * Init the game board
     * @param squares Count of the squares
     */
    public void init(int squares);
    
    
    /**
     * Add a connection
     * 
     * All the connections are double-direction connections
     * 
     * @param vehicle Vehicle of the connection
     * @param from One endpoint of the connection
     * @param to Second endpoint of the connection
     */
    public void addConnection(Vehicle vehicle, int from, int to);
    
    /**
     * The count of the squares in the game board
     * 
     * Note! The squares is numbered 1..count !
     * 
     * @return Number of squares in the game board
     */
    public int squareCount();
    
    /**
     * How many connections are connected to this square?
     * 
     * @param square Number of a squre
     * @return Count of connections
     */
    public int connectionsCount(int square);
    
    /**
     * What is the target of this connextion?
     * 
     * @param square Number of square
     * @param index Index of connection
     * @return Number of the target of this connection
     */
    public int connectionTo(int square, int index);
    
    /**
     * What vechile uses this connection?
     * 
     * @param square Number of square
     * @param index Index of connection
     * @return Vechile using this connection
     */
    public Vehicle connectionVehicle(int square, int index);
    
}
