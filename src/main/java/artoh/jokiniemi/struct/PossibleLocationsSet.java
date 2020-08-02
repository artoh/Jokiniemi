/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Set for possible location of Mister X
 * 
 * AI keeps list of possible locations of Mister X
 * (squares to where it is possible to go from squre where
 * Mister X was visible, using the tickets Mr X has used).
 * 
 * @author arto
 */
public class PossibleLocationsSet {
    
    /**
     * Constructor
     * 
     * @param game Game object
     */
    public PossibleLocationsSet(Game game) {
        this.game = game;               
        this.possible = new boolean[game.gameBoard().squareCount() + 1];
    }
    
    /**
     * When Mr X is visible, init this object with this square
     * 
     * @param square Square of visible Mister X
     */
    public void init(int square) {
        possible[square] = true;
    }
    
    /**
     * Remove locations of detectives. Needed to update this set
     * after move of detectives because of it is impossible to have
     * Mister X with same location than some detective has.
     */
    public void removeDetectiveLocations() {
        for (int i = 1; i <= game.detectives(); i++) {
            int location = game.log().currentPosition(i);
            if (possible[location]) {
                possible[location] = false;
            }
        }
    }
    
    /**
     * Count of possible squares of Mister X
     * @return Count of squares
     */
    public int count() {
        int count = 0;
        for (int i = 1; i < possible.length; i++) {
            if (possible[i] == true) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Construct a new Possible Location Set including the possible locations
     * of Mister X after moving with given ticket type.
     * 
     * @param vehicle Vehicle ti use
     * @param cleanDetectiveLocations Remove locations of detectives from this set
     * @return New set including possible locations
     */
    public PossibleLocationsSet nextSet(Vehicle vehicle, boolean cleanDetectiveLocations) {
        
        PossibleLocationsSet newSet = new PossibleLocationsSet(game);
                
        for (int square = 1; square < possible.length; square++) {
            if (possible[square]) {
                for (int c = 0; c < game.gameBoard().connectionsCount(square); c++) {
                    if (vehicle == Vehicle.BLACK_CARD ||
                        vehicle == game.gameBoard().connectionVehicle(square, c)) {
                        newSet.possible[game.gameBoard().connectionTo(square, c)] = true;
                    }
                }
            }
        }
        if (cleanDetectiveLocations) {
            newSet.removeDetectiveLocations();
        }
        
        return newSet;
    }
    
    private final Game game;
    private final boolean[] possible;
}
