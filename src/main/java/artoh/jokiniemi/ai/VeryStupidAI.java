/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.algorithm.IntegerArray;
import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Stub AI using nonsense randomize function to do the moves of Mister X
 * 
 * @author arto
 */
public class VeryStupidAI implements AIInterface {

    public VeryStupidAI(RandomizeInterface randomizer) {
        this.randomizer = randomizer;
    }
    
    @Override
    public void startGame(Game game) {
        this.game = game;
    }

    @Override
    public void doAITurn() {
        int currentPosition = game.log().currentPosition(0);
        
        IntegerArray possibleIndexes = new IntegerArray(10);
        
        for (int i = 0; i < game.gameBoard().connectionsCount(currentPosition); i++) {
            // Stupid can't use the ferry
            // Stupid is not a idiot and dosn't go to the same square where 
            // detective stands.
            if (game.gameBoard().connectionVehicle(currentPosition, i) != Vehicle.FERRY &&
                !isDetectivePresent(game.gameBoard().connectionTo(currentPosition, i))) {
                possibleIndexes.push(i);
            }
        }
        
        int index = possibleIndexes.count() == 0 ? 0 : randomizer.next(possibleIndexes.count()) - 1;
        
        game.doMove(0, game.gameBoard().connectionTo(currentPosition, index), 
                game.gameBoard().connectionVehicle(currentPosition, index), true);        
    }
    
    /**
     * Returns true if there are a detective in the square
     * @param square Number of square
     * @return True if detective is present
     */
    public boolean isDetectivePresent(int square) {
        for (int i = 1; i < game.detectives() + 1; i++) {
            if (game.log().currentPosition(i) == square) {
                return true;
            }
        }
        return false;
    }
    
    
    private Game game;
    private final RandomizeInterface randomizer;
    
}
