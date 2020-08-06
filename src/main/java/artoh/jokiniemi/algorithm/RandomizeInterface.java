package artoh.jokiniemi.algorithm;

/**
 * Satunnaislukugeneraattori
 * 
 * Satunnaislukugeneraattori alustetaan luokan koostajassa
 * (käyttäen siemenlukuna esimerkiksi kellonaikaa). Tämän
 * jälkeen satunnaislukuja pyydetään next() -funktiolla.
 * 
 * @author ahyvatti
 */
public interface RandomizeInterface {
    
    
    /**
     * Palauttaa satunnaisluvun (kokonaisluku) väliltä 1..max (molemmat mukaan luettuina)      
     * 
     * @param max Suurin sallittu satunnaisluku
     * @return Satunnaisluku
     */
    public int next(int max);
    
}
