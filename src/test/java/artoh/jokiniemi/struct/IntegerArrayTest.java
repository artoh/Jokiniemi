/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.struct.IntegerArray;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class IntegerArrayTest {
    
    public IntegerArrayTest() {
    }

    @Test
    public void arrayTestWith1024Integers() {
        IntegerArray array = new IntegerArray(8);
        
        for(int i=0; i<1024;i++) {
            array.push(i);
        }
        
        assertEquals(1024, array.count());
        
        for(int i=0; i<1024;i++) {
            assertEquals(i, array.at(i));
        }        
    }
    
}
