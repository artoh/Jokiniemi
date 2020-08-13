
package artoh.jokiniemi.algorithm;

/**
 * Toteuttaa Linear congreruential generator -algoritmin satunnaislukujen
 * tuottamiseen
 * 
 * Algoritmi käyttää Numeric Receipesin parametreja
 * Lähde: https://en.wikipedia.org/wiki/Linear_congruential_generator
 * 
 * @author arto
 */
public class LinearCongruentialGenerator implements RandomizeInterface {

    
    public LinearCongruentialGenerator() {
        this.state = System.nanoTime() % modulus;
    }
    
    @Override
    public int next(int max) {
        
        state = (multiplier * this.state + addition) % modulus;
        int value = ((((int) this.state)) % max);
        return ((value < 0 ? 0 - value : value) + 1);
    }
    
    private long state;
    private final long modulus = 4294967296L;
    private final long multiplier = 1664525L;
    private final long addition = 1013904223L;
    
    
}
