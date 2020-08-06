
package artoh.jokiniemi.ai;

import artoh.jokiniemi.game.Game;

/**
 * Tekoälyn rajapinta
 * 
 * @author ahyvatti
 */
public interface AIInterface {
    
    /**
     * Kutsutaan, kun uusi peli aloitetaan.
     * 
     * AI:n tulee alustaa uusi peli. Ensimmäinen siirto tehdään kuitenkin
     * vasta doAITurn()-kutsulla
     */
    public void startGame(Game game);
    
    /**          
     * 
     * Kutsutaan, kun on AI:n vuoro tehdä oma siirtonsa
     * 
     * Kun AI on päättänyt siirtonsa, kutsuu AI Game.doMove()-funktiota.
     * Jos AI käyttää tuplauskorttia, kutsuu AI Game.doMove()-funktiota kahdesti.
     * 
     */
    public void doAITurn();
    
}
