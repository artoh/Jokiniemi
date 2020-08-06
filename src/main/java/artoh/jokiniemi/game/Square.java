
package artoh.jokiniemi.game;

/**
 * Pelilaudan ruudu
 * 
 * Peliruutu tietäää yhteytensä, eli mihin ruutuihin tästä ruudusta pääsee
 * ja mitä kulkuneuvoa mikäkin yhteys käyttää
 * 
 * @author arto
 */
public class Square {
    
    /**
     * Luokan muodostaja
     */
    public Square() {
        this.connections = 0;
        this.capacity = INITIAL_CAPACITY;
        this.squares = new int[this.capacity];
        this.vehicles = new int[this.capacity];
    }
    
    /**
     * 
     * Lisää yhteyden
     * 
     * @param vehicle Yhteyden kulkuneuvo
     * @param toSquare Yhteyden kohde, pelilaudan ruudun numero
     */
    public void addConnection(Vehicle vehicle, int toSquare) {
        // If the data tables are full, doubles the sizes of the data tables and
        // copy the data tables to the new ones
        if (this.capacity == this.connections) {
            this.capacity = this.capacity * 2;
            int[] newSquares = new int[this.capacity];
            int[] newVehicles = new int[this.capacity];
            for (int i = 0; i < this.connections; i++) {
                newSquares[i] = this.squares[i];
                newVehicles[i] = this.vehicles[i];
            }
            this.squares = newSquares;
            this.vehicles = newVehicles;
        }        
        this.squares[this.connections] = toSquare;
        this.vehicles[this.connections] = vehicle.ordinal();
        this.connections++;
    }
    
    /**
     * Yhteyksien lukumäärä
     * 
     * @return Lukumäärä
     */
    public int connectionsCount() {
        return this.connections;
    }
    
    /**
     * Yhteyden kohderuutu 
     * 
     * @param index Yhteyden indeksi (alkaen nollasta)
     * @return Kohde, pelilaudan ruudun numero
     */
    public int connectionTo(int index) {
        return this.squares[index];
    }
    
    /**
     * Yhteyden kulkuneuvo
     * 
     * @param index Yhteyden indeksi (alkaen nollasta)
     * @return Yhteyden kulkuneuvo, START_SQUARE jos yhteys ei ole kelvollinen.
     */
    public Vehicle connectionVehicle(int index) {
        int vehicle = this.vehicles[index];
        
        if (vehicle == Vehicle.TAXI.ordinal()) {
            return Vehicle.TAXI;
        } else if (vehicle == Vehicle.BUS.ordinal()) {
            return Vehicle.BUS;
        } else if (vehicle == Vehicle.UNDERGROUD.ordinal()) {
            return Vehicle.UNDERGROUD;
        } else if (vehicle == Vehicle.FERRY.ordinal()) {
            return Vehicle.FERRY;
        } else { 
            return Vehicle.START_SQUARE;   
        }
                    
    }    
    
    private final static int INITIAL_CAPACITY = 8;
    
    private int capacity;
    private int connections;
    private int[] squares;
    private int[] vehicles;
    
}
