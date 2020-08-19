/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.algorithm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class AppliedFloydWarshallDistanceTest extends BoardDistanceTest {
    
    public AppliedFloydWarshallDistanceTest() {
    }

    @Override
    public BoardDistanceInterface createInstance() {
        return new AppliedFloydWarshallDistance();
    }
    
    @Test
    public void indexesForTriangle() {
        AppliedFloydWarshallDistance di = AppliedFloydWarshallDistance.class.cast(initSimple());
        assertEquals(0, di.index(2, 1));
        assertEquals(2, di.index(3, 2));
        assertEquals(8, di.index(5, 3));
    }
    
}
