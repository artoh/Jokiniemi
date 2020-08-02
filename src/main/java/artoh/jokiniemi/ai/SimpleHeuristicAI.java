/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.game.Game;

/**
 *
 * @author arto
 */
public class SimpleHeuristicAI implements AIInterface {

    public SimpleHeuristicAI(BoardDistanceInterface boardDistances) {
        this.boardDistances = boardDistances;
    }
    
    @Override
    public void startGame(Game game) {
        this.game = game;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doAITurn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int nearestDetectiveDistance(int square) {
        int myposition = game.log().currentPosition(0);
        int nearest = Integer.MAX_VALUE;
        
        for (int i = 1; i <= game.detectives(); i++) {
            int distance = boardDistances.distance(myposition, game.log().currentPosition(i));
            if (distance < nearest) {
                nearest = distance;
            }
        }
        return nearest;        
    }
    
    private BoardDistanceInterface boardDistances;
    private Game game;
    
}
