/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.struct.IntegerArray;

/**
 *
 * @author arto
 */
public class JumAnalyser {

    public JumAnalyser(GameBoardInterface gameBoard) {
        this.gameBoard = gameBoard;
        
        list = new int[gameBoard.squareCount() + 1];
        parent = new int[gameBoard.squareCount() + 1];
        level = new int[gameBoard.squareCount() + 1];
        valid = new boolean[gameBoard.squareCount() + 1];
                
    }
    
    public void addToList(int square, int parent, int level) {
        if (this.level[square] == 0) {
            list[listCounter] = square;
            this.parent[square] = parent;
            this.level[square] = level;
            this.valid[square] = false;
            this.listCounter++;
        }
    }
    
    public void markValid(int square) {
        this.valid[square] = true;
        int parentOfSquare = this.parent[square];
        while (parentOfSquare > 0) {
            this.valid[parentOfSquare] = true;
            parentOfSquare = this.parent[parentOfSquare];
        }
    }
    
    public IntegerArray[] analyse(int square) {
        // Initialize
        this.listCounter = 0;
        for (int i = 1; i <= gameBoard.squareCount(); i++) {
            this.level[i] = 0;
        }
        
        // Add squares to list
        for (int i = 0; i < gameBoard.connectionsCount(square); i++) {
            addToList( gameBoard.connectionTo(square, i), -1, 1);
        }        
        
        for (int i=0; i < this.listCounter; i++) {
            int squareInProcess = this.list[i];
            int levelInProcess = this.level[squareInProcess];
            
            for (int c = 0; c < gameBoard.connectionsCount(squareInProcess); c++) {
                int nextSquare = gameBoard.connectionTo(squareInProcess, c);
                if ( levelInProcess > 2) {
                    if (level[nextSquare] == 0) {
                        markValid(squareInProcess);
                        break;
                    }
                } else {                    
                    addToList(nextSquare, squareInProcess, levelInProcess + 1);
                }
            }
        }
        
        IntegerArray jumList[] = new IntegerArray[2];
        for (int i=0; i < 2; i++) {
            jumList[i] = new IntegerArray((i+1) * 8);
        }            
        
        // Collect the lists
        for (int i = 0; i < listCounter; i++) {
            int currentSquare = list[i];
            int currentLevel = level[currentSquare];
            if (currentLevel > 2) {
                break;
            } else if(valid[currentSquare]){                
                jumList[currentLevel-1].push(currentSquare);
            }
        }
        return jumList;
    }
    
        
    private GameBoardInterface gameBoard;
        
    private int[] list;
    private int[] parent;
    private int[] level;
    private boolean[] valid;
    private int listCounter = 0;
    
}
