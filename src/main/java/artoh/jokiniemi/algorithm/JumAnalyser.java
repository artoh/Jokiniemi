
package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.struct.IntegerArray;

/**
 * Saarrostusruutujen luetteloiden muodostaminen
 * 
 * @see JumArray
 * 
 * Saarrostusruutujen luettelon avulla arvioidaan Mr X:n riskiä joutua
 * etsivien saartamaksi.
 * 
 * Saarrostusruutujen luettelo luodaan leveyshakuun perustuvalla algoritmilla
 * 
 * - listalle lisätään kaikki ne ruudut, joihin ruudusta pääsee
 * - samoin menetellään alemmilla tasoilla niille ruuduille, jotka eivät
 * jo ole listalle
 *  - viimeisellä tasolla merkitään hyväksytyiksi vain ne ruudut joista
 * pääsee eteenpäin johonkin ruutuun, jotka eivät ole listalle
 * 
 * Tämän menettelyn avulla saarrostuksessa otetaan huomioon myös se, jos
 * polut yhtyvät niin, että ruudun voi saarrostaa etäämpää.
 * 
 * Aikavaatuvuus pahimmillaan O(n^3)
 * Tilavaativuu pahimmmillaan O(n^2)
 * 
 * Pelissä käytettävällä 199 ruudun ruudukolla aika- ja tilavaativuudet
 * eivät kuitenkaan muodostu kriittisiksi.
 * 
 * Tätä luokkaa käytetään JumArray-luokan kautta, joka muodostaa
 * välimuistin saartoruutujen tauluille.
 * 
 * @author arto
 */
public class JumAnalyser {

    /**
     * Muodostaja
     * 
     * @param gameBoard Peliruudukon olio
     */
    public JumAnalyser(GameBoardInterface gameBoard) {
        this.gameBoard = gameBoard;
        
        list = new int[gameBoard.squareCount() + 1];
        parent = new int[gameBoard.squareCount() + 1];
        level = new int[gameBoard.squareCount() + 1];
        valid = new boolean[gameBoard.squareCount() + 1];
                
    }
    
    /**
     * Lisää ruudun listalle (vain sisäiseen käyttöön)
     * 
     * @param square Ruudun numero
     * @param parent Ruutu, josta tänne tullaan
     * @param level Analyysin taso
     */
    void addToList(int square, int parent, int level) {
        if (this.level[square] == 0) {
            list[listCounter] = square;
            this.parent[square] = parent;
            this.level[square] = level;
            this.valid[square] = false;
            this.listCounter++;
        }
    }
    
    /**
     * Merkitsee ruudun (ja ruudut, josta sinne tultiin) kelvolliseksi.
     * (Vain sisäiseen käyttöön)
     * 
     * @param square Ruudun numero
     */
    void markValid(int square) {
        this.valid[square] = true;
        int parentOfSquare = this.parent[square];
        while (parentOfSquare > 0) {
            this.valid[parentOfSquare] = true;
            parentOfSquare = this.parent[parentOfSquare];
        }
    }
    
    /**
     * Tekee analyysin ruudusta
     * 
     * @param square Analysoitava ruutu
     * @return Saartoruutujen taulukot kahdelle tasolle
     */
    public IntegerArray[] analyse(int square) {
        // Initialize
        this.listCounter = 0;
        for (int i = 1; i <= gameBoard.squareCount(); i++) {
            this.level[i] = 0;
        }
        
        // Add squares to list
        for (int i = 0; i < gameBoard.connectionsCount(square); i++) {
            addToList(gameBoard.connectionTo(square, i), -1, 1);
        }        
        
        for (int i = 0; i < this.listCounter; i++) {
            int squareInProcess = this.list[i];
            int levelInProcess = this.level[squareInProcess];
            
            for (int c = 0; c < gameBoard.connectionsCount(squareInProcess); c++) {
                int nextSquare = gameBoard.connectionTo(squareInProcess, c);
                if (levelInProcess > 2) {
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
        for (int i = 0; i < 2; i++) {
            jumList[i] = new IntegerArray((i + 1) * 8);
        }            
        
        // Collect the lists
        for (int i = 0; i < listCounter; i++) {
            int currentSquare = list[i];
            int currentLevel = level[currentSquare];
            if (currentLevel > 2) {
                break;
            } else if (valid[currentSquare]) {                
                jumList[currentLevel - 1].push(currentSquare);
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
