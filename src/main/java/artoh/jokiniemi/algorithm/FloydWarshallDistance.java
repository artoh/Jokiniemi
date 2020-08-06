package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.Vehicle;

/**
 * Laskee ruutujen välisen etäisyyden käyttäen Floyd-Warshallin algoritmia
 * 
 * Kaikki etäisyydet lasketaan valmiiksi jo alustamisen yhteydessä init()-funktiossa
 * 
 * @author arto
 */
public class FloydWarshallDistance implements BoardDistanceInterface {

    @Override
    public void init(GameBoardInterface gameboard) {
        
        final int squares = gameboard.squareCount() + 1;
        
        // Alustaa etäisyysmatriisiin 1 jos ruutujen välillä on yhteys
        // ja muuten pelilaudan ruutujen määrän (=maksimietäisyys, koska
        // kaikki ruudut yhteydessä toisiinsa)
        
        distances = new int[squares + 1][squares + 1];
        
        for (int i = 1; i < squares; i++) {
            for (int j = 1; j < squares; j++) {
                distances[i][j] = squares;
            }
            
            
            for (int c = 0; c < gameboard.connectionsCount(i); c++) {
                if (gameboard.connectionVehicle(i, c) != Vehicle.FERRY) {
                    // Etsivät eivät voi käyttää lauttaa
                    distances[i][gameboard.connectionTo(i, c)] = 1;
                }
            }            
        }
        
        
        for (int k = 1; k < squares; k++) {
            for (int i = 1; i < squares; i++) {
                for (int j = 1; j < squares; j++) {
                    distances[i][j] = 
                            distances[i][j] < distances[i][k] + distances[k][j] ?
                            distances[i][j] :
                            distances[i][k] + distances[k][j];
                }
            }
            distances[k][k] = 0;
        }
    }

    @Override
    public int distance(int from, int to) {
        return distances[from][to];
    }
    
    private int distances[][];
    
}
