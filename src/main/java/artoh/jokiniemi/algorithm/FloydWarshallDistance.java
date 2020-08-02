package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.Vehicle;

/**
 * Calculate distances between squares using Floyd-Warshall algorith
 * 
 * All the distances will be calculated in the constructor.
 * 
 * @author arto
 */
public class FloydWarshallDistance implements BoardDistanceInterface {

    @Override
    public void init(GameBoardInterface gameboard) {
        
        final int squares = gameboard.squareCount() + 1;
        
        // Init distance table to 1 if squares are connected and
        // Integer.MAX if there are no direct connection
        
        distances = new int[squares + 1][squares + 1];
        
        for (int i = 1; i < squares; i++) {
            for (int j = 1; j < squares; j++) {
                distances[i][j] = squares;
            }
            
            
            for (int c = 0; c < gameboard.connectionsCount(i); c++) {
                if (gameboard.connectionVehicle(i, c) != Vehicle.FERRY) {
                    // Detectives is not allowed to use the ferry
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
