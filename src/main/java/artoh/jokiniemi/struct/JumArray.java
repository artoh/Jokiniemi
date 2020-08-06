
package artoh.jokiniemi.struct;

import artoh.jokiniemi.algorithm.JumAnalyser;
import artoh.jokiniemi.game.GameBoardInterface;

/**
 * Ruutujen saartamismahdollisuuksien taulukko
 * 
 * Ohjelma analysoi pelilaudan ruuduista, kuinka minkäkin ruudun
 * saartaminen onnistuu. Saartamiseen käytetään JumAnalyser -luokkaa
 * 
 * Tason yksi saartaminen tarkoittaa sitä, että etsivät saartavat
 * ruudun siirtymällä kaikkiin viereisiin ruutuihin. 
 * 
 * Tason kaksi saartamien tarkoittaa sitä, että etsivät pääsevät kaikkiin
 * sellaisiin ruutuihin, joiden kautta on mahdollista kontrolloida kahden
 * siirron päässä kyseisestä ruudusta pakenemista.
 * 
 * JumArray pitää yllä luetteloa niistä ruuduista, jotka etsivien pitäisi
 * miehittää ruudun täydelliseen saartamiseen, ja tekoäly voi siten
 * arvioida, millainen mahdollisuus etsivillä on ennättää näihin saartoruutuihin.
 * 
 * JumArray pitää yllä välimuistia jo selvitetyistä saarrostuksista siten,
 * ettei saarrostusta tarvitse laskea uudelleen joka kerran
 * 
 * @see JumAnalyser
 * 
 * @author arto
 */
public class JumArray {

    /**
     * Muodostaja
     * 
     * @param gameBoard Pelilaudan olio
     */
    public JumArray(GameBoardInterface gameBoard) {
        analyser = new JumAnalyser(gameBoard);
        
        jumSquares = new JumSquare[gameBoard.squareCount() + 1];
        
    }
    
    /**
     * Hakee ruudun saarrostusta kuvaavan olion.
     * Jos saarrostus on jo laskettu, haetaan se välimuistista, muuten
     * saarrostus lasketaan.
     * 
     * @param square Ruudun numero
     * @return 
     */
    public JumSquare getJumSquare(int square) {
        if (jumSquares[square] == null) {
            jumSquares[square] = new JumSquare(analyser.analyse(square));
        }
        return jumSquares[square];
    }
    
    /**
     * Yksittäisen ruudun saarrostustilanteita kuvaava olio
     */
    public class JumSquare {
        /**
         * Rakentaja.
         * @param jums Saarrostusruutujen taulukot tasoilla 1 ja 2
         */
        public JumSquare(IntegerArray[] jums) {
            this.jums = jums;
        }
        
        /**
         * Saarrostusruutujen lukumäärä
         * 
         * @see JumArray
         * 
         * @param level Saarrostustaso
         * @return Saarrostusruutujen lukumäärä
         */
        public int count(int level) {
            return this.jums[level - 1].count();
        }
        
        /**
         * Saarrostusruutujen hakeminen
         * 
         * @param level Saarrostustaso
         * @param index Ruudun indeksi (alkaen nollasta)
         * @return Saarrostusruudun numero
         */
        public int square(int level, int index) {
            return this.jums[level - 1].at(index);
        }
        
        private IntegerArray[] jums;
    }
    
    private JumAnalyser analyser;
    private JumSquare[] jumSquares;
    
}
