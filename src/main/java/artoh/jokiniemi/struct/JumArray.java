/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.JumAnalyser;
import artoh.jokiniemi.game.GameBoardInterface;

/**
 *
 * @author arto
 */
public class JumArray {

    public JumArray(GameBoardInterface gameBoard) {
        analyser = new JumAnalyser(gameBoard);
        
        jumSquares = new JumSquare[gameBoard.squareCount() + 1];
        
    }
    
    public JumSquare getJumSquare(int square) {
        if (jumSquares[square] == null) {
            jumSquares[square] = new JumSquare(analyser.analyse(square));
        }
        return jumSquares[square];
    }
    
    
    public class JumSquare {
        public JumSquare(IntegerArray[] jums) {
            this.jums = jums;
        }
        
        public int count(int level) {
            return this.jums[level - 1].count();
        }
        
        public int square(int level, int index) {
            return this.jums[level - 1].at(index);
        }
        
        private IntegerArray[] jums;
    }
    
    private JumAnalyser analyser;
    private JumSquare[] jumSquares;
    
}
