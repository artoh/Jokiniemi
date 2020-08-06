package artoh.jokiniemi.game;

/**
 * Pelilauta
 * 
 * Pelilaudalla on tieto peliruutujen välisistä yhteyksistä
 * 
 * @author ahyvatti
 */
public interface GameBoardInterface {
    
    /**
     * Alustaa pelilaudan     
     * @param squares Pelilaudan ruutujen lukumäärä
     */
    public void init(int squares);
    
    
    /**
     * Lisää yhteyden pelilaudan kahden ruudun välille
     * 
     * Kaikki yhteydet ovat kaksisuuntaisia. 
     * 
     * @param vehicle Yhteydessä käytettävä kulkuneuvo
     * @param from Yhteyden alkupää (peliruudun numero, numeroitu yhdestä alkaen)
     * @param to Yhteyden loppupää
     */
    public void addConnection(Vehicle vehicle, int from, int to);
    
    /**
     * Pelilaudan ruutujen lukumäärä
     * 
     * Pelilaudan ruudut on numeroitu alkaen ykkösestä
     * 
     * @return Pelilaudan ruutujen lukumäärä
     */
    public int squareCount();
    
    /**
     * Peliruudusta lähtevien yhteyksien lukumäärä
     * 
     * @param square Peliruudun numero
     * @return Yhteyksien lukumäärä
     */
    public int connectionsCount(int square);
    
    /**
     * Yhteyden määränpää (peliruutu, jonne yhteys johtaa)
     * 
     * @param square Peliruudun numero
     * @param index Yhteyden indeksi (alkaen nollasta)
     * @return Peliruudun numero, jonne yhteys johtaa
     */    
    public int connectionTo(int square, int index);
    
    /**
     * Yhteyden käyttämä kulkuneuvo
     * 
     * @param square Peliruudun numero
     * @param index Yhteyden indeksi (alkaen nollasta)
     * @return Kulkuneuvo, joka käyttää yhteyttä
     */
    public Vehicle connectionVehicle(int square, int index);
    
}
