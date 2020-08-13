/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.io;

import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.GameBoard;
import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.GameLog;
import artoh.jokiniemi.game.StartPlaceRandomizer;
import artoh.jokiniemi.game.StartPlaceRandomizerTest.MockRandomizer;
import artoh.jokiniemi.game.Vehicle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class BoardFileReaderTest {
    
    public BoardFileReaderTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void canReadFile() {
        BoardFileReader reader = new BoardFileReader();

        int indexes[] = {1,1,1};
        MockRandomizer mock = new MockRandomizer(indexes);
        StartPlaceRandomizer starter = new StartPlaceRandomizer(mock);
        
        Game game = new Game(starter);       
        
        assertTrue( reader.readBoard(game, starter));
        
        assertEquals(199, game.gameBoard().squareCount());
        
        assertEquals(25, game.log().turnsTotal());
        
        game.log().newGame(1, game.startPlacer());
        assertEquals(71, game.log().currentPosition(0));
        assertEquals(123, game.log().currentPosition(1));
        
        assertEquals(5, game.gameBoard().connectionsCount(199));
        
    }
    
    @Test
    public void parseTurnTable() {
        GameLog log = new GameLog();
        String table="S--X--X--X";
        BoardFileReader reader = new BoardFileReader();
        reader.initTurnTable(log, table);
        
        assertEquals(10, log.turnsTotal());
        assertTrue( log.isVisibleTurn(3));
        assertFalse( log.isVisibleTurn(5));        
    }
    
    @Test
    public void startLists() {
        int indexes[] = {2,4,1};
        MockRandomizer mock = new MockRandomizer(indexes);
        StartPlaceRandomizer starter = new StartPlaceRandomizer(mock);
        BoardFileReader reader = new BoardFileReader();
        reader.initStartList(starter, "X13,87,125,685");        
        reader.initStartList(starter, "D8,65,125,185,199");
        
        assertEquals(87, starter.startNewGameAndGetStartPlaceForMisterX());
                
        assertEquals(185, starter.getStartPlaceForDetective());
        assertEquals(8, starter.getStartPlaceForDetective());
    }
    
    @Test
    public void addConnections() {
        GameBoardInterface board = new GameBoard();
        board.init(10);
        BoardFileReader reader = new BoardFileReader();
        
        reader.addConnectionLine(board, "1T2B3M4F5");
        
        assertEquals(4, board.connectionsCount(1));
        assertEquals(1, board.connectionsCount(2));
        assertEquals(1, board.connectionTo(2, 0));
        assertEquals(Vehicle.FERRY, board.connectionVehicle(5, 0));
        assertEquals(Vehicle.UNDERGROUD, board.connectionVehicle(4, 0));
    }
    
}
