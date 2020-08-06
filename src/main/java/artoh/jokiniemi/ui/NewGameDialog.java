/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ui;

import artoh.jokiniemi.ai.AIInterface;
import artoh.jokiniemi.ai.SimpleHeuristicAI;
import artoh.jokiniemi.ai.VeryStupidAI;
import artoh.jokiniemi.algorithm.BoardDistanceInterface;
import artoh.jokiniemi.algorithm.FloydWarshallDistance;
import artoh.jokiniemi.algorithm.LinealCongruentialGenerator;
import artoh.jokiniemi.algorithm.RandomizeInterface;
import artoh.jokiniemi.game.Game;
import com.sun.java.swing.plaf.gtk.GTKConstants;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 *
 *  Dialog to start a new game
 * 
 *  User select count of detectives and level of the game.
 * 
 * @author arto
 */
public class NewGameDialog {
    
    Dialog<Boolean> dialog;

    private ToggleGroup detectiveGroup;
    private RadioButton detectives4;
    private RadioButton detectives5;
    
    private ToggleGroup levelGroup;
    private RadioButton veryEasyLevel;
    private RadioButton heuristicLevel;
    
    /**
     * Init the dialog
     */
    NewGameDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Uusi peli");
        dialog.setHeaderText("Valitse etsivien määrä ja käytettävä tekoäly");
        
        initDetectiveCoutSelection();
        initLevelSelection();
        
        Separator separator = new Separator(Orientation.HORIZONTAL);
        VBox vbox = new VBox(detectives4, detectives5, separator, veryEasyLevel, heuristicLevel);
        
        dialog.getDialogPane().setContent(vbox);
        
        ButtonType startButtonType = new ButtonType("Aloita peli", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(startButtonType);        
        
    }
    
    /**
     * Init radio buttons to select detective count
     */
    void initDetectiveCoutSelection() {
        detectiveGroup = new ToggleGroup();
        detectives4 = new RadioButton("4 etsivää");
        detectives4.setToggleGroup(detectiveGroup);
        detectives4.setSelected(true);
        
        detectives5 = new RadioButton("5 etsivää");
        detectives5.setToggleGroup(detectiveGroup);

    }
    
    /**
     * Init radio button to select level
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
     * Start new game with selections in the dialog
     * 
     * @param game Game object
     */
    void startGame(Game game) {
        int detectives = detectives4.isSelected() ? 4 : 5;
        RandomizeInterface randomizer = new LinealCongruentialGenerator();
        AIInterface ai;
        
        if (veryEasyLevel.isSelected()) {
             ai = new VeryStupidAI(randomizer);
        } else {
            BoardDistanceInterface distances = new FloydWarshallDistance();
            distances.init(game.gameBoard());
            ai = new SimpleHeuristicAI(distances, randomizer);
        }
        
        game.startGame(detectives, ai);
    }
    
    /**
     * Static function to show the dialog and start a new game.
     * @param game 
     */
    static public void newGame(Game game) {
        
        NewGameDialog newgame = new NewGameDialog();
        if (newgame.dialog.showAndWait() != null) {        
            newgame.startGame(game);
        }
    }
    
}
