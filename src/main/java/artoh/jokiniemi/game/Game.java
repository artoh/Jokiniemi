package artoh.jokiniemi.game;

import artoh.jokiniemi.ai.AIInterface;

/**
 * Pelin sääntöihin liittyvän logiikan toteutus
 * 
 * @author ahyvatti
 */
public class Game {

    /**
     * Peliluokan alustaja
     * 
     * @param startPlacer Etsivien aloituspaikat määrittelevä luokka
     */
    public Game(StartPlaceInterface startPlacer) {
        this.gameLog = new GameLog();
        this.gameboard = new GameBoard();
        this.startplacer = startPlacer;
    }    
    
    public enum GameStatus {
        /**
         * Peli ei ole vielä alkanut
         */
        NOT_STARTED,
        /**
         *  Peli on käynnissä
         */
        RUNNING,
        /**
         * Peli on päättynyt siihen, että kaikki vuorot on pelattu,
         * eikä Mr X ole jäänyt kiinni. Mr X (tietokone) voitti pelin.
         */
        MRX_WINS,
        /**
         * Etsivät saivat Mr X:n kiinni. Etsivät voittivat pelitn.
         */
        DETECTIVES_WIN
    }
    
    /**
     * Aloittaa uuden pelin
     * 
     * @param detectives Etsivien lukumäärä
     * @param ai Tekoälyolio
     */
    public void startGame(int detectives, AIInterface ai) {
        
        this.blackCardsLeft = Game.BLACK_CARDS_TOTAL;
        this.doubleCardsLeft = Game.DOUBLE_CARDS_TOTAL;
        
        this.taxiTicketsLeft = Game.TAXI_TICKETS_TOTAL;
        this.busTicketsLeft = Game.BUS_TICKETS_TOTAL;
        this.undergroudTicketsLeft = Game.UNDERGROUD_TICKETS_TOTAL;
        
        this.detectivesCount = detectives;
        this.ai = ai;
        this.gameLog.newGame(detectives, startplacer);
                
        this.status = GameStatus.RUNNING;

        ai.startGame(this);
        ai.doAITurn();
    }
    
    /**
     * Tekee siirron.
     * 
     * Käyttöliittymä kutsuu tätä funktiota, kun etsivä tekee siirron.
     * Kun kaikki etsivät ovat tehneet omat siirtonsa, vuoro päättyy ja
     * Game kutsuu tekoälyn AI.doAITurn()-funktiota, joka puolestaan
     * kutsuu tätä funktiota ilmoittaen tekoälyn valitseman siirron.
     *  
     * Kun tekoäly käyttää tuplausta, kutsuu se tätä funktiota kaksi kertaa
     * peräkkäin: ensin doubled-parametrilla true ja sitten false.
     * 
     * @param player Pelaaja (0: MrX, 1...n etsivät)
     * @param square Peliruudun numero, johon siirrytään
     * @param vehicle Lipputyyppi, jota käytetään siirtoon
     * @param doubled Tosi, kun Mr X käyttää tuplausta ja tämä on tuplausvuoroista ensimmäinen-
     * @return Pelin tila tämän siirron (ja sitä mahdollisesti seuraavan tekoälyn siirron) jälkeen
     */
    public GameStatus doMove(int player, int square, Vehicle vehicle, boolean doubled) {

        gameLog.logTurn(player, square, vehicle, doubled);        
        
        if (player == 0) {
            if (vehicle == Vehicle.BLACK_CARD) {
                this.blackCardsLeft--;
            } 
            if (doubled) {
                this.doubleCardsLeft--;
            }
        } else if (player > BOBBIES) {
            if (log().currentTurn() > 1 && log().position(player, log().currentTurn() - 2) == square) {
                ; // Free move!
            } else if (vehicle == Vehicle.TAXI) {
                this.taxiTicketsLeft--;
            } else if (vehicle == Vehicle.BUS) {
                this.busTicketsLeft--;
            } else if (vehicle == Vehicle.UNDERGROUD) {
                this.undergroudTicketsLeft--;
            }
        }
        
        checkStatus();
        
        if (player > 0 &&
            this.status == GameStatus.RUNNING &&
            this.detectivesMoved == this.detectivesCount)  {
            ai.doAITurn();                     
        }
        
        return this.status;
    }
    
    /**
     * Mr X:llä olevien tuplauskorttien lukumäärä
     * 
     * Mr X pystyy tuplauskortilla tekemään kaksi siirtoa peräkkäin ilman,
     * että etsivillä on vuoroa niiden välissä.
     * 
     * @return Korttien lukumäärä
     */
    public int doubleCardsLeft() {
        return this.doubleCardsLeft;
    }
    
    /**
     * Mr X:llä olevien mustien lippujen määrä
     * 
     * Kun Mr X liikkuu mustalla lipulla, eivät etsivät tiedä, mitä kulkuneuvoa
     * Mr X on käyttänyt, ja etsivien on siten vaikeampaa päätellä Mr X:n
     * sijaintia. Mustilla lipuilla pystyy liikkumaan myös lauttaa
     * käyttäen.
     * 
     * @return Lippujen lukumäärä
     */
    public int blackCardsLeft() {
        return this.blackCardsLeft;
    }
    
    /**
     * Etsivillä jäljellä olevien lippujen määrä
     * 
     * @param ticket Lipputyyppi (Vehicle.TAXI, Vehicle.BUS tai Vehicle.UNDERGROUD)
     * @return Jäljellä olevien lippujen määrä
     */
    public int ticketsLeft(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            return this.taxiTicketsLeft;
        } else if (ticket == Vehicle.BUS) {
            return this.busTicketsLeft;
        } else if (ticket == Vehicle.UNDERGROUD) {
            return this.undergroudTicketsLeft;
        }
        return 0;
    }
    
    
    /**
     * Peliloki
     * 
     * Pelilokissa on tieto kaikkien pelaajien siirroista sekä
     * peli pituudesta ja niistä vuoroista, jolloin Mr X näyttäytyy
     * 
     * @return Game log -olio
     */
    public GameLog log() {
        return this.gameLog;
    }        
    
    /**
     * Pelilauta
     * 
     * Pelilauta tuntee peliruutujen väliset yhteydet
     * 
     * @return Game board -olio
     */
    public GameBoardInterface gameBoard() {
        return this.gameboard;
    }
    
    /**
     * Aloituspaikat
     * 
     * Olio, joka tuntee pelin mahdolliset aloituspaikat sekä
     * arpoo pelin alussa Mr X:n ja etsivien aloituspaikat
     * 
     * @return Aloituspaikat määrittelevä olio
     */
    public StartPlaceInterface startPlacer() {
        return this.startplacer;
    }
    
    /**
     * Etsivien lukumäärä
     * @return Etsivien lukumäärä
     */
    public int detectives() {
        return this.detectivesCount;
    }
    
    /**
     * Pelin tila (ei aloitettu, käynnissä, Mr X voitti, etsivät voittivat)
     * @return Pelin tila
     */
    public GameStatus gameStatus() {
        return this.status;
    }
    
    /**
     * Päivittää pelin tilan
     * 
     * - Laskee, kuinka moni etsivä on jo liikkunut tällä vuorolla
     * - Tarkastaa, onko joku etsivistä samassa ruudussa kuin Mr X: jos
     *   näin on, etsivät voittavat pelin.
     * - Tarkastaa, onko kaikki vuorot pelattu siten, että Mr X on yhä
     *   vapaana: jos näin on, Mr X voitti pelin.
     * 
     * @return Pelin tila
     */
    private GameStatus checkStatus() {
        
        int mrXposition = gameLog.currentPosition(0);
        this.detectivesMoved = 0;
        
        for (int i = 1; i <= this.detectivesCount; i++) {
            if (gameLog.currentPosition(i) == mrXposition) {
                this.status = GameStatus.DETECTIVES_WIN;
            } else if (gameLog.position(i, gameLog.currentTurn()) > 0) {
                this.detectivesMoved++;
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

    public final static int BOBBIES = 2;
    
    public final static int BLACK_CARDS_TOTAL = 5;
    public final static int DOUBLE_CARDS_TOTAL = 2;
    
    public final static int TAXI_TICKETS_TOTAL = 22;
    public final static int BUS_TICKETS_TOTAL = 16;
    public final static int UNDERGROUD_TICKETS_TOTAL = 8;
    
    private int blackCardsLeft;
    private int doubleCardsLeft;
    
    private int detectivesCount;
    private int detectivesMoved;
    
    private int taxiTicketsLeft;
    private int busTicketsLeft;
    private int undergroudTicketsLeft;
    
    private GameStatus status = GameStatus.NOT_STARTED;
    
}
