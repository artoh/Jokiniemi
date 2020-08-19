package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.Vehicle;

/**
 * Ruutujen välisen etäisyyden laskenta käyttäen sovellettua Floyd-Warshallin algoritmia
 * 
 * Luokka on sopeutettu ottamaan huomioon sen, että kaikki yhteyden ovat kaksisuuntaisia.
 * Lisäksi etäisyys ruudusta itseensä on aina nolla, eikä sitä tallenneta.
 * Tallennustaulukko on siksi muodoltaan vain puolikas neliöstä.
 
 * 
 * Kaikki etäisyydet lasketaan valmiiksi jo alustamisen yhteydessä init()-funktiossa
 * 
 * Laskettaessa aikavaativuus O(n^3) ja tilavaativuus O(n^2) 
 * Laskennan jälkeen haettaessa aikavaativuus O(1) ja tilavaativuus O(n^2)
 * 
 * Tilavaativuus on hieman alle puolet Floyd-Warshallin algoritmista.
 * Aikavaativuus on noin 20 % Floyd-Warshallin algoritmia nopeampi.
 * 
 * @author arto
 */
public class AppliedFloydWarshallDistance extends FloydWarshallDistance {
    
    @Override
    public void init(GameBoardInterface gameboard) {
        
        final int squares = gameboard.squareCount() + 1;
                
        
        this.distances = new int[(squares - 1) * (squares - 1) / 2 + 1];
        
        // Alustetaan etäisyystaulukko maksietäisyyteen eli ruutujen määrään
        // (kaikkien ruutujen välillä on olemassa jokin yhteys).
        
        for (int i = 0; i < distances.length; i++) {
            distances[i] = squares;
        }
        
        // Merkitään yhteydet taulukkoon
        for (int i = 1; i < squares; i++) {            
            for (int c = 0; c < gameboard.connectionsCount(i); c++) {
                if (gameboard.connectionVehicle(i, c) != Vehicle.FERRY &&
                        gameboard.connectionTo(i, c) < i) {                    
                    distances[index(i, gameboard.connectionTo(i, c))] = 1;
                }
            }            
        }                               
                
        // Algoritmi etenee vain neliön puolikkaalla, joten
        // silmukassa käyntejä tulee huomattavasti vähemmän kuin
        // Floyd-Marshallin algoritmilla
        for (int k = 1; k < squares; k++) {            
            for (int i = 2; i < squares; i++) {
                for (int j = 1; j < i; j++) {
                    int myIndex = index(i, j);
                    int newDistance = distance(i, k) + distance(k, j);
                    if (newDistance < distances[myIndex]) {
                        distances[myIndex] = newDistance;
                    }                     
                }
            }
        }                                
    }    
    
    /**
     * Laskee tietyn ruudun sijainnin "puolineliötaulukossa"
     * 
     * Koordinaatit muutetaan yksiulotteisen taulukon 
     * indekseiksi siten, että kaikki tieto tallennetaan vain
     * yhteen kertaan (tallennetaan vain lävistäjän alapuolinen
     * osuus neliöstä) eikä läviställä olevia nolla-arvoja tallenneta
     * ollenkaan. Taulukon indeksit etenevät siis:
     * 
     *   1  2  3  4
     * 1 -
     * 2 0  -
     * 3 1  2  -
     * 4 3  4  5  -
     * 
     * @param y taulukon rivi, 1..n
     * @param x taulukon sarake, 1..n
     * @return Indeksi puolineliötaulukossa
     */
    int index(int y, int x) {
        return (y - 2) * (y - 1) / 2 + x - 1;
    }

    
    @Override
    /**
     * Tämä toteutus ottaa huomioon symmetrisyyden niin, että
     * lävistäjän ylempi puoli neliöstä peilataan alempaan
     * puoliskoon, jonka arvot on tallennettu.
     */
    public int distance(int from, int to) {        
        if (from <= 0 || to <= 0) {
            return Integer.MAX_VALUE;
        } else if (from < to) {            
            return distances[index(to, from)];
        } else if (to < from) {            
            return distances[index(from, to)];
        } else {
            return 0;
        }
    }
    
    private int distances[];
    
}
