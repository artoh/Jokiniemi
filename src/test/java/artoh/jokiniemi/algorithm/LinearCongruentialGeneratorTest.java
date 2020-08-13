
package artoh.jokiniemi.algorithm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for random generator
 * 
 * @author arto
 */
public class LinearCongruentialGeneratorTest {
    
    public LinearCongruentialGeneratorTest() {
    }

    @Test
    public void randomBetweenOneAndMax() {
        RandomizeInterface randomizer = new LinearCongruentialGenerator();
        for(int i=10; i<100;i++) {
            int random = randomizer.next(i);
            assertTrue( random >= 1);
            assertTrue( random <= i);
        }
    }
    
    @Test
    public void allRandomsNotSame() {
        RandomizeInterface randomizer = new LinearCongruentialGenerator();
        int random1 = randomizer.next(Integer.MAX_VALUE);
        int random2 = randomizer.next(Integer.MAX_VALUE);
        int random3 = randomizer.next(Integer.MAX_VALUE);
        assertFalse( random1 == random2 && random1 == random3);
    }
    
}
