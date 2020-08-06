package artoh.jokiniemi.algorithm;

import artoh.jokiniemi.game.GameBoardInterface;

/**
 * Algoritmi, jolla selvitetään kahden ruudun välinen etäisyys
 * 
 * @author ahyvatti
 */
public interface BoardDistanceInterface {

    /**
     * 
     * Alustaa algoritmin peliruudun tiedoilla.
     * 
     * Algoritmista riippuen tämä funktioi saattaa laskea
     * etäisyysmatriisin tai alustaa välimuistin tms.
     * 
     * Initialize the algorithm to be used with game board
     * 
     * @param gameboard Pelilauta
     */
    public void init(GameBoardInterface gameboard);
    
    
    /**
     * Laskee kahden ruudun välisen etäisyyden
     * 
     * Ei huomioi lautalla matkustamista, koska etsivät eivät voi käyttää
     * lauttaa.
     *  
     * @param from Ensimmäinen peliruutu
     * @param to Toinen peliruutu
     * @return Etäisyys peliruutuina
     */
    public int distance(int from, int to);
    
}
