/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.net.URL;

/**
 * Read the integrated gameboard file (board.txt) and init the
 * game board and start places.
 * 
 * @author ahyvatti
 */
public class BoardFileReader {

    /**
     * Read gameboard file
     * 
     * @param game The Game object
     * @return True if success
     */
    public boolean readBoard(Game game, StartPlaceRandomizer starter) {
        
        try {
            FileReader reader = new FileReader(getResourceFile("board.txt"));
            BufferedReader br = new BufferedReader(reader);            
            
            // Line "JOKINIEMI" as file format specifier
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
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * Get the resource file
     * @param fileName File name
     * @return File object
     */
    private File getResourceFile(String fileName) {
        ClassLoader loader = getClass().getClassLoader();
        URL resource = loader.getResource(fileName);
        return new File(resource.getFile());
    }
    
    /**
     * Initial turn table
     * 
     * @param log GameLog object
     * @param line Line like S--X---X---X (X means mr X visible)
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
     * Initial start place list of mr X or detective
     * 
     * @param starter Start Place Randomizer object
     * @param list Line like D12,34,123
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
     * Parse a connection line and store it into Game Board
     * 
     * Connections are described in lines like
     * 
     * 12T32T44B13U123
     * 
     * 12 = FROM squre
     * T = Taxi
     * 32 = TO square with taxi
     * 
     * B = Bus
     * M = Underground
     * F = Ferry
     * 
     * Connections are two-sided: "From" number is less than "to" number
     * 
     * @param board Game Board object
     * @param line Line of text like 1T12B34U87F122
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
