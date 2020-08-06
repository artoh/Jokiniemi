package artoh.jokiniemi.game;

/**
 * Rajapinta Mr X:n ja pelaajien aloituspaikkojen arpomiseen
 * 
 * Pelin alkaessa pitää ENSIN kysyä Mr X:n aloitussijainti
 * startNewGameAndGetStartPlaceForMisterX() -funktiolla ja SITTEN
 * pelaajien aloitussijainnit kutsumalla getStartPlaveForDetective()-funktiota
 * jokaiselle etsivälle.
 * 
 * @author arto
 */
public interface StartPlaceInterface {
    
    /**
     * Aloittaa uuden pelin ja arpoo aloitussijainnin Mr X:llw
     * 
     * @return Mr X:n aloitussijainti, pelilaudan ruudun numero
     */    
    public int startNewGameAndGetStartPlaceForMisterX();

    /**
     * Arpoo etsivän aloitussijainnin
     * 
     * @return Etsivän aloitussijainti
     */
    public int getStartPlaceForDetective();    
    
}
