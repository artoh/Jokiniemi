/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Storing and selecting between possible moves
 * 
 * @author arto
 */
public class ScoredAlternativesArray {

    /**
     * Constructor
     */
    public ScoredAlternativesArray() {
        this.totalScores = 0;
        this.bestScores = 0;
        this.alternativeCount = 0;
        this.bestAlternative = null;
        this.alternatives = new ScoredAlternative[INITIAL_CAPACITY];
    }
    
    /**
     * Add a possible turn
     * 
     * @param firstTicket Ticket of first (or only) journey
     * @param firstTarget Target of first (or only) journey
     * @param secondTicket Ticker of second journey is the turn is doubles
     * @param secondTarget Target of second journey is the turn is doubles
     * @param score Scores to this alternative, higher scores are better
     */
    public void add(Vehicle firstTicket, int firstTarget, 
                Vehicle secondTicket, int secondTarget, int score) {
        
        if (this.alternativeCount == this.alternatives.length) {
            ScoredAlternative[] newArray = new ScoredAlternative[this.alternativeCount*2];
            for (int i=0; i < this.alternativeCount; i++) {
                newArray[i] = alternatives[i];
            }
            alternatives = newArray;
        }
        this.alternatives[alternativeCount] = 
                new ScoredAlternative(firstTicket, firstTarget, 
                                      secondTicket, secondTarget, score);
        this.alternativeCount++;
        this.totalScores += score;
        this.bestScores = score > this.bestScores ? score : this.bestScores;
        this.bestAlternative = score == bestScores 
                ? new ScoredAlternative(firstTicket, firstTarget, secondTicket, secondTarget, score) 
                : this.bestAlternative;
        
        System.out.println(" -  \t" + score + " \t " + firstTarget + " \t " +  secondTarget + " \t " + firstTicket);
    }
    
    /**
     * Make next move
     * 
     * The scores means the possibilities of the possible moves.
     * If a move have hight scores, it has hight possibilities to be selected.
     * This function select next move and make it.
     * 
     * @param game Game object
     * @param set Possible Locations Set to 
     * @param randomizer 
     */
    public void doMove(Game game, PossibleLocationsSet set, RandomizeInterface randomizer) {
        if (totalScores == 0) {
            alternatives[0].doMove(game, set);            
        } else {
            for (int i=0; i < this.alternativeCount; i++) {
                // Clean worses!
                if (this.alternatives[i].score() < this.bestScores * 3 / 4) {
                    this.totalScores -= this.alternatives[i].score();
                    this.alternatives[i].cleanAsWorse();
                }                
            }
            int random = randomizer.next(totalScores) - 1;
            int sumScores = 0;
            
            for (int i=0; i < this.alternativeCount; i++) {
                sumScores += this.alternatives[i].score();
                if (sumScores >= random && this.alternatives[i].score() > 0) {
                    this.alternatives[i].doMove(game, set);
                    System.out.println("RAND " + random + " @ " + i);
                    break;
                }
            }
        }
    }
    
    public void doBestMove(Game game, PossibleLocationsSet set) {
        this.bestAlternative.doMove(game, set);
    }

    /**
     *  Inner class for storing alternatives
     * 
     *  For private use by ScoredAlternativeArray only
     *  (The class is visible for testing purposes!)
     */
    class ScoredAlternative {
        
        public ScoredAlternative(Vehicle firstTicket, int firstTarget, 
                Vehicle secondTicket, int secondTarget, int score) {
            this.firstTicket = firstTicket;
            this.firstTarget = firstTarget;
            this.secondTicket = secondTicket;
            this.secondTarget = secondTarget;
            this.score = score;
        }
                
        /**
         * Do this move. Called by ScoredAlternativeArray.doMove()
         * if this alternative is selected.
         * 
         * @param game
         * @param set 
         */
        public void doMove(Game game, PossibleLocationsSet set) {
            game.doMove(0, this.firstTarget, this.firstTicket, this.secondTarget > 0);
            if( game.log().isVisibleTurn(game.log().currentTurn())) {
                set.init(firstTarget);
            } else {
                set = set.nextSet(this.firstTicket, true);
            }
            if (this.secondTarget > 0) {
                game.doMove(0, this.secondTarget, this.secondTicket, false);
                if( game.log().isVisibleTurn(game.log().currentTurn())) {
                    set.init(secondTarget);
                } else {
                    set = set.nextSet(this.secondTicket, true);
                }                
            }
            System.out.println("---> " + firstTicket + " " + firstTarget + " ---> " + secondTarget);
        }
        
        public void cleanAsWorse() {
            this.score = 0;
        }
        
        /**
         * Score (or weight) of this alternative
         * @return 
         */
        public int score() {
            return this.score;
        }
        
        private Vehicle firstTicket;
        private int firstTarget;
        private Vehicle secondTicket;
        private int secondTarget;
        
        private int score;
        
    }

    private int totalScores;
    private int bestScores;
    private ScoredAlternative bestAlternative;
    private ScoredAlternative[] alternatives;
    private int alternativeCount;
    
    private static int INITIAL_CAPACITY = 25;
    
}
