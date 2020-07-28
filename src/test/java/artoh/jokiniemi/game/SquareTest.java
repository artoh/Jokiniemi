
package artoh.jokiniemi.game;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class SquareTest {
    
    public SquareTest() {
    }
        
    @Test
    public void addOneConnection() {
        Square square = new Square();
        assertEquals(0, square.connectionsCount());
        square.addConnection(Vehicle.TAXI, 10);
        assertEquals(1, square.connectionsCount());
        assertEquals(Vehicle.TAXI, square.connectionVehicle(0));
        assertEquals(10, square.connectionTo(0));
    }
    
    @Test
    public void addTenConnections() {
        Square square = new Square();
        for(int i=10; i<20;i++) {
            square.addConnection(Vehicle.BUS, i);
        }
        assertEquals(10, square.connectionsCount());
        for(int i=0; i<10; i++) {
            assertEquals(i+10, square.connectionTo(i));
            assertEquals(Vehicle.BUS, square.connectionVehicle(i));
        }
    }

    @Test
    public void ferryConnection() {
        Square square = new Square();
        square.addConnection(Vehicle.FERRY, 100);
        assertEquals(Vehicle.FERRY, square.connectionVehicle(0));
    }
    
    @Test
    public void undergroudConnection() {
        Square square = new Square();
        square.addConnection(Vehicle.UNDERGROUD, 120);
        assertEquals(Vehicle.UNDERGROUD, square.connectionVehicle(0));        
    }
    
    @Test
    public void uncorrectConnection() {
        Square square = new Square();
        square.addConnection(Vehicle.BLACK_CARD, 120);
        assertEquals(Vehicle.START_SQUARE, square.connectionVehicle(0));        
    }
    
    @Test
    public void addThousandConnections() {
        Square square = new Square();
        for(int i=0; i<1000;i++) {
            square.addConnection(Vehicle.TAXI, i+1);
        }
        assertEquals(1000, square.connectionsCount());
    }
    
}
