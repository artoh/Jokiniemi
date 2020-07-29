/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.game;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.algorithm.RandomizeInterface;

/**
 *
 * @author ahyvatti
 */
public class Game {

    
    public Game(RandomizeInterface randomizer) {
        this.randomizer = randomizer;
        this.gameLog = new GameLog();
        this.gameboard = null;
    }    
    
    public enum GameStatus {
        /**
         * Game has not been started
         */
        NOT_STARTED,
        /**
         * Game is running
         */
        RUNNING,
        /**
         * Game has ended when all the turns have been played and Mr X
         * is not catched. Mr X wins the game.
         */
        MRX_WINS,
        /**
         * Detectives have catched Mr X. Detectives win the game.
         */
        DETECTIVES_WIN
    }
    
    /**
     * Start a new game
     * 
     * @param detectives Count of detectives
     * @param ai The AI object
     */
    public void startGame(int detectives, AIInterface ai) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 
     * Do a move
     * 
     * User inferface will call this function with moves of the detectives.
     * When all the detectives have did their moves, the turn ends and
     * Game will call AI and ask the AI to do Mr X's move.
     * 
     * When the game ends, this function returns the status of the game
     * 
     * @param player Player (0: mrX, 1..n detectives)
     * @param square Square to move
     * @param vehicle Vehicle doing the move
     * @param doubled True when mrX make a first half of the double turn
     * @return Status of the game after this move (and move of AI)
     */
    public GameStatus doMove(int player, int square, Vehicle vehicle, boolean doubled) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Amount of Double Cards Mr X have
     * @return Amount of cards
     */
    public int doubleCardsLeft() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Amount of Black Tickets Mr X have
     * @return amount of tickets
     */
    public int blackCardsLeft() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * The Game Log object
     * @return Game log
     */
    public GameLog log() {
        return this.gameLog;
    }
    
    /**
     * The Randomizer object
     * @return Randomizer object
     */
    public RandomizeInterface randomizer() {
        return this.randomizer;
    }
    
    /**
     * The Game Board object
     * @return Game board object
     */
    public GameBoardInterface gameBoard() {
        return this.gameboard;
    }
    
    private final GameLog gameLog;
    private final RandomizeInterface randomizer;
    private final GameBoardInterface gameboard;

    private final int BLACK_CARDS_TOTAL = 5;
    private final int DOUBLE_CARDS_TOTAL = 2;
    
    private int blackCardsLeft;
    private int doubleCardsLeft;
    
}
