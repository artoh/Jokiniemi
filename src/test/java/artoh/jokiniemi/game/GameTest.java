/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

import artoh.jokiniemi.ai.AIInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class GameTest {
    
    public GameTest() {
    }
    
    
    @Before
    public void setUp() {
        StartPlaceInterface starter = new MockStarter();
        AIInterface ai = new MockAI();
        
        game = new Game(starter);
        game.log().init(5);
        game.startGame(3, ai);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void detectivesCount() {
        assertEquals(3, game.detectives());        
    }
    
    @Test
    public void bobbiesCount() {
        assertEquals(2, game.bobbies());
    }

    
   @Test
   public void detectivesWin() {
       int blacks = game.blackCardsLeft();       
       
       game.doMove(1, 11, Vehicle.BUS, false);
       game.doMove(2, 21, Vehicle.UNDERGROUD, false);
       game.doMove(3, 31, Vehicle.TAXI, false);
       
       assertEquals( blacks - 1, game.blackCardsLeft());
       assertEquals( 52, game.log().currentPosition(0));
       assertEquals( Game.GameStatus.RUNNING, game.gameStatus());
       
       game.doMove(1, 52, Vehicle.TAXI, false);
       assertEquals( Game.GameStatus.DETECTIVES_WIN, game.gameStatus());       
   }

   @Test
   public void mrXWins() {
       int doubles = game.doubleCardsLeft();
       
       for (int i=0; i < 3; i++) {
           assertEquals(Game.GameStatus.RUNNING,  game.doMove(1, i+10, Vehicle.TAXI, false));           
           game.doMove(2, i+20, Vehicle.TAXI, false);
           game.doMove(3, i+30, Vehicle.TAXI, false);
       }
       assertEquals(doubles-1, game.doubleCardsLeft());
       assertEquals(Game.GameStatus.MRX_WINS, game.gameStatus());
       
   }
   
   @Test
   public void invalidTicketCountZero() {
       assertEquals(0, game.ticketsLeft(Vehicle.START_SQUARE));
   }
   
   @Test
   public void ticketsDecrease() {
       assertEquals(Game.TAXI_TICKETS_TOTAL, game.ticketsLeft(Vehicle.TAXI));
       game.doMove(3, 11, Vehicle.TAXI, false);
       assertEquals(Game.TAXI_TICKETS_TOTAL - 1, game.ticketsLeft(Vehicle.TAXI));       
   }
   
   @Test
   public void bobbysMoveDontDecreaseTickets() {
       assertEquals(Game.UNDERGROUD_TICKETS_TOTAL, game.ticketsLeft(Vehicle.UNDERGROUD));
       game.doMove(1, 11, Vehicle.UNDERGROUD, false);
       assertEquals(Game.UNDERGROUD_TICKETS_TOTAL, game.ticketsLeft(Vehicle.UNDERGROUD));              
   }
   
   @Test
   public void goingBackIsFree() {
       System.out.println(game.log().currentPosition(3));
       assertEquals(Game.BUS_TICKETS_TOTAL, game.ticketsLeft(Vehicle.BUS));
       game.doMove(3, 11, Vehicle.BUS, false);
       game.doMove(1, 41, Vehicle.UNDERGROUD, false);
       game.doMove(2, 61, Vehicle.TAXI, false);
       assertEquals(Game.BUS_TICKETS_TOTAL - 1, game.ticketsLeft(Vehicle.BUS));
       game.doMove(3, 2, Vehicle.BUS, false);
       assertEquals(Game.BUS_TICKETS_TOTAL - 1, game.ticketsLeft(Vehicle.BUS));       
   }
   
   @Test
   public void undergroudTicketsDecrease() {
       assertEquals(Game.UNDERGROUD_TICKETS_TOTAL, game.ticketsLeft(Vehicle.UNDERGROUD));       
       game.doMove(3, 11, Vehicle.UNDERGROUD, false);
       assertEquals(Game.TAXI_TICKETS_TOTAL, game.ticketsLeft(Vehicle.TAXI));
       assertEquals(Game.UNDERGROUD_TICKETS_TOTAL - 1, game.ticketsLeft(Vehicle.UNDERGROUD));                
   }
    
    public class MockStarter implements StartPlaceInterface {

        @Override
        public int startNewGameAndGetStartPlaceForMisterX() {
            return 50;
        }

        @Override
        public int getStartPlaceForDetective() {
            return detective++;
        }
        
        private int detective;
        
    }
    
    public class MockAI implements AIInterface {

        @Override
        public void startGame(Game game) {
            turn = 0;
        }

        @Override
        public void doAITurn() {
            switch(turn) {
                case 0:
                    game.doMove(0, 51, Vehicle.TAXI, false);
                    break;
                case 1:
                    game.doMove(0, 52, Vehicle.BLACK_CARD, false);
                    break;
                case 2:
                    game.doMove(0, 53, Vehicle.TAXI, true);
                    game.doMove(0, 54, Vehicle.UNDERGROUD, false);
                    break;
                case 4:
                    game.doMove(0, 55, Vehicle.TAXI, false);
                    break;
            }
            turn++;
        }
        
        private int turn;
    }
    
    private Game game;
    
}
