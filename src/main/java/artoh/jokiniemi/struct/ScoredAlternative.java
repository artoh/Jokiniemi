package artoh.jokiniemi.struct;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Pisteytetty liikkumisvaihtoehto vuorolle
 * 
 * AI:n kulloinkin parhaimmaksi arvioima siirtovaihtoehto tallennetaan tällä
 * luokalla.
 * 
 * @author arto
 */
public class ScoredAlternative {
    /**
     * Siirtovaihtoehdon muodostaja
     * 
     * Parametreja secondTicket ja secondTarget käytetään vain kun
     * kyse on tupasiirrosta.
     * 
     * @param firstTicket Siirrossa käytettävä lippu
     * @param firstTarget Siirron kohde (peliruudun numero)
     * @param secondTicket Tuplatussa siirrossa jälkimmäisessä siirrossa käytetty lippu
     * @param secondTarget Tuplatussa siirrossa jälkimmäisen siirron kohde (peliruudun numeo)
     * @param score Siirron pisteytys
     */
    public ScoredAlternative(Vehicle firstTicket, int firstTarget, 
              Vehicle secondTicket, int secondTarget, int score) {
        this.firstTicket = firstTicket;
        this.firstTarget = firstTarget;
        this.secondTicket = secondTicket;
        this.secondTarget = secondTarget;
        this.score = score;
    }

    /**
     * Tekee siirron
     * 
     * @param game Game-olio
     */
    public void doMove(Game game) {
        game.doMove(0, this.firstTarget, this.firstTicket, this.secondTarget > 0);
        if (this.secondTarget > 0) {
            game.doMove(0, this.secondTarget, this.secondTicket, false);
        }
        System.out.println("---> " + firstTicket + " " + firstTarget + " ---> " + secondTarget);
    }


    /**
     * Siirrolle annettu pisteytys
     * @return Siirron pisteytys
     */
    public int score() {
        return this.score;
    }

    private Vehicle firstTicket;
    private int firstTarget;
    private Vehicle secondTicket;
    private int secondTarget;

    private int score;
}    
