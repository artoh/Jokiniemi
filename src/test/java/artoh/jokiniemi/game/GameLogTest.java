/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

import org.junit.Test;
import static org.junit.Assert.*;

import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;

/**
 *
 * @author arto
 */
public class GameLogTest {
    
    public GameLogTest() {
    }

    
    @Test
    public void totalTurns() {
        GameLog log = new GameLog();
        log.init(25);
        assertEquals(25, log.turnsTotal());
    }
    
    @Test
    public void turnsLeft() {
        StartPlaceRandomizer starter = new StartPlaceRandomizer(new LinearCongruentialGenerator());
        starter.addStartPlace(true, 10);
        starter.addStartPlace(false, 20);
        
        GameLog log = new GameLog();        
        log.init(25);        
        log.newGame(1, starter);
        
        assertEquals(24, log.turnsLeft());
    }
    
    @Test
    public void visibleTurnsTest() {
        GameLog log = new GameLog();
        log.init(25);
        log.setVisibleTurn(5);
        log.setVisibleTurn(10);
        assertFalse(log.isVisibleTurn(3));
        assertTrue(log.isVisibleTurn(10));
    }
    
    @Test
    public void visibleTurnsAfterEnd() {
        GameLog log = new GameLog();
        log.init(25);
        assertFalse(log.isVisibleTurn(26));                
        assertFalse(log.isVisibleTurn(35));                                
    }
    
    @Test
    public void testGameLogging() {
        StartPlaceRandomizer starter = new StartPlaceRandomizer(new LinearCongruentialGenerator());
        starter.addStartPlace(true, 10);
        starter.addStartPlace(false, 20);
        
        GameLog log = new GameLog();
        log.init(25);
        log.newGame(1, starter);
        
        assertEquals(0, log.currentTurn());
        assertEquals(10, log.currentPosition(0));
        assertEquals(Vehicle.START_SQUARE, log.vehicle(1, 0));
        
        log.logTurn(0, 30, Vehicle.TAXI, false);
        
        assertEquals(1, log.currentTurn());
        
        log.logTurn(1, 40, Vehicle.BUS, false);
        
        assertEquals(30, log.currentPosition(0));
        assertEquals(40, log.position(1, 1));
        
        log.logTurn(0, 60, Vehicle.UNDERGROUD, true);
        
        assertEquals(Vehicle.DOUBLED, log.vehicle(1, 2));
        
    }
}
