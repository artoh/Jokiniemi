/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;
import artoh.jokiniemi.game.Vehicle;

/**
 * Syvyyshakuun perustuva etäisyyden määrittely, joka huomioi käytettävissä olevat liput
 * 
 * Määrittelee kahden pelilaudan ruudun välisen etäisyyden niin, että eri tyyppisiä
 * matkalippu on käytettävissä vain rajallinen määrä.
 * 
 * Perustuu iteratiivisesti syvenevään A* -hakuun (IDA*). Haulle on määritelty
 * lähtösyvyys, ja ellei se riitä reitin löytymiseen, syvennetään hakua. Haulle määritellään
 * myös enimmäissyvyys (ellei reittiä tällä syvyydellä löydy, todetaan ruutu
 * saavuttamattomaksi).
 * 
 * Reittiä ei tallenneta
 * 
 * Aikavaativuus O(n^m)  
 * Tilavaativuus O(m)
 * 
 * Haku on hidas, jos kohderuutua ei saavuteta kohtuullisella hakusyvyydellä (erityisesti
 * silloin, kun kohderuutua ei saavuteta ollenkaan, ja algoritmi hakee kaikkia mahdollisia
 * reittiyhdistelmiä koko pelilaudalla). Koska lähimmän etsivän sijaintia määriteltäessä
 * voidaan yli 10 vuoron etäisyydellä oleva etsivät katsoa olevan "tosi kaukana" voidaan
 * tässä ohjelmassa algoritmia käytettäessä määritellä enimmäissyvyydeksi 10 ruutua, jolloin
 * algoritmin hidastuminen ei vielä ole häiritsevää).
 * 
 * Luokka toteuttaa BoardDistanceInterfacen, joten luokkaa testataan myös BoardDistanceTest
 * -testikantaluokan testeillä. 
 * 
 * Lähde: https://en.wikipedia.org/wiki/Iterative_deepening_A*
 * 
 * @author arto
 */
public class TicketAwareDistance implements BoardDistanceInterface {

    @Override
    public void init(GameBoardInterface gameboard) {
        this.gameBoard = gameboard;
    }

    @Override
    public int distance(int from, int to) {
        return distanceWithTickets(from, to, gameBoard.squareCount(), gameBoard.squareCount(), gameBoard.squareCount(), 5, 1024);
    }
    
    /**
     * Pelilaudan kahden ruudun välinen etäisyys lippujen enimmäismäärä huomioiden
     * 
     * @param from Lähtöruutu (ruudun numero)
     * @param to Kohderuutu (ruudun numero)
     * @param taxiTickets Käytettävissä olevien taksilippujen määrä
     * @param busTickets Käytettävissä olevien bussilippujen määrä
     * @param undergroudTickets Käytettävissä olevien metrolippujen määrä
     * @param firstLimit Aloittava hakusyvyys
     * @param maxDeep Haun enimmäissyvyys, jonka jälkeen kohde todetaan saavuttamattomaksi. Normaalilla pelilaudalla syvyys 20 on jo liian hidas.
     * @return Ruutujen välinen etäisyys vuoroina, tai Integer.MAX_VALUE jos liput eivät riitä tälle yhteydelle
     */
    public int distanceWithTickets(int from, int to, int taxiTickets, int busTickets, int undergroudTickets, int firstLimit, int maxDeep) {
        if (from <= 0 || to <= 0) {
            return Integer.MAX_VALUE;
        }
        
        int limit = firstLimit;
        this.taxiTicketsLeft = taxiTickets;
        this.busTicketsLeft = busTickets;
        this.undergroundTicketsLeft = undergroudTickets;
        this.shortest = Integer.MAX_VALUE;
        this.target = to;
        this.walked = new boolean[gameBoard.squareCount() + 1];
        
        while (this.shortest == Integer.MAX_VALUE && limit <= maxDeep) {
            if (findRoute(from, Vehicle.START_SQUARE, limit, 0)) {
                return Integer.MAX_VALUE;
            }
            limit += 3;
        }
        return shortest;
    }
    
    /**
     * Vähentää lipun
     * 
     * @param ticket Lipputyyppi
     * @return tosi, jos liput eivät riitä
     */
    boolean takeTicket(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            if (this.taxiTicketsLeft > 0) {
                this.taxiTicketsLeft--;
            } else {
                return true;
            }
        } else if (ticket == Vehicle.BUS) {
            if (this.busTicketsLeft > 0) {
                this.busTicketsLeft--;
            } else {
                return true;
            }
        } else if (ticket == Vehicle.UNDERGROUD) {
            if (this.undergroundTicketsLeft > 0) {
                this.undergroundTicketsLeft++;
            } else {
                return true;            
            }
        } else {
            return ticket != Vehicle.START_SQUARE;
        }       
        return false;
    }
    
    /**
     * Palauttaa lipun käytettäväksi
     * 
     * @param ticket Lipputyyppi
     */
    void returnTicket(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            this.taxiTicketsLeft++;
        } else if (ticket == Vehicle.BUS) {
            this.busTicketsLeft++;
        } else if (ticket == Vehicle.UNDERGROUD) {
            this.undergroundTicketsLeft++;
        }
    }
    
    /**
     * Iteratiivinen etsintäfunktio
     * 
     * @param square Tutkittava ruutu
     * @param ticket Lippu, jolla ruutuun saavutaan
     * @param limit Haun enimmäissyvyys tästä eteenpäin
     * @param length Etäisyys ruutuina tähän saavuttaessa
     * @return Tosi, jos liput loppuivat tai yhteyksiä ei enää löytynyt
     */
    boolean findRoute(int square, Vehicle ticket, int limit, int length) {
        if (takeTicket(ticket)) {
            return true;
        }
        
        if (square == this.target) {
            this.shortest = length;
            returnTicket(ticket);
            return false;
        }
        
        if (length + 1 >= this.shortest || limit == 0) {
            returnTicket(ticket);
            return false;
        }        
        
        walked[square] = true;
        
        boolean allWalked = true;

        for (int c = 0; c < gameBoard.connectionsCount(square); c++) {
            int connectionTarget = gameBoard.connectionTo(square, c);
            if (walked[connectionTarget]) {
                continue;
            }
            if (!findRoute(connectionTarget, gameBoard.connectionVehicle(square, c), limit - 1, length + 1)) {
                allWalked = false;
            }
        }
        
        walked[square] = false;
        
        returnTicket(ticket);
        return allWalked;
        
    }
    
    private GameBoardInterface gameBoard;
    private int taxiTicketsLeft;
    private int busTicketsLeft;
    private int undergroundTicketsLeft;
    private int shortest;    
    private int target;
    private boolean walked[];
}
