package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.GameBoard;
import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.StartPlaceRandomizer;
import artoh.jokiniemi.game.Vehicle;
import artoh.jokiniemi.io.BoardFileReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author arto
 */
public abstract class BoardDistanceTest {
    
    public abstract BoardDistanceInterface createInstance();
    
    /**
     * Distances in line 1 - 2 - 3 - 4 - 5 - 6
     */
    @Test
    public final void lineDistances() {
        BoardDistanceInterface distance = createInstance();
        
        GameBoardInterface gameboard = new GameBoard();
        gameboard.init(1001);
        
        for (int i = 1; i < 999; i++) {
            gameboard.addConnection(Vehicle.TAXI, i, i+1);
        }
        
        distance.init(gameboard);
        
        for (int i = 2; i < 1000; i++) {
            assertEquals(i-1, distance.distance(1, i));
        }        
    }
    
    private BoardDistanceInterface initSimple() {
        BoardDistanceInterface distance = createInstance();
        
        GameBoardInterface gameboard = new GameBoard();
        gameboard.init(8);
        
        gameboard.addConnection(Vehicle.TAXI, 1, 2);
        gameboard.addConnection(Vehicle.BUS, 1, 4);
        gameboard.addConnection(Vehicle.FERRY, 1, 5);
        gameboard.addConnection(Vehicle.TAXI, 2, 3);
        gameboard.addConnection(Vehicle.TAXI, 3, 4);
        gameboard.addConnection(Vehicle.TAXI, 3, 5);
        gameboard.addConnection(Vehicle.TAXI, 4, 5);
        gameboard.addConnection(Vehicle.UNDERGROUD, 4, 8);
        gameboard.addConnection(Vehicle.TAXI, 4, 5);
        gameboard.addConnection(Vehicle.BUS, 4, 5);
        gameboard.addConnection(Vehicle.TAXI, 5, 6);
        gameboard.addConnection(Vehicle.TAXI, 6, 7);
        gameboard.addConnection(Vehicle.TAXI, 7, 8);
        
        distance.init(gameboard);
        return distance;
    }
    
    @Test
    public final void directConnections() {
        BoardDistanceInterface di = initSimple();
        
        assertEquals(0, di.distance(3, 3));
        assertEquals(1, di.distance(1, 2));
        assertEquals(1, di.distance(3, 5));
        assertEquals(1, di.distance(8, 4));
        assertEquals(1, di.distance(6, 5));
    }
    
    @Test
    public final void combinedConnections() {
        BoardDistanceInterface di = initSimple();

        assertEquals(2, di.distance(1, 5));
        assertEquals(3, di.distance(6, 1));
        assertEquals(2, di.distance(4, 7));
        assertEquals(2, di.distance(6, 3));
        assertEquals(3, di.distance(2, 6));
        assertEquals(3, di.distance(8, 2));
    }

    @Test
    public final void negativeConnection() {
        BoardDistanceInterface di = initSimple();
        assertEquals(Integer.MAX_VALUE, di.distance(1, -1));
        assertEquals(Integer.MAX_VALUE, di.distance(-1, 1));
        assertEquals(Integer.MAX_VALUE, di.distance(-1, -1));
    }
    
    /**
     * Init with real gameboard
     * @return 
     */
    private BoardDistanceInterface initReal() {
        BoardFileReader reader = new BoardFileReader();
        StartPlaceRandomizer starter = new StartPlaceRandomizer(null);
        Game game = new Game(starter);
        reader.readBoard(game, starter);
        
        BoardDistanceInterface distance = createInstance();
        distance.init(game.gameBoard());
        return distance;
    }
    
    @Test
    public final void testRealDirectConnections() {
        BoardDistanceInterface di = initReal();
        
        int pairs[][] = {{128,160}, {198,199}, {88,117},
            {1,46}, {95,122}, {163,111}, {185,187}, {162,136},
            {7,42}, {84,67}, {13,89}, {102,127}, {142,116}};
        
        for (int i=0; i < pairs.length; i++) {
            assertEquals(1, di.distance(pairs[i][0], pairs[i][1]));
        }        
    }

    @Test
    public final void testRealCombinedConnections() {
        BoardDistanceInterface di = initReal();
        
        assertEquals(3, di.distance(13, 104));
        assertEquals(3, di.distance(108, 115));
        assertEquals(2, di.distance(1, 57));
        assertEquals(5, di.distance(194, 157));
        assertEquals(3, di.distance(157, 108));
        assertEquals(10, di.distance(7, 164));
    }
}
