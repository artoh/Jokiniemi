/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.ai.SimpleHeuristicAI;
import artoh.jokiniemi.ai.VeryStupidAI;
import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import artoh.jokiniemi.ui.JokiniemiApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integraatiotesti, joka suorittaa koko pelin läpi
 * 
 * @author arto
 */
public class StupidDetectiveTest {
    
    public StupidDetectiveTest() {
    }

    
    private void stupidTest(Game game) {
        int move = 0;

        while (game.gameStatus() == Game.GameStatus.RUNNING) {
            int detective = move % 4 + 1;
            int position = game.log().currentPosition(detective);
            int index = move % game.gameBoard().connectionsCount(position);

            // Huom! Ei huomioi rajallisia matkalippuja, joiden
            // valvonta on käyttöliittymässä eli liput voivat
            // mennä pakkaselle. Ei myöskään sitä, jos ruudussa
            // on jo etsivä.

            int nextPosition = game.gameBoard().connectionTo(position, index);
            Vehicle nextVehicle = game.gameBoard().connectionVehicle(position, index);

            game.doMove(detective, nextPosition, nextVehicle, false);

            move++;
        }

        if (game.gameStatus() == Game.GameStatus.MRX_WINS) {
            assertEquals(0,game.log().turnsLeft());
        }        
    }
    
    @Test
    public void testWithStupidAI() {
        Game game = JokiniemiApplication.initGame();
        AIInterface ai = new VeryStupidAI(new LinearCongruentialGenerator());
        game.startGame(4, ai);
        stupidTest(game);
    }
    
    @Test
    public void testWithHeuristicAI() {
        Game game = JokiniemiApplication.initGame();
        BoardDistanceInterface distances = new FloydWarshallDistance();
        distances.init(game.gameBoard());
        AIInterface ai = new SimpleHeuristicAI(distances);   
        game.startGame(4, ai);
        stupidTest(game);
    }

}
