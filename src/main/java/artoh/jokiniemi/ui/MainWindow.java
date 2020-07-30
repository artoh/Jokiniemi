/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Game;
import artoh.jokiniemi.game.Vehicle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Main window of the application
 * 
 * @author arto
 */
public class MainWindow {
   
    /**
     * Constructor
     * @param game The Game object 
     */
    public MainWindow(Game game) {
        this.game = game;
        this.widgets = new LogWidget[6][game.log().turnsTotal()];
    }
    
    /**
     * Get the X position of detectives column
     * @param detective Number of detective
     * @return Column x-position
     */
    private int getXForDetective(int detective) {
        return (60 + 140 * detective + (detective > 0 ? 15 : 0));
    }
    
    /**
     * Initilize LogWidgets for logging the game
     */
    private void initWidgets() {
        for (int d = 0; d < 6; d++) {
            for (int t = 0; t < game.log().turnsTotal(); t++) {
                LogWidget wg = new LogWidget();
                mainGroup.getChildren().add(wg);
                
                wg.setTranslateX(getXForDetective(d));
                wg.setTranslateY(60 + 70 * t);
                
                widgets[d][t] = wg;
            }
        }
    }
    
    /**
     * Draw the turn numbers in the left side.
     * Circulated numbers are turns when Mr X is visible
     */
    private void drawTurnNumbers() {        
        for (int turn = 0; turn < game.log().turnsTotal(); turn++) {
            
            if (game.log().isVisibleTurn(turn)) {
                Ellipse e = new Ellipse(15, turn * 70 + 35 + 60, 30, 30);            
                e.setFill(Color.TRANSPARENT);
                e.setStroke(Color.BLACK);
                mainGroup.getChildren().add(e);                
            }
            
            Text text = new Text(0, turn * 70 + 40 + 60, "" + turn);
            text.setFont(Font.font("Sans", FontPosture.REGULAR, 32));
            mainGroup.getChildren().add(text);            
            
        }                
    }
    
    /**
     * Draw the titles of detectives.
     * 
     */
    private void drawTitles() {
        Text xt = new Text(60, 30, "Mr. X");
        xt.setFont(Font.font("Sans", FontWeight.BLACK, 32));
        mainGroup.getChildren().add(xt);
        
        for (int detective = 1; detective < 6; detective++) {
            Ellipse ellipse = new Ellipse(60 + 15 + 50 + 140 * detective, 24, 32, 26);            
            switch (detective) {
                case 1: 
                    ellipse.setFill(Color.GREEN); 
                    break;
                case 2: 
                    ellipse.setFill(Color.YELLOW);
                    break;
                case 3: 
                    ellipse.setFill(Color.BLACK); 
                    break;
                case 4: 
                    ellipse.setFill(Color.BLUE); 
                    break;
                case 5: 
                    ellipse.setFill(Color.RED); 
                    break;
            }
            mainGroup.getChildren().add(ellipse);
            Text text = new Text(60 + 15 + 40 + 140 * detective, 30, "" + detective);
            text.setFont(Font.font("Sans", FontWeight.BLACK, 32));
            text.setFill(Color.WHITE);
            mainGroup.getChildren().add(text);
            
        }        
    }
    
    /**
     * Initialize new game button and status message area.
     */
    private void drawStatusArea() {
        Button newButton = new Button("Uusi peli");
        newButton.setTranslateY(-40);
        mainGroup.getChildren().add(newButton);
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                NewGameDialog.newGame(game);
                update();
            }            
        });
        
        statusText = new Text(150, -20, "");
        mainGroup.getChildren().add(statusText);
        
        Line line = new Line(-20, -5, 900, -5);
        mainGroup.getChildren().add(line);        
        
        updateStatus();
    }
    
    /**
     * Init event handler to handle clicks of the widgets
     */
    public void initEventHandler() {
        MainWindow thisWindow = this;
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>()  {
            @Override
            public void handle(MouseEvent t) {
                if (t.getY() > 50 
                        && t.getX() > 214 
                        && game.gameStatus() == Game.GameStatus.RUNNING) {
                    int detective = (int) ((t.getX() - 75) / 140);
                    if (detective <= game.detectives()) {
                        MoveWindow.doTurn(thisWindow, game, detective);
                    }                    
                }                
            }            
        };
        mainGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);        
    }
    
    /**
     * Update the status message
     */
    public void updateStatus() {
        if (game.gameStatus() == Game.GameStatus.NOT_STARTED) {
            statusText.setText("Aloita peli viereisestä napista.");
        } else if (game.gameStatus() == Game.GameStatus.RUNNING) {
            statusText.setText("Peli on käynnissä. Siirrä etsiviä.");
        } else if (game.gameStatus() == Game.GameStatus.DETECTIVES_WIN) {
            statusText.setText("Etsivät voittivat pelin!"); 
        } else if (game.gameStatus() == Game.GameStatus.MRX_WINS) {
            statusText.setText("Mr. X voitti pelin.");
        }
    }
    
    /**
     * Update log of Mister X
     */
    public void updateMisterX() {
        for (int t = 0; t < game.log().turnsTotal(); t++) {
            boolean hidden = game.gameStatus() == Game.GameStatus.RUNNING ?
                    !game.log().isVisibleTurn(t) : false;
            widgets[0][t].show(game.log().vehicle(0, t), 
                    game.log().position(0, t), hidden, false);
        }
    }
    
    /**
     * Update the log of detectives
     */
    public void updateDetectives() {        
        for (int d = 1; d < 6; d++) {
            for (int t = 0; t < game.log().turnsTotal(); t++) {
                if (d > game.detectives()) {
                    widgets[d][t].show(Vehicle.START_SQUARE, 0, false, false);
                } else if (game.log().position(d, game.log().currentTurn()) == 0 &&
                        t == game.log().currentTurn() &&
                        game.gameStatus() == Game.GameStatus.RUNNING) {
                    widgets[d][t].askForMove();
                } else {
                    widgets[d][t].show(game.log().vehicle(d, t), 
                            game.log().position(d, t), 
                            false, 
                            false);
                }
            }
        }
    }
    
    /**
     * Update the log of Mister X and detectives and status text.
     */
    public void update() {
        updateStatus();
        updateMisterX();
        updateDetectives();
    }
    
    /**
     *  Init the main window
     * @param window Stage object
     */
    public void init(Stage window) {
        window.setTitle("Jokiniemi");
        
        mainGroup = new Group();
        
        initWidgets();
        drawTurnNumbers();
        drawTitles();
        drawStatusArea();
        initEventHandler();        
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainGroup);
        Scene scene = new Scene(scrollPane);
        
        window.setScene(scene);
        window.show();
        
    }
    
    private Game game;
    Group mainGroup;
    private LogWidget[][] widgets;
    Text statusText;
}
