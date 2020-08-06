package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ikkuna, jolla kysytään etsivän seuraavaa siirtoa
 * 
 * MainWindow käyttää saattista doTurn()-funktiota, kun käyttäjä
 * napsauttaa etsivät saraketta. Silloin käyttäjälle näytetään ikkuna,
 * josta käyttäjä voi valita käytettävän yhteyden (kulkuneuvo ja kohde)
 * 
 * @author arto
 */
public class MoveWindow {
    
    /**
     * Muodostaja
     * 
     * @param mainWindow MainWindow-olio
     * @param game Game-olio
     * @param detective Etsivän numero (1..n)
     */
    public MoveWindow(MainWindow mainWindow, Game game, int detective) {
        this.stage = new Stage();
        
        this.game = game;
        this.detective = detective;
        this.mainWindow = mainWindow;   
        this.turn = game.log().currentTurn();
        
        Group group = initWidgets();
        Scene scene = new Scene(group);
        this.stage.setScene(scene);  
        this.stage.setTitle("Valitse siirto etsivälle " + detective);
    }
    
    /**
     * Onko ruudussa jo etsivää
     * @param square Peliruudun numero
     * @return Tosi, jos ruutu on jo varattu
     */
    private boolean isReserved(int square) {
        for (int i = 1; i <= game.detectives(); i++) {
            if (game.log().currentPosition(i) == square) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Alustaa widgetin seuraavan siirron valitsemista varten
     * 
     * Widgeteissa on kaikki mahdolliset siirrot. Ne on järjestetty
     * sarakkeisiin kulkuneuvojen mukaisesti.
     * 
     * @return Group jossa kaikki widgetit ovat
     */
    private Group initWidgets() {
        Group group = new Group();
        
        int taxiIndex = 0;
        int busIndex = 0;
        int ugroundIndex = 0;
        
        int currentPosition = game.log().currentPosition(this.detective);
        
        for (int i = 0; i < game.gameBoard().connectionsCount(currentPosition); i++) {
            Vehicle vehicle = game.gameBoard().connectionVehicle(currentPosition, i);
            int squareTo = game.gameBoard().connectionTo(currentPosition, i);
            
            if (vehicle == Vehicle.FERRY || isReserved(squareTo)) {
                continue;
            }            
            
            MoveWidget widget = new MoveWidget(vehicle, squareTo, this);
            group.getChildren().add(widget);
            if (vehicle == Vehicle.TAXI) {
                widget.setTranslateY(70 * taxiIndex++);
            } else if (vehicle == Vehicle.BUS) {
                widget.setTranslateY(70 * busIndex++);
                widget.setTranslateX(140);
            } else if (vehicle == Vehicle.UNDERGROUD) {
                widget.setTranslateY(70 * ugroundIndex++);
                widget.setTranslateX(140 * 2);
            }
        }
        
        return  group;
    }
    
    /**
     * Siirtää etsivää. Kutsutaan, kun käyttäjä klikkaa widgetiä.
     * 
     * @param vehicle Valittu kulkuneuvo
     * @param square  Valittu kohderuutu (pelilaudan ruutu)
     */
    public void doMove(Vehicle vehicle, int square) {
        if (game.log().position(this.detective, turn) == 0 &&
            game.log().currentTurn() == turn) {
            game.doMove(this.detective, square, vehicle, false);
        }
        stage.close();
        mainWindow.update();
    }
    
    /**
     * Näyttää ikkunan ja odottaa siirtoa
     */
    public void exec() {
        this.stage.showAndWait();
    }
        
    private final Stage stage;
    private final Game game;
    private final int detective;
    private final MainWindow mainWindow;
    private final int turn;
    
    /**
     * Kysyy etsivän seuraavaa siirtoa, sekä tekee siirron.
     * 
     * MainWindow kutsuu tätä staattista funktiota, kun
     * käyttäjä haluaa valita seuraavan siirtonsa.
     * 
     * Käyttäjälle näytetään ikkuna, jossa on painikkeet jokaiselle
     * mahdolliselle siirrolle. Kun käyttäjä painaa jotain painiketta,
     * tehdään siirto.
     * 
     * 
     * @param mainWindow MainWindow -olio
     * @param game Game -olio
     * @param detective Etsivän numero (1..n)
     */
    static public void doTurn(MainWindow mainWindow, Game game, int detective) {
        MoveWindow move = new MoveWindow(mainWindow, game, detective);
        move.exec();
    }
    
}
