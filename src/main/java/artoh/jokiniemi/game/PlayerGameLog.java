package artoh.jokiniemi.game;

/**
 * Yksittäisen pelaajan lokitiedot
 * 
 * @author arto
 */
public class PlayerGameLog {
    
    /**
     * Pelaajalokin alustaja
     * 
     * @param turns Pelin kokonaisvuorot
     * @param initialPosition Pelaajan aloitussijainti, peliruudun numero
     */
    public PlayerGameLog(int turns, int initialPosition) {
        this.turnsPlayed = 0;
        
        this.squares = new int[turns + 1];
        this.vehicles = new int[turns + 1];
        
        this.squares[0] = initialPosition;
        this.vehicles[0] = Vehicle.START_SQUARE.ordinal();
    }
    
    /**
     * Lisää siirron lokiin
     * 
     * @param square Pelaajan uusi sijainti, peliruudun numero
     * @param vehicle Lipputyyppi, jolla pelaaja liikkui
     * @return Pelaajan tekemien siirtojen määrä
     */
    public int addTurn(int square, Vehicle vehicle) {
        
        if (this.turnsPlayed + 1 >= this.squares.length) {
            return this.turnsPlayed;
        }
        
        this.turnsPlayed++;
        this.squares[this.turnsPlayed] = square;
        this.vehicles[this.turnsPlayed] = vehicle.ordinal();
        return this.turnsPlayed;
    }
    
    /**
     * Palaajan sijainti tietyllä vuorolla
     * @param turn Vuoron numero
     * @return Peliruudun numero
     */
    public int position(int turn) {
        return this.squares[turn];
    }
    
    /**
     * Lippu, jolla pelaaja on tehnyt siirron
     * @param turn Vuoron numero
     * @return Lipputyyppi, jolla siirto on tehty
     */
    public Vehicle vehicle(int turn) {
        int ordinal = this.vehicles[turn];
        
        if (ordinal == Vehicle.TAXI.ordinal()) {
            return Vehicle.TAXI;
        } else if (ordinal == Vehicle.BUS.ordinal()) {
            return Vehicle.BUS;
        } else if (ordinal == Vehicle.UNDERGROUD.ordinal()) {
            return Vehicle.UNDERGROUD;
        } else if (ordinal == Vehicle.BLACK_CARD.ordinal()) {
            return Vehicle.BLACK_CARD;
        } else if (ordinal == Vehicle.DOUBLED.ordinal()) {
            return Vehicle.DOUBLED;
        } else {
            return Vehicle.START_SQUARE;
        }
    }
            
    /**
     * Pelaajan nykyinen sijainti
     * @return Peliruudun numero
     */
    public int currentPosition() {
        return squares[turnsPlayed];
    }
    
    /**
     * Vuoroja pelattu
     * @return Pelaajan tekemien siirton lukumäärä
     */
    public int turn() {
        return turnsPlayed;
    }
    
    private int turnsPlayed;
    private int squares[];
    private int vehicles[];
    
}
