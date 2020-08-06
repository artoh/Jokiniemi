package artoh.jokiniemi.ai;

import artoh.jokiniemi.struct.IntegerArray;
import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * AI:n tynkä, joka toteuttaa järjettömän, vaihtoehdoista arpomiseen 
 * perustuvan typerän teköälyn.
 * 
 * Tämä luokka mahdolistaa käyttöliittymän ja pelilogiikan testaamisen
 * ennen syvällisempää teköälyn kehittämistä - sekä pelaajalle
 * helpon voittamisen!
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
        
        IntegerArray possibleIndexes = new IntegerArray(10);    // Ei etsivää samalla ruudulla
        IntegerArray goodIndexes = new IntegerArray(10);    // Ei etsivää viereisellä ruudulla
        
        for (int i = 0; i < game.gameBoard().connectionsCount(currentPosition); i++) {
            // Ei osaa käyttää lauttaa !
            int squareTo = game.gameBoard().connectionTo(currentPosition, i);
            if (game.gameBoard().connectionVehicle(currentPosition, i) != Vehicle.FERRY &&
                !isDetectivePresent(squareTo)) {
                boolean detectiveNear = false;
                for (int j = 0; j < game.gameBoard().connectionsCount(squareTo); j++) {
                    if (isDetectivePresent(game.gameBoard().connectionTo(squareTo, j))) {
                        detectiveNear = true;
                    }
                }
                possibleIndexes.push(i);
                if (!detectiveNear) {
                    goodIndexes.push(i);
                }
            }
        }
        
        // Ensijaisesti käyttää "hyvää" paikkaa, ellei sellaista ole niin "mahdollista,
        // ja ellei edes "mahdollista", niin sitten ensimmäistä jolla jää kiinni...
        if (goodIndexes.count() > 0) {
            possibleIndexes = goodIndexes;
        }
        
                                
        int index = possibleIndexes.count() == 0 
                ? 0 
                : possibleIndexes.at(randomizer.next(possibleIndexes.count()) - 1);
        
        game.doMove(0, game.gameBoard().connectionTo(currentPosition, index), 
                game.gameBoard().connectionVehicle(currentPosition, index), false);        
    }
    
    /**
     * Onko ruudussa etsivää?
     * @param square Peliruudun numero
     * @return Tosi, jos ruudussa on etsivä
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
