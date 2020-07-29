package artoh.jokiniemi.game;

/**
 * Keep a log of positons and vehicles of mr X and detectives
 * 
 * @author ahyvatti
 */
public class GameLog {
    
    
    public GameLog() {
        
    }
    
    /**
     * Is Mr X visible during this turn
     * @param turn Turn number
     * @return True if positon of Mr X is visibe
     */
    public boolean isVisibleTurn(int turn) {
        return this.visible[turn];
    }
    
    /**
     * Init the game log
     * @param turns Total amount of turns in the game
     */
    public void init(int turns) {
        this.turns_total = turns;
        this.visible = new boolean[turns];        
    }
    
    /**
     * Set a turn as visible turn for Mister X
     * @param turn Index of turn
     */
    public void setVisibleTurn(int turn) {
        this.visible[turn] = true;
    }
    
    /**
     * Start a new game. Mark the initial positions of Mr X and detectives.
     * 
     * @param detectives Count of detectives
     * @param starter Start placer object
     */
    public void newGame(int detectives, StartPlaceRandomizer starter) {
        
        this.logs = new PlayerGameLog[detectives + 1];
        this.logs[0] = new PlayerGameLog(turnsTotal(), starter.startNewGameAndGetStartPlaceForMisterX());
        for(int i=0; i < detectives; i++) {
            this.logs[i+1] = new PlayerGameLog(turnsTotal(), starter.getStartPlaceForDetective());
        }        
    }
    
    /**
     * Number of the current turn
     * @return Number of turn
     */
    public int currentTurn() {
        return this.logs[0].turn();
    }
    
    /**
     * Amount of turns total in the game
     * @return Amount of turn
     */
    public int turnsTotal() {
        return turns_total;
    }
    
    /**
     * Add a mark to the log
     * 
     * @param player Number of player (0: mr X, 1..n: detectives)
     * @param square Square number
     * @param vehicle Vehicle used to go here
     * @param doubled Mr X use the Double card
     */
    public void logTurn(int player, int square, Vehicle vehicle, boolean doubled) {
        this.logs[player].addTurn(square, vehicle);
        
        if(doubled) {
            for(int i=1; i<this.logs.length;i++) {
                this.logs[i].addTurn(this.logs[i].currentPosition(), Vehicle.DOUBLED);
            }
        }
    }
    
    /**
     * Return a position of a player
     * @param player Number a player (0: mrX, 1..n detectives)
     * @param turn Number of turn
     * @return Square of player position
     */
    public int position(int player, int turn) {
        return this.logs[player].position(turn);
    }
    
    
    /**
     * Return players current position
     * @param player Number of player (0: mrX, 1..n detectives)
     * @return Square of player position
     */
    public int currentPosition(int player) {
        return this.logs[player].currentPosition();
    }
    
    /**
     * Return a vehicle of plaer when moving to the square
     * @param player Number of a player (0: mrX 1..n detectives)
     * @param turn Number of turn
     * @return Vehicle used to move
     */
    public Vehicle vehicle(int player, int turn) {
        return this.logs[player].vehicle(turn);
    }
    
    private int turns_total = 0;    
    private boolean[] visible;
    private PlayerGameLog[] logs;
    
}
