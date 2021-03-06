package artoh.jokiniemi.ui;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.ai.SimpleHeuristicAI;
import artoh.jokiniemi.ai.VeryStupidAI;
import artoh.jokiniemi.algorithm.AppliedFloydWarshallDistance;
import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.algorithm.LinearCongruentialGenerator;
import artoh.jokiniemi.game.Game;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 *
 * Dialogi uuden pelin aloittamiseen
 * 
 * Käyttäjä valitsee etsivien määrän ja pelin tason.
 *  
 * @author arto
 */
public class NewGameDialog {
    
    Dialog<Boolean> dialog;
    
    private ToggleGroup levelGroup;
    private RadioButton veryEasyLevel;
    private RadioButton heuristicLevel;
    
    /**
     * Rakentaja
     */
    NewGameDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Uusi peli");
        dialog.setHeaderText("Valitse käytettävä tekoäly");
        
        initLevelSelection();
        
        VBox vbox = new VBox(veryEasyLevel, heuristicLevel);
        
        dialog.getDialogPane().setContent(vbox);
        
        ButtonType startButtonType = new ButtonType("Aloita peli", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(startButtonType);  
        
        // Jotta KDE-ympäristössä dialogi näkyisi kokonaan
        // https://stackoverflow.com/questions/55190380/javafx-creates-alert-dialog-which-is-too-small
        dialog.setResizable(true);
        dialog.onShownProperty().addListener(e -> {
            Platform.runLater(() -> dialog.setResizable(false));
        });
        
    }
    

    /**
     * Alustaa valintanapit tasolle
     */
    void initLevelSelection() {
        levelGroup = new ToggleGroup();
        veryEasyLevel = new RadioButton("Typerä tekoäly");
        veryEasyLevel.setToggleGroup(levelGroup);        
        
        heuristicLevel = new RadioButton("Heuristinen tekoäly");
        heuristicLevel.setToggleGroup(levelGroup);
        heuristicLevel.setSelected(true);
    }
    
    /**
     * Aloittaa uuden pelin dialogin valintojen mukaisesti
     * 
     * @param game Game -olio
     */
    void startGame(Game game) {
        AIInterface ai;
        
        if (veryEasyLevel.isSelected()) {
            ai = new VeryStupidAI(new LinearCongruentialGenerator());
        } else {
            BoardDistanceInterface distances = new AppliedFloydWarshallDistance();
            distances.init(game.gameBoard());
            ai = new SimpleHeuristicAI(distances);
        }
        
        game.startGame(4, ai);
    }
    
    /**
     * Staattinen funktio dialogin näyttämiseen ja pelin aloittamiseen.
     * 
     * Pääikkuna kutsuu tätä funktiota, kun käyttäjä painaa "Uusi peli" -nappia
     * 
     * @param game Game-olio
     */
    static public void newGame(Game game) {
        
        NewGameDialog newgame = new NewGameDialog();
        if (newgame.dialog.showAndWait() != null) {        
            newgame.startGame(game);
        }
    }
    
}
