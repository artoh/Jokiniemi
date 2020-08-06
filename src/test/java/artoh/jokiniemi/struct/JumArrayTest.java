/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.StartPlaceRandomizer;
import artoh.jokiniemi.io.BoardFileReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class JumArrayTest {
    
    public JumArrayTest() {
    }
    
    @Before
    public void setUp() {
        BoardFileReader reader = new BoardFileReader();
        StartPlaceRandomizer starter = new StartPlaceRandomizer(null);
        Game game = new Game(starter);
        reader.readBoard(game, starter);        
        
        jumArray = new JumArray(game.gameBoard());
    }
    
    @Test
    public void jumsAt17() {
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(17);
        
        assertEquals(2, jumSquare.count(1));
        assertEquals(4, jumSquare.count(2));
    }

    @Test
    public void jumsAt18() {
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(18);        
        assertEquals(2, jumSquare.count(1));        
        assertEquals(3, jumSquare.count(2));
        
        assertTrue( jumSquare.square(2, 0) == 1 ||
                jumSquare.square(2, 1) == 1 ||
                jumSquare.square(2, 2) == 1);
    }

    @Test
    public void jumsAt193() {
        JumArray.JumSquare jumSquare = jumArray.getJumSquare(193);
        
        assertEquals(2, jumSquare.count(1));
        assertEquals(5, jumSquare.count(2));
    }

    
    
    JumArray jumArray;
}
