/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.AppliedFloydWarshallDistance;
import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.TicketAwareDistance;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.GameBoard;
import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.GameLog;
import artoh.jokiniemi.game.StartPlaceInterface;
import artoh.jokiniemi.game.Vehicle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class NearestDetectiveCacheTest {
    
    public NearestDetectiveCacheTest() {
    }
    
    @Before
    public void setUp() {
        game = new MockGame();
        
        game.gameBoard().init(10);
        for (int i=1; i < 9; i +=3 ) {
            game.gameBoard().addConnection(Vehicle.TAXI, i, i+1);
            game.gameBoard().addConnection(Vehicle.BUS, i+1, i+2);
            game.gameBoard().addConnection(Vehicle.UNDERGROUD, i+2, i+3);
        }
        game.gameBoard().addConnection(Vehicle.TAXI, 9, 10);
        
        BoardDistanceInterface distancer = new AppliedFloydWarshallDistance();
        distancer.init(game.gameBoard());
        
        this.cache = new NearestDetectiveCache(game, distancer);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void distanceCounting() {
        assertEquals(0, cache.countDistance(1));
        assertEquals(1, cache.countDistance(3));
        assertEquals(2, cache.countDistance(6));
        assertEquals(0, cache.countDistance(8));
    }
    
    @Test
    public void distancesWithCacheWithoutInvalidate() {
        assertEquals(0, cache.distance(1));
        assertEquals(1, cache.distance(3));
        game.move();
        assertEquals(0, cache.distance(1));
        assertEquals(1, cache.distance(3));        
    }
    
    @Test
    public void distancesWithCacheWithInvalidate() {
        assertEquals(0, cache.distance(1));
        assertEquals(1, cache.distance(3));
        assertEquals(2, cache.countDistance(6));
        game.move();
        cache.invalidate();
        assertEquals(1, cache.distance(1));
        assertEquals(1, cache.distance(3));        
        assertEquals(1, cache.countDistance(6));        
    }
    
    @Test
    public void minTicketsCount() {
        assertEquals(8, cache.minTickets());
        game.testingTickets = true;
        assertEquals(2, cache.minTickets());
    }
    
    private NearestDetectiveCache cache;
    private MockGame game;
        
    private class MockLog extends GameLog {
        @Override
        public int currentPosition(int detective) {
            switch (detective) {
                case 1:
                    return 1 + moves;
                case 2:
                    return 4 + moves;
                case 3:
                    return 8;
            }
            return 10;
        }
        
        public void move() {
            this.moves++;
        }        
        
        private int moves = 0;                
    }
        
    private class MockGame extends Game {
        
        public MockGame() {
            super(null);
            this.mockLog = new MockLog();
        }
        
        @Override
        public GameLog log() {
            return this.mockLog;
        }
        
        @Override
        public int detectives() {
            return 3;
        }
                
        public void move() {
            this.mockLog.move();
        }       
        
        @Override
        public int ticketsLeft(Vehicle ticket) {
            if (testingTickets) {
                return ticket == Vehicle.UNDERGROUD ? 16 : 
                    ticket == Vehicle.BUS ? 8 : 2;                    
                
            } else {
                return ticket == Vehicle.UNDERGROUD ? 8 : 
                    ticket == Vehicle.BUS ? 16 : 22;                                    
            }
        }
        
        private MockLog mockLog;
        boolean testingTickets = false;
    }
}
