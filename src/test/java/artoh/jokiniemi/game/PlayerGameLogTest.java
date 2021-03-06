/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class PlayerGameLogTest {
    
    public PlayerGameLogTest() {
    }


    @Test
    public void shortDetectiveLogTest() {
        PlayerGameLog log = new PlayerGameLog(25,100);
        log.addTurn(101, Vehicle.TAXI);
        log.addTurn(102, Vehicle.BUS);
        log.addTurn(103, Vehicle.UNDERGROUD);
        log.addTurn(104, Vehicle.DOUBLED);
        
        assertEquals(100, log.position(0));
        assertEquals(Vehicle.START_SQUARE, log.vehicle(0));
        
        assertEquals(101, log.position(1));
        assertEquals(Vehicle.TAXI, log.vehicle(1));

        assertEquals(102, log.position(2));
        assertEquals(Vehicle.BUS, log.vehicle(2));

        
        assertEquals(103, log.position(3));
        assertEquals(Vehicle.UNDERGROUD, log.vehicle(3));
        
        assertEquals(104, log.position(4));
        assertEquals(Vehicle.DOUBLED, log.vehicle(4));        
    }
    
    @Test
    public void mrXBlackCardTest() {
        PlayerGameLog log = new PlayerGameLog(25,40);
        log.addTurn(80, Vehicle.BLACK_CARD);
        
        assertEquals(40, log.position(0));
        assertEquals(80, log.position(1));
        
        assertEquals(Vehicle.BLACK_CARD, log.vehicle(1));        
        assertEquals(1, log.turn());
    }

    @Test
    public void hugePlayerLogTest() {
        PlayerGameLog log = new PlayerGameLog(1001,1);
        
        for(int i=1; i<1000;i++) {
            assertEquals(i,log.addTurn(i+10, Vehicle.TAXI));
            assertEquals(i+10, log.currentPosition());
        }
        for(int i=1; i<1000; i++) {
            assertEquals(i+10, log.position(i));
        }                
    }
    
    @Test
    public void logginAfterGameEnds() {
        PlayerGameLog log = new PlayerGameLog(2,10);
        assertEquals(10, log.currentPosition());
        assertEquals(1, log.addTurn(101, Vehicle.TAXI));
        assertEquals(101, log.currentPosition());
        assertEquals(2, log.addTurn(102, Vehicle.BUS));
        assertEquals(102, log.currentPosition());
        assertEquals(2, log.addTurn(103, Vehicle.UNDERGROUD));                
        assertEquals(102, log.currentPosition());
    }
    
}
