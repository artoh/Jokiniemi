
package artoh.jokiniemi.game;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class GameBoardTest {
    
    public GameBoardTest() {
    }

    
    @Test
    public void smallBoard() {
        GameBoardInterface board = new GameBoard();
        board.init(3);
        board.addConnection(Vehicle.TAXI, 1, 2);
        board.addConnection(Vehicle.BUS, 2, 3);
        assertEquals(1, board.connectionsCount(1));
        assertEquals(2, board.connectionsCount(2));
        assertEquals(2, board.connectionTo(1, 0));
        assertEquals(2, board.connectionTo(3, 0));
        assertEquals(3, board.squareCount());
        assertEquals(Vehicle.TAXI, board.connectionVehicle(1, 0));
        assertEquals(Vehicle.BUS, board.connectionVehicle(3, 0));
    }
    
    @Test
    public void hugeBoard() {
        GameBoardInterface board = new GameBoard();
        board.init(1000);
        for(int i=1; i <= 1000; i++) {
            for(int j=i+1; j <= 1000; j++) {
                board.addConnection(Vehicle.TAXI, i, j);
            }
        }
        for(int i=1; i<=999; i++) {
            assertEquals(999, board.connectionsCount(i));
            assertEquals(Vehicle.TAXI, board.connectionVehicle(i, i-1));
        }
        assertEquals( 1000, board.squareCount());
    }
}
