/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.game.Game;

/**
 * Ylläpitää ruudun sijaintia lähimpään etsivään
 * 
 * Koska tätä etäisyyttä tarvitaan useassa siirtovaihtoehtoa arvioivassa
 * funktiossa, tallennetaan lasketut etäisyydet välimuistiin joista se
 * ensisijaisesti haetaan (ellei etäisyyttä löydy välimuistista, se lasketaan).
 * 
 * Aina etsivien liikkumisen jälkeen välimuisti tyhjennetään.
 * 
 * @author arto
 */
public class NearestDetectiveCache {
    
    /**
     * Muodostaja
     * 
     * @param game Game-olio
     */
    public NearestDetectiveCache(Game game, BoardDistanceInterface distancer) {
        this.distancer = distancer;
        this.game = game;
        this.distances = new int[game.gameBoard().squareCount() + 1];
        invalidate();
    }
    
    /**
     * Tyhjentää välimuistin
     * 
     * Kun etsivät ovat liikkuneet, pitää välimuisti tyhjentää, jotta
     * jatkossa ilmoitetaan etäisyys nykyisiin sijainteihin
     */
    public final void invalidate() {
        for (int i = 1; i < this.distances.length; i++) {
            this.distances[i] = -1;
        }
    }
    
    /**
     * Laskee etäisyyden lähimpään sijaintiin
     * 
     * @param square Ruutu, josta etäisyys lasketaan
     * @return Etäisyys vuoroina
     */
    int countDistance(int square) {
        int nearest = Integer.MAX_VALUE;
        
        for (int detective = 1; detective <= this.game.detectives(); detective++) {
            int distance = this.distancer.distance(square, game.log().currentPosition(detective));
            if (distance < nearest) {
                nearest = distance;
            }
        }
        return nearest;
    }
    
    /**
     * Etäisyys ruudusta lähimpään etsivään
     * 
     * Etäisyys haetaan ensisijaisesti välimuistista ja lasketaan ainoastaan, ellei
     * etäisyyttä ole vielä aiemmin laskettu välimuistiin
     * 
     * @param square Peliruudun numero
     * @return Etäisyys vuoroina
     */
    public int distance(int square) {
        if (this.distances[square] < 0) {
            this.distances[square] = countDistance(square);
        }
        return this.distances[square];
    }
    
    private int[] distances;
    private BoardDistanceInterface distancer;
    private Game game;
}
