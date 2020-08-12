package artoh.jokiniemi.ui;

import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.algorithm.TicketAwareDistance;
import artoh.jokiniemi.game.Game;
import static artoh.jokiniemi.ui.JokiniemiApplication.initGame;
import java.util.Arrays;

/**
 * Ohjelman käynnistämiseen tarvittava luokka
 * 
 * Ohjelmistotekniikan kurssin oppien mukaisesti tällaisesta luokasta
 * on hyötyä kun JavaFX-sovellusta paketoidaan.
 * 
 * Tähän luokkaan on toteutettu myös yksinkertainen suorituskykytesti
 * etäisyydenhaulle. 
 * 
 * @author arto
 */
public class Main {

    /**
     * Suorittaa suorituskykytestin
     * 
     * Suorituskykytestissä pelilaudalle tehdään 20 000 etäisyyshakua.
     * Pelilaudalla on 199 ruutua, eli etäisyyspareja on noin 40 000.
     * Koska aineistona käytetään varsinaista pelilautaa, joka on näinkin
     * pieni, jäävät suoritusajat harmillisen lyhyiksi.
     *  
     * @param distance Etäisyyksien määrittelyn toteuttava luokka
     * @return 
     */
    private static long performanceTest(BoardDistanceInterface distance) {
        Game game = JokiniemiApplication.initGame();
        long alku = System.nanoTime();

        distance.init(game.gameBoard());
        int sum = 0;

        for (int i = 0; i < 20000; i++) {
            int from = ((i + 79) * 1785) % 199 + 1;
            int to = ((i + 577) * 9745) % 199 + 1;
            sum += distance.distance(from, to);            
        }
        long loppu = System.nanoTime();
        
        return (sum == 80173 ? loppu - alku : 0);
    }
    
    /**
     * Suorituskykytestin ajaminen
     * 
     * @param algorithm Suorituskykytestin nimi
     */
    private static void testPerformance(String algorithm) {
        JokiniemiApplication app = new JokiniemiApplication();
        if (algorithm.equals("FW")) {
            System.out.println("Floyd-Warshall " + 1.0 * performanceTest(new FloydWarshallDistance()) / 1e9 + " s.");
        } else if (algorithm.equals("TA")) {
            System.out.println("Ticket Aware " + 1.0 * performanceTest(new TicketAwareDistance()) / 1e9 + " s.");    
        }
    }    
    
    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("test")){
            testPerformance(args[1]);
        } else {
            JokiniemiApplication.main(args);
        }
    }
}
