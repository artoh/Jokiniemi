package artoh.jokiniemi;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.ai.SimpleHeuristicAI;
import artoh.jokiniemi.ai.VeryStupidAI;
import artoh.jokiniemi.algorithm.AppliedFloydWarshallDistance;
import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.algorithm.TicketAwareDistance;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import artoh.jokiniemi.ui.JokiniemiApplication;

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
     * @return Kulunut aika nanosekunteina
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
     * Tekoälyn suorituskykytestin ajaminen
     * 
     * Ajaa täyden pelin etsivien hassuilla siirroilla.
     * Koska tähän liittyy paljon satunnaisuutta, on testi
     * syytä toistaa. Paluuarvo 0 tarkoittaa testin epäonnistumista 
     * sen takia, että Mr X jää kiinni.
     * 
     * @param game Peli-olio
     * @return Kulunut aika nanosekunteina
     */
    private static long aiTest(Game game) {
        int move = 0;
        long alku = System.nanoTime();

        while (game.gameStatus() == Game.GameStatus.RUNNING) {
            int detective = move % 4 + 1;
            int position = game.log().currentPosition(detective);
            int index = move % game.gameBoard().connectionsCount(position);

            int nextPosition = game.gameBoard().connectionTo(position, index);
            Vehicle nextVehicle = game.gameBoard().connectionVehicle(position, index);

            game.doMove(detective, nextPosition, nextVehicle, false);

            move++;
        }
        long loppu = System.nanoTime();

        return (game.gameStatus() == Game.GameStatus.MRX_WINS ? loppu - alku : 0);                         
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
        } else if (algorithm.equals("AF")) {
            System.out.println("Applied Floyd-Warshall " + 1.0 * performanceTest(new AppliedFloydWarshallDistance()) / 1e9 + " s.");
        } else if (algorithm.equals("HA")) {
            Game game = JokiniemiApplication.initGame();
            BoardDistanceInterface distances = new AppliedFloydWarshallDistance();
            distances.init(game.gameBoard());
            AIInterface ai = new SimpleHeuristicAI(distances);   
            game.startGame(4, ai);
            System.out.println("Heuristic AI " + 1.0 * aiTest(game) / 1e9 + " s.");
        } else if (algorithm.equals("SA")) {
            Game game = JokiniemiApplication.initGame();
            AIInterface ai = new VeryStupidAI(new LinearCongruentialGenerator());
            game.startGame(4, ai);
            System.out.println("Stupid AI " + 1.0 * aiTest(game) / 1e9 + " s.");
        }
    }    
    
    /**
     * Pääfunktio
     * 
     * @param args Käynnistysparametrit (test FW ja test TA suorituskykytesteihin)
     */
    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("test")) {
            testPerformance(args[1]);
        } else {
            JokiniemiApplication.main(args);
        }
    }
}
