/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.TicketAwareDistance;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Ylläpitää ruudun sijaintia lähimpään etsivään
 * 
 * Koska tätä etäisyyttä tarvitaan useassa siirtovaihtoehtoa arvioivassa
 * funktiossa, tallennetaan lasketut etäisyydet välimuistiin joista se
 * ensisijaisesti haetaan (ellei etäisyyttä löydy välimuistista, se lasketaan).
 * 
 * Aina etsivien liikkumisen jälkeen välimuisti tyhjennetään.
 * 
 * Tietorakenne huomioi sen, milloin etäisyys on tarpeen laskea hitaammalla
 * algoritmilla, joka huomioi etsivillä jäljellä olevien lippujen määrät.
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
        this.ticketDistancer = new TicketAwareDistance();
        this.ticketDistancer.init(game.gameBoard());
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
        this.tickets = minTickets();
    }
    
    /**
     * Lippujen vähimmäismäärä
     * 
     * Sen lipputyypin vähimmäismäärä, jonka lippuja on vähiten. Jos etäisyys
     * ruutuun on tätä pidempi, on mahdollista, että siihen ei riittäisi lippuja,
     * ja etäisyys on syytä laskea myös lippujen määrän huomioivalla
     * hitaammalla algoritmilla.
     * 
     * @return Vähin määrä lippuja
     */
    int minTickets() {
        int minTickets = this.game.ticketsLeft(Vehicle.UNDERGROUD);
        if (this.game.ticketsLeft(Vehicle.BUS) < minTickets) {
            minTickets = this.game.ticketsLeft(Vehicle.BUS);
        } else if (this.game.ticketsLeft(Vehicle.TAXI) < minTickets) {
            minTickets = this.game.ticketsLeft(Vehicle.TAXI);
        }
        return minTickets;
    }    
    
    /**
     * Yksittäisen etsivän etäisyys tiettyyn ruutuun
     * 
     * Jos kyseessä ei ole bobby, huomioi myös lippujen riittävyyden.
     * 
     * @param detective Etsivän numero
     * @param square Pelilaudan ruudun numero
     * @return Etäisyys vuoroina
     */
    public int detectiveDistance(int detective, int square) {
        int distance = this.distancer.distance(square, game.log().currentPosition(detective));
        if (distance > tickets && detective > this.game.bobbies()) {
            distance = this.ticketDistancer.distanceWithTickets(square, game.log().currentPosition(detective), 
                    game.ticketsLeft(Vehicle.TAXI), game.ticketsLeft(Vehicle.BUS), game.ticketsLeft(Vehicle.UNDERGROUD), distance, 10);
            return (distance > 10 ? 10 : distance);
        } else {
            return distance;
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
            int distance = detectiveDistance(detective, square);
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
    private int tickets;
    private BoardDistanceInterface distancer;
    private TicketAwareDistance ticketDistancer;
    private Game game;
}
