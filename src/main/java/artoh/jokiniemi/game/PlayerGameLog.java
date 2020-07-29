/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

/**
 * Game log for a single player
 * 
 * @author arto
 */
public class PlayerGameLog {
    
    /**
     * Construct a game log for a player
     * 
     * @param turns Turns in game 
     * @param initialPosition Initial position, number of start square
     */
    public PlayerGameLog(int turns, int initialPosition) {
        this.turnsPlayed = 0;
        
        this.squares = new int[turns + 1];
        this.vehicles = new int[turns + 1];
        
        this.squares[0] = initialPosition;
        this.vehicles[0] = Vehicle.START_SQUARE.ordinal();
    }
    
    /**
     * Add a turn into the log
     * 
     * @param square Number of square of the new position
     * @param vehicle Vechicle used to go to this position
     * @return Turns played
     */
    public int addTurn(int square, Vehicle vehicle) {
        
        this.turnsPlayed++;
        this.squares[this.turnsPlayed] = square;
        this.vehicles[this.turnsPlayed] = vehicle.ordinal();
        return this.turnsPlayed;
    }
    
    /**
     * Postion of player in turn n
     * @param turn Turn index
     * @return Number of square
     */
    public int position(int turn) {
        return this.squares[turn];
    }
    
    /**
     * Vehicle of player in turn n
     * @param turn Turn index
     * @return Vehicle used by player on turn n
     */
    public Vehicle vehicle(int turn) {
        int ordinal = this.vehicles[turn];
        
        if (ordinal == Vehicle.TAXI.ordinal()) {
            return Vehicle.TAXI;
        } else if (ordinal == Vehicle.BUS.ordinal()) {
            return Vehicle.BUS;
        } else if (ordinal == Vehicle.UNDERGROUD.ordinal()) {
            return Vehicle.UNDERGROUD;
        } else if (ordinal == Vehicle.BLACK_CARD.ordinal()) {
            return Vehicle.BLACK_CARD;
        } else if (ordinal == Vehicle.DOUBLED.ordinal()) {
            return Vehicle.DOUBLED;
        } else {
            return Vehicle.START_SQUARE;
        }
    }
            
    /**
     * Current positiof of playes
     * @return Number of square
     */
    public int currentPosition() {
        return squares[turnsPlayed];
    }
    
    /**
     * Turns playes
     * @return Number of turns played
     */
    public int turn() {
        return turnsPlayed;
    }
    
    private int turnsPlayed;
    private int squares[];
    private int vehicles[];
    
}
