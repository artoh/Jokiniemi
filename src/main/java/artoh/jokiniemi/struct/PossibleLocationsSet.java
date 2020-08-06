package artoh.jokiniemi.struct;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;

/**
 * Mr X:n mahdollisten sijaintien seuraaminen etsivien kannalta
 * 
 * Ohjelma pitää yllä listaa kaikista mahdollista ruuduista, joihin Mister X
 * olisi voinut päätyä lähtemällä näyttäytymisruudustaan liikkeelle
 * niillä matkalipuilla, joita on käyttänyt.
 * 
 * Tämän tiedon perusteella tekoäly voi yrittää valita sellaisia
 * kulkuneuvoja ja käyttää mustia lippua sellaisella tavalla, että
 * etsivät voivat mahdollisimman huonosti päätellä Mister X:n
 * todellisen sijainnin
 * 
 * Tallennusmuotona on binääritaulukko, jossa on jokaisen ruudun osalta tieto
 * siitä, voisiko Mr X olla kyseisessä ruudussa. nextSet()-funktio luo 
 * seuraavalle siirrolle uuden binääritaulukon, johon tulevat kaikki
 * ne ruudut, joihin edellisen vuoron mahdollisista ruuduista pääsee
 * käyttäen sitä matkalippua, joka on valittuna. Algoritmisesti tässä
 * on siis kyse leveyshausta tehokkuudella O(n^m), mutta koska yhteyksen
 * määrä ruutua kohti on pieni, on haun toteuttaminen pelin kartalla
 * (enintään 199 ruutua) kuitenkin nopeaa.
 * 
 * 
 * @author arto
 */
public class PossibleLocationsSet {
    
    /**
     * Muodostaja
     * 
     * @param game Game-olio
     */
    public PossibleLocationsSet(Game game) {
        this.game = game;               
        this.possible = new boolean[game.gameBoard().squareCount() + 1];
        
    }

    /**
     * Merkitsee kaikki ruudut mahdollisiksi
     * 
     * Kun peli alkaa, eivät etsivät tiedä Mr X:n sijaintia ja siksi
     * aluksi kaikki ruudut ovat mahdollisia
     */
    public void fill() {
        for (int i = 1; i < this.possible.length; i++) {
            this.possible[i] = true;
        }
        update();
    }
    
    /**
     * Kun Mr X näyttäytyy, alustaa Mr X:n sijainnilla.
     * 
     * Tällöin etsivät tietävät, missä Mr X on, ja Mr X:n sijainti
     * on ainoa mahdollinen ruutu.
     * 
     * @param square Peliruudun numero, missä Mr X näyttäytyi.
     */
    public void init(int square) {
        for (int i = 0; i < possible.length; i++) {
            this.possible[i] = false;
        }        
        this.possible[square] = true;
        update();
    }
    
    /**
     * Poistaa etsivien sijainnit.
     * 
     * Kun etsivät ovat liikkuneet, pitää heidän sijaintinsa poistaa
     * mahdollisten sijaintien listasta, koska Mr X ei voi 
     * olla samassa ruudussa kuin etsivä.
     */
    public void removeDetectiveLocations() {
        for (int i = 1; i <= game.detectives(); i++) {
            int location = game.log().currentPosition(i);
            if (possible[location]) {
                possible[location] = false;
            }
        }
        update();
    }        
    
    /**
     * Mr X:n mahdollisten sijaintien lukumäärä
     * @return Ruutujen lukumäärä
     */
    public int count() {
        System.out.println("SQ " + squareCount);
        return squareCount;
    }
    
    /**
     * Muodostaa uuden mahdollisten sijaintien taulun, jossa on kaikki
     * Mr X:n mahdolliset sijainnit sen jälkeen, kun hän on 
     * liikkunut tietyn tyyppisellä lipulla.
     * 
     * @param vehicle Matkalipun tyyppi, jolla liikuttu
     * @param cleanDetectiveLocations Poistetaanko ne sijainnit, joissa etsivät ovat
     * @return Uusi taulukko, jossa mahdolliset sijainnit
     */
    public PossibleLocationsSet nextSet(Vehicle vehicle, boolean cleanDetectiveLocations) {
        
        PossibleLocationsSet newSet = new PossibleLocationsSet(game);
                
        for (int square = 1; square < possible.length; square++) {
            if (possible[square]) {
                for (int c = 0; c < game.gameBoard().connectionsCount(square); c++) {
                    if (vehicle == Vehicle.BLACK_CARD ||
                        vehicle == game.gameBoard().connectionVehicle(square, c)) {
                        newSet.possible[game.gameBoard().connectionTo(square, c)] = true;
                    }
                }
            }
        }
        if (cleanDetectiveLocations) {
            newSet.removeDetectiveLocations();
        } else {
            newSet.update();
        }
        
        return newSet;
    }
    
    
    /**
     * Onko taksi ainoa mahdollinen kulkuneuvo?
     * 
     * Mr X:n ei kannata käyttää mustaa lippua, jos taksi olisi kuitenkin ainoa
     * kulkuneuvo kaikissa niissä ruuduissa, joissa Mr X mahdollisesti olisi.
     * 
     * @return Tosi, jos taksi on ainoa mahdollinen kulkuneuvo mahdollisista ruuduista.
     */
    public boolean onlyTaxiPossible() {
        return this.onlyTaxi;
    }
    
    /**
     * Onko lautta mahdollinen kulkuneuvo?
     * 
     * Jos Mr X:n olisi mahdollista kulkea lautalla, kannattaa Mr X:n ehkä
     * käyttää mustaa lippua vaikka ei itse liikkuisikaan lautalla ihan
     * vain etsiviä hämätäkseen..
     * 
     * @return Tosi, jos Mr X:n olisi mahdollista kulkea lautalla
     */
    public boolean ferryPossible() {
        return this.ferry;
    }
    
    public void update() {
        onlyTaxi = true;
        ferry = false;
        this.squareCount = 0;

        for (int i = 1; i < possible.length; i++) {
            if (possible[i]) {
                System.out.println("POSS " + i);
                this.squareCount++;
                for (int c = 0; c < game.gameBoard().connectionsCount(i); c++) {
                    if (game.gameBoard().connectionVehicle(i, c) == Vehicle.FERRY) {
                        ferry = true;   
                        System.out.println("FERRY " + i);
                    }
                    if (game.gameBoard().connectionVehicle(i, c) != Vehicle.TAXI) {
                        onlyTaxi = false;
                    }
                }
            }
        }
        System.out.println("sq " + this.squareCount);
    }
    
    private final Game game;
    private final boolean[] possible;
    
    private int squareCount = 0;
    private boolean onlyTaxi = false;
    private boolean ferry = false;
}
