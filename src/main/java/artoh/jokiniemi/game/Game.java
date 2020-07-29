package artoh.jokiniemi.game;

import artoh.jokiniemi.ai.AIInterface;

/**
 *
 * @author ahyvatti
 */
public class Game {

    
    public Game(StartPlaceInterface startPlacer) {
        this.gameLog = new GameLog();
        this.gameboard = new GameBoard();
        this.startplacer = startPlacer;
    }    
    
    public enum GameStatus {
        /**
         * Game has not been started
         */
        NOT_STARTED,
        /**
         * Game is running
         */
        RUNNING,
        /**
         * Game has ended when all the turns have been played and Mr X
         * is not catched. Mr X wins the game.
         */
        MRX_WINS,
        /**
         * Detectives have catched Mr X. Detectives win the game.
         */
        DETECTIVES_WIN
    }
    
    /**
     * Start a new game
     * 
     * @param detectives Count of detectives
     * @param ai The AI object
     */
    public void startGame(int detectives, AIInterface ai) {
        this.blackCardsLeft = this.BLACK_CARDS_TOTAL;
        this.doubleCardsLeft = this.DOUBLE_CARDS_TOTAL;
        
        this.detectivesCount = detectives;
        this.ai = ai;
        this.gameLog.newGame(detectives, startplacer);
        
        this.status = GameStatus.RUNNING;

        ai.startGame(this);
        ai.doAITurn();
    }
    
    /**
     * 
     * Do a move
     * 
     * User inferface will call this function with moves of the detectives.
     * When all the detectives have did their moves, the turn ends and
     * Game will call AI and ask the AI to do Mr X's move.
     * 
     * When the game ends, this function returns the status of the game
     * 
     * @param player Player (0: mrX, 1..n detectives)
     * @param square Square to move
     * @param vehicle Vehicle doing the move
     * @param doubled True when mrX make a first half of the double turn
     * @return Status of the game after this move (and move of AI)
     */
    public GameStatus doMove(int player, int square, Vehicle vehicle, boolean doubled) {
               
        gameLog.logTurn(player, square, vehicle, doubled);        
        
        if (player > 0) {
            this.detectivesMoved++;            
        } else {
            if (vehicle == Vehicle.BLACK_CARD) {
                this.blackCardsLeft--;
            } 
            if (doubled) {
                this.doubleCardsLeft--;
            }
        }
        
        checkStatus();
        
        if (player > 0 &&
            this.status == GameStatus.RUNNING &&
            this.detectivesCount == this.detectivesMoved) {
            ai.doAITurn();
            this.detectivesMoved = 0;
        }
        
        return this.status;
    }
    
    /**
     * Amount of Double Cards Mr X have
     * @return Amount of cards
     */
    public int doubleCardsLeft() {
        return this.doubleCardsLeft;
    }
    
    /**
     * Amount of Black Tickets Mr X have
     * @return amount of tickets
     */
    public int blackCardsLeft() {
        return this.blackCardsLeft;
    }
    
    /**
     * The Game Log object
     * @return Game log
     */
    public GameLog log() {
        return this.gameLog;
    }        
    
    /**
     * The Game Board object
     * @return Game board object
     */
    public GameBoardInterface gameBoard() {
        return this.gameboard;
    }
    
    /**
     * The StartPlaceRandomizer object
     * @return Start placer object
     */
    public StartPlaceInterface startPlacer() {
        return this.startplacer;
    }
    
    /**
     * Return count of detectives
     * @return Count of detectives
     */
    public int detectives() {
        return this.detectivesCount;
    }
    
    /**
     * Return game status
     * @return Game status
     */
    public GameStatus gameStatus() {
        return this.status;
    }
    
    /**
     * Update status of the game
     * 
     * Check if
     * - some detective is in same square as Mr X: Detectives win
     * - all the turns are gone and Mr is still free: Mr X win
     * 
     * @return Game status
     */
    private GameStatus checkStatus() {
        
        int mrXposition = gameLog.currentPosition(0);
        
        for (int i = 1; i < this.detectivesCount + 1; i++) {
            if (gameLog.currentPosition(i) == mrXposition) {
                this.status = GameStatus.DETECTIVES_WIN;
            }
        }
        
        if (gameLog.currentTurn() == gameLog.turnsTotal() - 1 && 
            this.detectivesCount == this.detectivesMoved &&
            this.status == GameStatus.RUNNING) {
            this.status = GameStatus.MRX_WINS;
        }
        
        return this.status;
    }
   
    
    
    private final GameLog gameLog;
    private final GameBoardInterface gameboard;
    private final StartPlaceInterface startplacer;
    private AIInterface ai;

    private final static int BLACK_CARDS_TOTAL = 5;
    private final static int DOUBLE_CARDS_TOTAL = 2;
    
    private int blackCardsLeft;
    private int doubleCardsLeft;
    
    private int detectivesCount;
    private int detectivesMoved;
    
    private GameStatus status = GameStatus.NOT_STARTED;
    
}
