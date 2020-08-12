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
public class TicketAwareDistanceTest extends BoardDistanceTest {
    
    public TicketAwareDistanceTest() {
    }

    @Override
    public BoardDistanceInterface createInstance() {
        return new TicketAwareDistance();
    }
    
    @Test
    public void taxiOnlyDistances() {
        TicketAwareDistance di = TicketAwareDistance.class.cast(initSimple());
        
        assertEquals(6, di.distanceWithTickets(1, 8, 8, 0, 0, 4, 10));
        assertEquals(4, di.distanceWithTickets(4, 8, 5, 0, 0, 4, 10));
        assertEquals(Integer.MAX_VALUE, di.distanceWithTickets(3, 7, 2, 0, 0, 4, 10));
    }
    
    @Test
    public void combinedDistances() {
        TicketAwareDistance di = TicketAwareDistance.class.cast(initSimple());
        assertEquals(3, di.distanceWithTickets(5, 7, 1, 1, 1, 4, 10));
        assertEquals(2, di.distanceWithTickets(1, 3, 1, 1, 1, 4, 10));
        assertEquals(Integer.MAX_VALUE, di.distanceWithTickets(1, 2, 0, 100, 100, 4, 10));
    }
    
    @Test
    public void realDistancesWithTickets() {
        TicketAwareDistance di = TicketAwareDistance.class.cast(initReal());
        assertEquals(3, di.distanceWithTickets(163, 123, 8, 0, 0, 4, 25));
        assertEquals(5, di.distanceWithTickets(165, 79, 2, 3, 0, 4, 25));
        assertEquals(Integer.MAX_VALUE, di.distanceWithTickets(1, 173, 0, 100, 100, 4, 15));
    }
}
