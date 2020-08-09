package artoh.jokiniemi.io;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.GameLog;
import artoh.jokiniemi.game.StartPlaceRandomizer;
import artoh.jokiniemi.game.Vehicle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Lukee ohjelmaan resussiksi sisällytetyn pelilaudan
 * (board.txt) ja alustaa pelilaudan sekä aloituspaikat * 
 * 
 * @author ahyvatti
 */
public class BoardFileReader {

    /**
     * Lukee pelilaudan tiedot
     * 
     * @param game Game-olio
     * @param starter Aloitussijainnit arpova olio
     * @return 
     */
    public boolean readBoard(Game game, StartPlaceRandomizer starter) {
        
        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("board.txt");            
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));            
            
            // Rivi "JOKINIEMI" tiedoston alussa tiedostotyypin tunnisteena
            if (!br.readLine().equals("JOKINIEMI")) {
                return false;
            }
            // Count of squares
            game.gameBoard().init(Integer.parseInt(br.readLine()));                        
            
            // Turntable
            initTurnTable(game.log(), br.readLine());
            
            // Start lists
            initStartList(starter, br.readLine());
            initStartList(starter, br.readLine());
            
            String line;            
            while ((line = br.readLine()) != null) {
                addConnectionLine(game.gameBoard(), line);
            }
            
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
    
    
    /**
     * Alustaa vuorotaulun
     * 
     * @param log GameLog -olio
     * @param line Rivi tyyliin S--X---X---X (X vuorolla, jossa Mr X näkyvä)
     */
    public void initTurnTable(GameLog log, String line) {
        log.init(line.length());
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == 'X') {
                log.setVisibleTurn(i);
            }
        }
    }
    
    /**
     * Alustaa aloitussijaintien listan
     * 
     * Rivi on muotoa X12,45,124 X:n aloitussijainneille 12, 45, 124
     * tai D14,33,177 etsivien aloitussijainneille
     * 
     * @param starter Aloituspaikkojen arpoja
     * @param list Rivi tietoa
     */
    public void initStartList(StartPlaceRandomizer starter, String list) {
                
        boolean mrX = list.charAt(0) == 'X';
        int square = 0;
        for (int i = 1; i < list.length(); i++) {
            if (list.charAt(i) == ',') {
                starter.addStartPlace(mrX, square);
                square = 0;
            } else {
                square = square * 10 + list.charAt(i) - '0';
            }
        }
        starter.addStartPlace(mrX, square);
    }
    
    
    /**
     * Tulkitsee peliruutujen väliset yhteydet määrittelevän rivin
     * ja tallentaa yhteydet.
     * 
     * Yhteydet on määritelty rivillä, joka on muotoa
     * 
     * 12T32T44B13U123F199
     * 
     * 12 - Määrittelee ruudun 12 yhteyksiä
     * T32 T44 = Taksillä pääsee ruutuihin 32 ja 144
     * B13 - Bussilla ruutuun 13
     * U123 - Metrolla ruutuun 123
     * F199 - Lautalla (Mr X pääsee) ruutuun 199
     * 
     * Yhteydet ovat kaksisuuntasia ja merkitty tiedostossa suuntaan
     * pienemmästä ruudusta isompaan päin: ts. esimerkissä määriteltäessä
     * ruutua 32 ei siinä ole merkitty taksiyhteyttä ruutuun 12, koska
     * se lisätään jo ruutua 12 luettaessa.
     * 
     * @param board Pelilaudan olio
     * @param line Rivi tekstiä esim 12T32T44B13U123F199
     */    
    public void addConnectionLine(GameBoardInterface board, String line) {
        int from = 0;
        int to = 0;
        Vehicle vehicle = Vehicle.START_SQUARE;
        
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) > 'A') {
                if (from == 0) {
                    from = to;
                    to = 0;
                } else if (vehicle != Vehicle.START_SQUARE) {
                    board.addConnection(vehicle, from, to);
                    to = 0;
                }
                
                switch (line.charAt(i)) {
                    case 'T':
                        vehicle = Vehicle.TAXI;
                        break;
                    case 'B':
                        vehicle = Vehicle.BUS;
                        break;
                    case 'M':
                        vehicle = Vehicle.UNDERGROUD;
                        break;
                    case 'F':
                        vehicle = Vehicle.FERRY;
                        break;
                    default:
                        break;
                }
                
            } else {
                to = to * 10 + line.charAt(i) - '0';
            }
        }
        
        if (from != 0 && to != 0 && vehicle != Vehicle.START_SQUARE) {
            board.addConnection(vehicle, from, to);
        }
    }
    
}
