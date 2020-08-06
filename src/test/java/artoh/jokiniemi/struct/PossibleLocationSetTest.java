/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.game.Game;
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
public class PossibleLocationSetTest {
    
    public PossibleLocationSetTest() {
    }
    
    @Before
    public void setUp() {
        Game game = new Game(new MockStarter());
        
        game.gameBoard().init(20);
        game.gameBoard().addConnection(Vehicle.TAXI, 1, 2);
        game.gameBoard().addConnection(Vehicle.TAXI, 2, 3);
        game.gameBoard().addConnection(Vehicle.TAXI, 3, 4);
        game.gameBoard().addConnection(Vehicle.TAXI, 4, 5);
        game.gameBoard().addConnection(Vehicle.TAXI, 4, 7);
        game.gameBoard().addConnection(Vehicle.TAXI, 1, 3);
        game.gameBoard().addConnection(Vehicle.TAXI, 5, 7);
        game.gameBoard().addConnection(Vehicle.TAXI, 7, 9);
        game.gameBoard().addConnection(Vehicle.BUS, 1, 4);
        game.gameBoard().addConnection(Vehicle.BUS, 4, 5);
        game.gameBoard().addConnection(Vehicle.BUS, 5, 6);
        game.gameBoard().addConnection(Vehicle.UNDERGROUD, 6, 8);
        game.gameBoard().addConnection(Vehicle.UNDERGROUD, 8, 9);
        game.gameBoard().addConnection(Vehicle.FERRY, 1, 9);

        game.log().init(10);       
        game.startGame(2, new MockAI());
                
        set = new PossibleLocationsSet(game);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void oneSquareAfterInitialization() {
        set.init(5);
        assertEquals(1, set.count());
    }
    
    @Test
    public void zeroSquaresAfterCleaning() {
        set.init(5);
        set.removeDetectiveLocations();
        assertEquals(0, set.count());
    }    
    
    @Test
    public void movingWithTaxi() {
        set.init(4);     
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, false);
        assertEquals(3, newSet.count());
    }
    
    @Test
    public void movingWithTaxiCleaningDetectives() {
        set.init(4);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, true);
        assertEquals(2, newSet.count());
    }
    
    @Test
    public void movingWithBus() {
        set.init(4);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.BUS, false);
        assertEquals(2, newSet.count());
    }
    
    @Test
    public void movingWithBlack() {
        set.init(9);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.BLACK_CARD, false);
        assertEquals(3, newSet.count());
    }
    
    @Test
    public void moreMoves() {
        set.init(4);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, true);
        assertEquals(2, newSet.count());
        PossibleLocationsSet newerSet = newSet.nextSet(Vehicle.TAXI, false);
        assertEquals(5, newerSet.count());
    }
    
    @Test
    public void onlyTaxi3() {
        set.init(3);
        assertTrue(set.onlyTaxiPossible());
    }
    
    @Test 
    public void busToo() {
        set.init(3);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, true);
        assertFalse(newSet.onlyTaxiPossible());        
    }
    
    @Test
    public void ferry2() {
        set.init(2);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, true);
        assertTrue(newSet.ferryPossible());
    }
    
    @Test
    public void dontFerry6() {
        set.init(6);
        PossibleLocationsSet newSet = set.nextSet(Vehicle.TAXI, true);
        assertFalse(newSet.ferryPossible());        
    }
    
    @Test
    public void allAvailableAfterFill() {
        set.fill();
        assertEquals(20, set.count());
    }

    public class MockStarter implements StartPlaceInterface {

        @Override
        public int startNewGameAndGetStartPlaceForMisterX() {
            return 1;
        }

        @Override
        public int getStartPlaceForDetective() {
            return 5;
        }
        
        private int detective;
        
    }
    
    public class MockAI implements AIInterface {

        @Override
        public void startGame(Game game) {
        }

        @Override
        public void doAITurn() {
        }
        
        private int turn;
    }    
    
    private PossibleLocationsSet set;
}
