
package artoh.jokiniemi.game;

/**
 * A Square of the Game Board
 * 
 * A Square knows the connections: what squares is connected and
 * by what vehicle
 * 
 * @author arto
 */
public class Square {
    
    /**
     * The constructor. Inital data tables with initial size.
     */
    public Square() {
        this.connections = 0;
        this.capacity = INITIAL_CAPACITY;
        this.squares = new int[this.capacity];
        this.vehicles = new int[this.capacity];
    }
    
    /**
     * 
     * Add a connection
     * 
     * @param vehicle Vehicle of connection
     * @param toSquare Target square of connection
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
     * Count of connections
     * 
     * @return Count
     */
    public int connectionsCount() {
        return this.connections;
    }
    
    /**
     * Target square of a connection
     * @param index Index of connection (index starts at 0)
     * @return Number of target square
     */
    public int connectionTo(int index) {
        return this.squares[index];
    }
    
    /**
     * Vehicle used with this connection
     * 
     * @param index Index of connection (index starts at 0)
     * @return Vehicle used by this connection. START_SQUARE if vehicle is uncorrect.
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
