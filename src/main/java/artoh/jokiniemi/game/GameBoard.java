package artoh.jokiniemi.game;

/**
 * Pelilaudan toteutus
 * 
 * @author arto
 */
public class GameBoard implements GameBoardInterface {
    
    @Override
    public void init(int squares) {
        this.squares = new Square[squares];
        for (int i = 0; i < squares; i++) {
            this.squares[i] = new Square();
        }
    }

    @Override
    public void addConnection(Vehicle vehicle, int from, int to) {
        // All the connections all two-directionals
        this.squares[from - 1].addConnection(vehicle, to);
        this.squares[to - 1].addConnection(vehicle, from);
    }

    @Override
    public int squareCount() {
        return this.squares.length;
    }

    @Override
    public int connectionsCount(int square) {
        return this.squares[square - 1].connectionsCount();
    }

    @Override
    public int connectionTo(int square, int index) {
        return this.squares[ square - 1].connectionTo(index);
    }

    @Override
    public Vehicle connectionVehicle(int square, int index) {
        return this.squares[square - 1].connectionVehicle(index);
    }
    
    private Square[] squares;
    
}
