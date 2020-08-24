/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

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
public class ScoredAlternativeTest {
    
    public ScoredAlternativeTest() {
    }
    
    @Before
    public void setUp() {
        
        this.game = new MockGame();
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void getScore() {
        ScoredAlternative alternative = new ScoredAlternative(Vehicle.BUS, 15, Vehicle.START_SQUARE, 0, 100 );        
        assertEquals(100, alternative.score());
    }
    
    @Test
    public void doSimpleAlternative() {
        ScoredAlternative alternative = new ScoredAlternative(Vehicle.TAXI, 2, Vehicle.START_SQUARE, 0, 150); 
        alternative.doMove(game);
        assertEquals(2,game.target);
        assertEquals(Vehicle.TAXI, game.vehicle);
    }
    

    @Test
    public void doDoubleAlternative() {
        ScoredAlternative alternative = new ScoredAlternative(Vehicle.TAXI, 3, Vehicle.BUS, 5, 150);
        alternative.doMove(game);
        assertEquals(5,game.target);
        assertEquals(Vehicle.BUS, game.vehicle);
    }
    
    public MockGame game;
    public GameBoard gameboard;
    
    public class MockGame extends Game {
        
        public MockGame() {
            super(null);
        }
        
        @Override
        public GameStatus doMove(int player, int square, Vehicle vehicle, boolean doubled) {
            this.target = square;
            this.vehicle = vehicle;
            return Game.GameStatus.RUNNING;
        }
        
        public int target;
        public Vehicle vehicle = Vehicle.START_SQUARE;                                        
        
    }
}
