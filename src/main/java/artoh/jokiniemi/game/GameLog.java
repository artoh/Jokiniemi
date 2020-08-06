package artoh.jokiniemi.game;

/**
 * Peliloki
 * 
 * Pitää kirjaa etsivien ja Mr X:n siirroista ja kulkuneuvoista,
 * sekä tietoa siitä, millä siirroilla Mr X paljastuu
 * 
 * @author ahyvatti
 */
public class GameLog {
    
    
    public GameLog() {
        
    }
    
    /**
     * Paljastuuko Mr X tällä siirrolla
     * 
     * Etsivät eivät yleensä näe, millä pelilaudan ruudulla Mr X sijaitsee.
     * Ainoastaan muutamalla vuorolla Mr X joutuu näyttäytymään etsiville.
     * 
     * @param turn Vuoron numero. Vuoro 0 on pelaajien aloitussijainti
     * @return Tosi, jos Mr X on näkyvillä
     */
    public boolean isVisibleTurn(int turn) {
        if (turn >= this.turnsTotal) {
            return false;
        }
        return this.visible[turn];  // FIX ME !!!!!!!!!
    }
    
    /**
     * Alustaa pelilokin
     * @param turns Pelivuorojen kokonaismäärä
     */
    public void init(int turns) {
        this.turnsTotal = turns;
        this.visible = new boolean[turns];        
    }
    
    /**
     * Asettaa, että Mr X näyttäytyy tällä vuorolla
     * @param turn Pelivuoron numero
     */
    public void setVisibleTurn(int turn) {
        this.visible[turn] = true;
    }
    
    /**
     * Aloittaa pelin.
     * 
     * Sijoittaa etsivät ja Mr X:n aloitussijainteihinsa ja alustaa lokin.
     * 
     * @param detectives Etsivien lukumäärä
     * @param starter Aloitussijainnit arpova olion
     */
    public void newGame(int detectives, StartPlaceInterface starter) {
        
        this.logs = new PlayerGameLog[detectives + 1];
        this.logs[0] = new PlayerGameLog(turnsTotal(), starter.startNewGameAndGetStartPlaceForMisterX());
        for (int i = 0; i < detectives; i++) {
            this.logs[i + 1] = new PlayerGameLog(turnsTotal(), starter.getStartPlaceForDetective());
        }        
    }
    
    /**
     * Nykyisen vuoron numero
     * 
     * Vuoron numeron määrittää Mr X:n tekemien siirtojen lukumäärä.
     * Vuoro 0 tarkoittaa, että aloitussijainnit on arvottu, mutta
     * tietokone ei ole vielä tehnyt ensimmäistä siirtoaan. 
     * 
     * @return Vuoron numero
     */
    public int currentTurn() {
        return this.logs[0].turn();
    }
    
    /**
     * Pelin vuorojen kokonaismäärä
     * 
     * Kun kaikki vuorot on pelattu, voittaa Mr X ellei hän ole vielä jäänyt
     * kiinni.
     * 
     * @return Vuorojen kokonaismäärä
     */
    public int turnsTotal() {
        return turnsTotal;
    }
    
    /**
     * Jäljellä olevien vuorojen määrä (AI:n tehdessä siirtoaan)
     * @return Jäjellä olevat vuorot
     */
    public int turnsLeft() {
        return turnsTotal - currentTurn() - 1;
    }
    
    /**
     * Lisää lokimerkinnän
     * 
     * @param player Pelaajan numero (0: Mr X, 1..n etsivät)
     * @param square Peliruudun numero, johon tällä vuorolla siirrytään.
     * @param vehicle Lipputyyppi, jolla liikutaan
     * @param doubled Mr X käyttää tuplausta (tuplauksen ensimmäisellä siirrolla)
     */
    public void logTurn(int player, int square, Vehicle vehicle, boolean doubled) {
        this.logs[player].addTurn(square, vehicle);
        
        if (doubled) {
            for (int i = 1; i < this.logs.length; i++) {
                this.logs[i].addTurn(this.logs[i].currentPosition(), Vehicle.DOUBLED);
            }
        }
    }
    
    /**
     * Pelaajan sijainti
     * @param player Pelaajan numero (0: Mr X, 1..n etsivät)
     * @param turn Pelivuorn numero (0: aloitussijainti)
     * @return Pelilaudan ruutu, jossa pelaaja on (tämän vuoron jälkeen)
     */
    public int position(int player, int turn) {
        return this.logs[player].position(turn);
    }
    
    
    /**
     * Pelaajan nykyinen sijainti
     * @param player Pelaajan numero (0: Mr X, 1..n etsivät)
     * @return Pelilaudan ruutu, jossa pelaaja on viimeisimmän siirtonsa jälkeen
     */
    public int currentPosition(int player) {
        return this.logs[player].currentPosition();
    }
    
    /**
     * Lipputyyppi, jolla siirto on tehty
     * @param player Pelaajan numeoro (0: Mr X, 1..n etsivät)
     * @param turn Vuoron numero
     * @return Lipputyyppi
     */
    public Vehicle vehicle(int player, int turn) {
        return this.logs[player].vehicle(turn);
    }
    
    private int turnsTotal = 0;    
    private boolean[] visible;
    private PlayerGameLog[] logs;
    
}
