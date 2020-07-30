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
 *
 * @author arto
 */
public class MainWindow {
    
    public MainWindow(Game game) {
        this.game = game;
        this.widgets = new LogWidget[6][game.log().turnsTotal()];
    }
    
    
    private int getXForDetective(int detective) {
        return (60 + 140 * detective + (detective > 0 ? 15 : 0));
    }
    
    private void initWidgets() {
        for (int d = 0; d < 6; d++) {
            for (int t=0; t < game.log().turnsTotal(); t++) {
                LogWidget wg = new LogWidget();
                mainGroup.getChildren().add(wg);
                
                wg.setTranslateX(getXForDetective(d));
                wg.setTranslateY(60 + 70 * t);
                
                widgets[d][t] = wg;
            }
        }
    }
    
    private void drawTurnNumbers() {        
        for (int turn = 0; turn < game.log().turnsTotal(); turn++) {
            
            if( game.log().isVisibleTurn(turn)) {
                Ellipse e = new Ellipse(15,turn*70+35+60,30,30);            
                e.setFill(Color.TRANSPARENT);
                e.setStroke(Color.BLACK);
                mainGroup.getChildren().add(e);                
            }
            
            Text text = new Text(0, turn*70+40+60, "" + turn);
            text.setFont(Font.font("Sans", FontPosture.REGULAR, 32));
            mainGroup.getChildren().add(text);            
            
        }                
    }
    
    private void drawTitles() {
        Text xt = new Text(60,30,"Mr. X");
        xt.setFont(Font.font("Sans",FontWeight.BLACK,32));
        mainGroup.getChildren().add(xt);
        
        for(int detective=1; detective<6; detective++) {
            Ellipse ellipse = new Ellipse(60+15+50+140*detective,24,32,26);            
            switch(detective) {
                case 1: ellipse.setFill(Color.GREEN); break;
                case 2: ellipse.setFill(Color.YELLOW); break;
                case 3: ellipse.setFill(Color.BLACK); break;
                case 4: ellipse.setFill(Color.BLUE); break;
                case 5: ellipse.setFill(Color.RED); break;
            }
            mainGroup.getChildren().add(ellipse);
            Text text = new Text(60+15+40+140*detective,30,""+detective);
            text.setFont(Font.font("Sans",FontWeight.BLACK,32));
            text.setFill(Color.WHITE);
            mainGroup.getChildren().add(text);
            
        }        
    }
    
    private void drawStatusArea() {
        Button newButton = new Button("Uusi peli");
        newButton.setTranslateY(-40);
        mainGroup.getChildren().add(newButton);
        newButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                NewGameDialog.newGame(game);
                update();
            }            
        });
        
        statusText = new Text(150,-20,"");
        mainGroup.getChildren().add(statusText);
        
        Line line = new Line(-20,-5,900, -5);
        mainGroup.getChildren().add(line);        
        
        updateStatus();
    }
    
    public void initEventHandler() {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>()  {
            @Override
            public void handle(MouseEvent t) {
                System.out.println("Mouse " + t.getX() + "," + t.getY());
            }            
        };
        mainGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);        
    }
    
    public void updateStatus() {
        if( game.gameStatus() == Game.GameStatus.NOT_STARTED) {
            statusText.setText("Aloita peli viereisestä napista.");
        } else if (game.gameStatus() == Game.GameStatus.RUNNING) {
            statusText.setText("Peli on käynnissä. Siirrä etsiviä.");
        } else if (game.gameStatus() == Game.GameStatus.DETECTIVES_WIN) {
            statusText.setText("Etsivät voittivat pelin!"); 
        } else if (game.gameStatus() == Game.GameStatus.MRX_WINS) {
            statusText.setText("Mr. X voitti pelin.");
        }
    }
    
    
    public void updateMisterX() {
        for (int t=0; t < game.log().turnsTotal(); t++) {
            boolean hidden = game.gameStatus() == Game.GameStatus.RUNNING ?
                    !game.log().isVisibleTurn(t) : false;
            widgets[0][t].show(game.log().vehicle(0, t), 
                    game.log().position(0, t), hidden, false);
        }
    }
    
    public void updateDetectives() {        
        for (int d=1; d<6; d++) {
            for (int t=0; t < game.log().turnsTotal(); t++) {
                if (d > game.detectives()) {
                    widgets[d][t].show(Vehicle.START_SQUARE, 0, false, false);
                } else if(game.log().position(d, game.log().currentTurn()) == 0 &&
                        t == game.log().currentTurn()) {
                    widgets[d][t].askForMove();
                } else {
                    widgets[d][t].show(game.log().vehicle(d, t), 
                            game.log().position(d,t), 
                            false, 
                            false);
                }
            }
        }
    }
    
    public void update() {
        updateStatus();
        updateMisterX();
        updateDetectives();
    }
    
    
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
