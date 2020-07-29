/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.algorithm;

/**
 * Implements Linear congreruential generator algorithm
 * 
 * Algorith uses Numerical Receipes paramters 
 * Source: https://en.wikipedia.org/wiki/Linear_congruential_generator
 * 
 * @author arto
 */
public class LinealCongruentialGenerator implements RandomizeInterface {

    
    public LinealCongruentialGenerator() {
        this.state = System.nanoTime();
    }
    
    @Override
    public int next(int max) {
        
        state = (multiplier * this.state + addition) % modulus;
        int value =  (((int)(1 + this.state)) % max );
        return ( value < 0 ? 0 - value : value);
    }
    
    private long state;
    private final long modulus = 4294967296L;
    private final long multiplier = 1664525L;
    private final long addition = 1013904223L;
    
    
}
