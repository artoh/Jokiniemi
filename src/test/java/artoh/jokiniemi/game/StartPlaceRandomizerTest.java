/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

import org.junit.Test;
import static org.junit.Assert.*;

import artoh.jokiniemi.algorithm.RandomizeInterface;

/**
 *
 * @author arto
 */
public class StartPlaceRandomizerTest {
    
    public StartPlaceRandomizerTest() {
    }

    @Test
    public void smallTestWithMock() {
        int indexes[] = {2,2,3};
        RandomizeInterface mock = new MockRandomizer(indexes);
        StartPlaceRandomizer starter = new StartPlaceRandomizer(mock);
        
        starter.addStartPlace(true, 100);
        starter.addStartPlace(true, 200);
        starter.addStartPlace(true, 300);
        
        starter.addStartPlace(false, 400);
        starter.addStartPlace(false, 500);
        starter.addStartPlace(false, 600);
        starter.addStartPlace(false, 700);
        
        assertEquals(200, starter.startNewGameAndGetStartPlaceForMisterX());
        assertEquals(500, starter.getStartPlaceForDetective());
        assertEquals(700, starter.getStartPlaceForDetective());
        
    }
    
    @Test
    public void lastIndexesWithMock() {
        int indexes[] = {10,10,9};
        RandomizeInterface mock = new MockRandomizer(indexes);
        StartPlaceRandomizer starter = new StartPlaceRandomizer(mock);        
        
        for(int i=0;i<10;i++) {
            starter.addStartPlace(true, i);
            starter.addStartPlace(false, i);
        }
        
        assertEquals(9, starter.startNewGameAndGetStartPlaceForMisterX());
        assertEquals(9, starter.getStartPlaceForDetective());
        assertEquals(8, starter.getStartPlaceForDetective());
    }
    
    
    private class MockRandomizer implements RandomizeInterface {

        public MockRandomizer(int[] values) {
            this.values = values;
            this.current = 0;
        }
        
        @Override
        public int next(int max) {            
            return this.values[current++];            
        }
        
        private int[] values;
        private int current;
        
    }
}
