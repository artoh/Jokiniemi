package artoh.jokiniemi.ui;

import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.StartPlaceRandomizer;
import artoh.jokiniemi.io.BoardFileReader;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * 
 * Sovelluksen luokka pelin ja käyttöliittymän alustamiseen
 * 
 * @author arto
 */
public class JokiniemiApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Game game = initGame();
        
        if (game != null) {
            MainWindow mainWindow = new MainWindow(game);
            mainWindow.init(stage);
        }
        
    }   

    /**
     * Alustaa pelin säännöt ja pelilaudan
     * @return Alustettu peliolio
     */
    public static Game initGame() {
        RandomizeInterface randomizer = new LinearCongruentialGenerator();
        StartPlaceRandomizer starter = new StartPlaceRandomizer(randomizer);
        BoardFileReader loader = new BoardFileReader();
        
        Game game = new Game(starter);
        
        if (loader.readBoard(game, starter)) {
            return game;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Jokiniemen käynnistäminen epäonnistui.");
            alert.setContentText("Pelilauden lataamisessa tapahtui virhe.");
            alert.showAndWait();
            return null;
        }        
    }
    
    
    
    /**
     * Ohjelman käynnistävä pääfunktio
     * 
     * @param args Käynnistysriviparametrit (ei käytössä)
     */
    public static void main(String[] args) {
        launch(JokiniemiApplication.class);

    }
    
}