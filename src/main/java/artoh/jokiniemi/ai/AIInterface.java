/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ai;

import artoh.jokiniemi.game.Game;

/**
 * Interface for Game AI
 * 
 * @author ahyvatti
 */
public interface AIInterface {
    
    /**
     * Called when start a new gmae
     */
    public void startGame(Game game);
    
    /**
     * Called to do the turn of AI
     * 
     * AI will call Game.doMove() 
     * When using the double card, AI will call Game.goMove() twice
     * 
     */
    public void doAITurn();
    
}
