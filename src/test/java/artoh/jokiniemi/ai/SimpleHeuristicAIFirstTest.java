/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.StartPlaceInterface;
import artoh.jokiniemi.game.Vehicle;
import artoh.jokiniemi.struct.PossibleLocationsSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class SimpleHeuristicAIFirstTest {
    
    public SimpleHeuristicAIFirstTest() {
    }
    
    @Before
    public void setUp() {
        
        starter = new MockStarter();
        game = new MockGame(starter);
        
        game.log().init(5);
        game.log().setVisibleTurn(3);
        game.gameBoard().init(12);
        
        game.gameBoard().addConnection(Vehicle.TAXI, 1, 2);
        game.gameBoard().addConnection(Vehicle.TAXI, 1, 3);
        game.gameBoard().addConnection(Vehicle.TAXI, 2, 5);
        game.gameBoard().addConnection(Vehicle.BUS, 2, 4);
        game.gameBoard().addConnection(Vehicle.BUS, 3, 4);
        game.gameBoard().addConnection(Vehicle.TAXI, 3, 7);
        game.gameBoard().addConnection(Vehicle.TAXI, 3, 9);
        game.gameBoard().addConnection(Vehicle.BUS, 3, 9);
        game.gameBoard().addConnection(Vehicle.TAXI, 4, 6);
        game.gameBoard().addConnection(Vehicle.UNDERGROUD, 4, 7);
        game.gameBoard().addConnection(Vehicle.TAXI, 5, 6);
        game.gameBoard().addConnection(Vehicle.TAXI, 6, 11);
        game.gameBoard().addConnection(Vehicle.FERRY, 8, 12);
        game.gameBoard().addConnection(Vehicle.TAXI, 7, 8);
        game.gameBoard().addConnection(Vehicle.TAXI, 8, 9);
        game.gameBoard().addConnection(Vehicle.TAXI, 8, 10);
        game.gameBoard().addConnection(Vehicle.TAXI, 9, 10);
        game.gameBoard().addConnection(Vehicle.TAXI, 11, 12);
        
        
        FloydWarshallDistance distancer = new FloydWarshallDistance();        
        ai = new SimpleHeuristicAI(distancer);
        distancer.init(game.gameBoard());

        game.log().newGame(1, starter);
        ai.startGame(game);    
        ai.beginTurn(1, 0);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void nearestDetectiveAtStart() {
        assertEquals(0, ai.nearestDetectiveDistance(10));
        assertEquals(1, ai.nearestDetectiveDistance(8));
        assertEquals(1, ai.nearestDetectiveDistance(9));
        assertEquals(2, ai.nearestDetectiveDistance(3));
        assertEquals(2, ai.nearestDetectiveDistance(7));
        assertEquals(3, ai.nearestDetectiveDistance(1));
        assertEquals(3, ai.nearestDetectiveDistance(4));
        assertEquals(4, ai.nearestDetectiveDistance(6));
        assertEquals(4, ai.nearestDetectiveDistance(2));
        assertEquals(5, ai.nearestDetectiveDistance(5));
    }

    @Test
    public void jumPointsAt5() {
        assertEquals(350, ai.jumPoints(2));
    }
    
    @Test
    public void safeAt2() {
        assertTrue(ai.isSafe(2, 3, 1));
    }
    
    @Test
    public void notSafeAt3() {
        assertFalse(ai.isSafe(3, 2, 2));
    }
    
    @Test
    public void connectionVehiclePoints() {
        assertEquals(3, ai.connectionVehiclePoints(6));
        assertEquals(10, ai.connectionVehiclePoints(4));
        assertEquals(3, ai.connectionVehiclePoints(9));
        assertEquals(9, ai.connectionVehiclePoints(12));
    }
    
    @Test
    public void notInTrouble() {
        assertFalse(ai.isInTrouble(1));
    }
    
    @Test
    public void inTrouble() {
        assertTrue(ai.isInTrouble(9));
        assertTrue(ai.isInTrouble(8));
    }
    
    @Test
    public void blackScaleInBegin() {
        assertEquals(10, ai.blackCardScale(30, Vehicle.TAXI));
    }
    
    @Test
    public void doubleScaleInBegin() {
        assertEquals(10, ai.doubleCardScale(40, 1));
    }
    
    @Test
    public void safeLocation() {
        PossibleLocationsSet set = new PossibleLocationsSet(game);
        set.init(1);       
        assertEquals(1000,ai.evaluateMove(Vehicle.TAXI, 2, 3, set));
    }
    
    @Test
    public void moveAndScore() {
        PossibleLocationsSet set = new PossibleLocationsSet(game);
        game.log().logTurn(0, 2, Vehicle.TAXI, false);
        game.log().logTurn(1, 3, Vehicle.TAXI, false);
        ai.beginTurn(2, 1);
        assertTrue( ai.evaluateMove(Vehicle.TAXI, 5, 1, set) > ai.evaluateMove(Vehicle.TAXI, 1, 1, set));
    }
    
    @Test
    public void doAiMove() {
        game.log().logTurn(0, 2, Vehicle.TAXI, false);
        game.log().logTurn(1, 3, Vehicle.TAXI, false);
        ai.doAITurn();
        assertEquals(5, game.log().currentPosition(0));
    }
    
    @Test
    public void aiDoublesInTrouble() {
        game.log().logTurn(0, 1, Vehicle.TAXI, false);
        game.log().logTurn(1, 4, Vehicle.UNDERGROUD, false);
        ai.doAITurn();
        assertEquals(5, game.log().currentPosition(0));
    }
    
    @Test
    public void playAllMovesAndWin() {
        game.startGame(1, ai);
        assertEquals(Game.GameStatus.RUNNING, game.gameStatus());
        game.doMove(1, 9, Vehicle.TAXI, false);
        game.doMove(1, 3, Vehicle.TAXI, false);
        game.doMove(1, 4, Vehicle.TAXI, false);
        game.doMove(1, 6, Vehicle.TAXI, false);
        assertEquals(Game.GameStatus.MRX_WINS, game.gameStatus());
    }
    
    public class MockGame extends Game {
        
        public MockGame(StartPlaceInterface startPlacer) {
            super(startPlacer);
        }
        
        @Override
        public int detectives() {
            return 1;
        }
        
        @Override
        public int blackCardsLeft() {
            return 1;
        }
        
        @Override
        public int doubleCardsLeft() {
            return 1;
        }
        
    }
    
    public class MockStarter implements StartPlaceInterface {

        @Override
        public int startNewGameAndGetStartPlaceForMisterX() {
            return 1;
        }

        @Override
        public int getStartPlaceForDetective() {
            return 10;
        }
        
    }    
    
    private Game game;
    private MockStarter starter;
    private SimpleHeuristicAI ai;
    
}
