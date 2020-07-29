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
        game.startGame(2, ai);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void detectivesCount() {
        assertEquals(2, game.detectives());        
    }

    
   @Test
   public void detectivesWin() {
       int blacks = game.blackCardsLeft();       
       
       game.doMove(1, 11, Vehicle.BUS, false);
       game.doMove(2, 21, Vehicle.UNDERGROUD, true);
       
       assertEquals( blacks-1, game.blackCardsLeft());
       assertEquals( 52, game.log().currentPosition(0));
       assertEquals( Game.GameStatus.RUNNING, game.gameStatus());
       
       game.doMove(1, 52, Vehicle.TAXI, false);
       assertEquals( Game.GameStatus.DETECTIVES_WIN, game.gameStatus());       
   }

   @Test
   public void mrXWins() {
       int doubles = game.doubleCardsLeft();
       
       for (int i=0; i < 2; i++) {
           assertEquals(Game.GameStatus.RUNNING,  game.doMove(1, i+10, Vehicle.TAXI, false));           
           game.doMove(2, i+20, Vehicle.TAXI, false);
       }
       assertEquals(doubles-1, game.doubleCardsLeft());
       assertEquals(Game.GameStatus.MRX_WINS, game.gameStatus());
       
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
                case 4:
                    game.doMove(0, 55, Vehicle.TAXI, false);
            }
            turn++;
        }
        
        private int turn;
    }
    
    private Game game;
    
}
