/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Window to ask detectives next move
 * 
 * MainWindow uses static doTurn-function when user click the button
 * to select detectives target and vehicle.
 * 
 * @author arto
 */
public class MoveWindow {
    
    /**
     * Constructor 
     * 
     * @param mainWindow MainWindow object
     * @param game Game object
     * @param detective Number of detective (1..n)
     */
    public MoveWindow(MainWindow mainWindow, Game game, int detective) {
        this.stage = new Stage();
        
        this.game = game;
        this.detective = detective;
        this.mainWindow = mainWindow;        
        
        Group group = initWidgets();
        Scene scene = new Scene(group);
        this.stage.setScene(scene);  
        this.stage.setTitle("Valitse siirto etsivälle " + detective);
    }
    
    /**
     * Init MoveWidgets to select next target.
     * 
     * Widgets includes all possible moves and are organized 
     * into columns depending the vehicle.
     * 
     * @return Group including all the widgets
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
            
            if (vehicle == Vehicle.FERRY) {
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
     * Move detective. Called by widget when used clicks widget.
     * 
     * @param vehicle Selected vehicle.
     * @param square  Selected target square (square number)
     */
    public void doMove(Vehicle vehicle, int square) {
        game.doMove(this.detective, square, vehicle, false);
        stage.close();
        mainWindow.update();
    }
    
    /**
     * Show the window and wait for click
     */
    public void exec() {
        this.stage.showAndWait();
    }
        
    private final Stage stage;
    private final Game game;
    private final int detective;
    private final MainWindow mainWindow;
    
    /**
     * Ask for next move and make the move. Called by MainWindow when
     * user wants to select next target.
     * 
     * The window with possible moves will be shown, and when the
     * user click a selection, the move will be done.
     * 
     * @param mainWindow
     * @param game
     * @param detective 
     */
    static public void doTurn(MainWindow mainWindow, Game game, int detective) {
        MoveWindow move = new MoveWindow(mainWindow, game, detective);
        move.exec();
    }
    
}
