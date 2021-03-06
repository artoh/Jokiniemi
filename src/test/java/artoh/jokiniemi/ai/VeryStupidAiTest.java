/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.StartPlaceInterface;
import artoh.jokiniemi.game.Vehicle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class VeryStupidAiTest {
    
    public VeryStupidAiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        game = new Game(new MockStarter());
        ai = new VeryStupidAI(new LinearCongruentialGenerator());
        
        game.log().init(5);
        game.gameBoard().init(6);
        game.gameBoard().addConnection(Vehicle.TAXI, 1, 2);
        game.gameBoard().addConnection(Vehicle.TAXI, 2, 3);
        game.gameBoard().addConnection(Vehicle.TAXI, 2, 5);
        game.gameBoard().addConnection(Vehicle.TAXI, 2, 6);
        game.gameBoard().addConnection(Vehicle.TAXI, 3, 4);
        game.gameBoard().addConnection(Vehicle.FERRY, 1, 6);
        
    }
        
    @After
    public void tearDown() {
    }
    
    @Test
    public void simpleGame() {
        game.startGame(1, ai);
        
        assertTrue( ai.isDetectivePresent(4));
        assertFalse( ai.isDetectivePresent(3));
        
        assertEquals(2, game.log().currentPosition(0));
        
        game.doMove(1, 3, Vehicle.TAXI, false);
        assertTrue( ai.isDetectivePresent(3));
        assertFalse( ai.isDetectivePresent(4));
        
        assertEquals( Game.GameStatus.RUNNING, game.gameStatus());
        
        game.doMove(1, 2, Vehicle.BUS, false);        
        assertEquals( Game.GameStatus.DETECTIVES_WIN, game.gameStatus());
        
    }

    
    public class MockStarter implements StartPlaceInterface {

        @Override
        public int startNewGameAndGetStartPlaceForMisterX() {
            return 1;
        }

        @Override
        public int getStartPlaceForDetective() {
            return 4;
        }
        
    }
    
    private Game game;
    private VeryStupidAI ai;
}
