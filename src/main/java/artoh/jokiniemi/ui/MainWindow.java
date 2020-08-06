
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Sovelluksen pääikkuna
 * 
 * @author arto
 */
public class MainWindow {
   
    /**
     * Muodostaja
     * @param game Peliolio
     */
    public MainWindow(Game game) {
        this.game = game;
        this.widgets = new LogWidget[6][game.log().turnsTotal()];        
    }
    
    /**
     * X-koordinaatti etsivän sarakkeelle ikkunassa
     * @param detective Etsivät numero
     * @return Sarakkeen x-koordinaatti
     */
    private int getXForDetective(int detective) {
        return (60 + 140 * detective + (detective > 0 ? 15 : 0) + (detective > 2 ? 15 : 0));
    }
    
    /**
     * Alustaa LogWidgetit pelin etenemisen seuraamiseen
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
     * Piirtää vuoronumerot vasempaan laitaan
     * Ympyröidyt numerot ovat vuoroja, joilla X näyttäytyy
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
    
    private static Arc createArc(int x, int y, int startAngle, Color color) {
        Arc arc = new Arc();
        arc.setCenterX(x);
        arc.setCenterY(y);
        arc.setRadiusX(32);
        arc.setRadiusY(32);
        arc.setStartAngle(startAngle);
        arc.setLength(360 / 3);
        arc.setFill(color);
        arc.setType(ArcType.ROUND);
        return arc;
    }
    
    public static void addBobbyCircle(Group group, int x, int y) {
        group.getChildren().add(createArc(x, y, 0, Color.YELLOW));
        group.getChildren().add(createArc(x, y, 360 / 3, Color.CYAN));
        group.getChildren().add(createArc(x, y, 360 / 3 * 2, Color.RED));
    }
    
    public static Color getPlayerColor(int detective) {
        switch(detective) {
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.YELLOW;
            default:
                return Color.RED;
        }
    }
    
    public static void addPlayerCircle(Group group, int detective, int x, int y) {
        if (detective <= 2) {
            addBobbyCircle(group, x, y);
        }
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(24);
        circle.setFill(getPlayerColor(detective));
        group.getChildren().add(circle);
    }
    
    
    /**
     * Piirtää etsivien värikuvakkeet
     * 
     */
    private void drawTitles() {
        Text xt = new Text(60, 30, "Mr. X");
        xt.setFont(Font.font("Sans", FontWeight.LIGHT, 32));
        mainGroup.getChildren().add(xt);
        
        for (int detective = 1; detective <= 4; detective++) {
            addPlayerCircle(mainGroup, detective, getXForDetective(detective) + 60, 24);
            
        }        
    }
    
    /**
     * Alustaa "Uusi peli"-napin sekä statusalueen
     */
    private void drawStatusArea() {
        Button newButton = new Button("Uusi peli");
        newButton.setTranslateY(-45);
        mainGroup.getChildren().add(newButton);
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                NewGameDialog.newGame(game);
                update();
            }            
        });
        
        statusText = new Text(150, -25, "");
        mainGroup.getChildren().add(statusText);
        
        Line line = new Line(-20, -10, 1000, -10);
        mainGroup.getChildren().add(line);        
        
        updateStatus();
    }
    
    private void initTicketsArea() {
        taxiTickets = new TicketWidget(mainGroup, Vehicle.TAXI, 800, 0);
        busTickets = new TicketWidget(mainGroup, Vehicle.BUS, 800, 70);
        metroTickets = new TicketWidget(mainGroup, Vehicle.UNDERGROUD, 800, 140);
        
        metroTickets.setAmount(8);
        busTickets.setAmount(16);
        taxiTickets.setAmount(22);
    }
    
    private void updateTickets() {
        taxiTickets.setAmount(game.ticketsLeft(Vehicle.TAXI));
        busTickets.setAmount(game.ticketsLeft(Vehicle.BUS));
        metroTickets.setAmount(game.ticketsLeft(Vehicle.UNDERGROUD));
    }
    
    /**
     * Alustaa tapahtumakäsittelijän, jolla seurataan etsivän siirtoon
     * käytettyä hiiren napsautusta
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
                    if (detective <= game.detectives() &&
                        game.log().position(detective, game.log().currentTurn()) == 0) {
                        MoveWindow.doTurn(thisWindow, game, detective);
                    }                    
                }                
            }            
        };
        mainGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);        
    }
    
    /**
     * Päivittää statusviestin
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
     * Päivittää Mr X:n lokin
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
     * Päivittää etsivien lokit
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
     * Päivittää lokit sekä statiustekstit.
     */
    public void update() {
        updateStatus();
        updateMisterX();
        updateDetectives();
        updateTickets();
    }
    
    /**
     * Alustaa pääikkunana
     * @param window Stage-olio
     */
    public void init(Stage window) {
        window.setTitle("Jokiniemi");
        
        mainGroup = new Group();
        
        initWidgets();
        drawTurnNumbers();
        drawTitles();
        drawStatusArea();
        initTicketsArea();
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
    
    TicketWidget taxiTickets;
    TicketWidget busTickets;
    TicketWidget metroTickets;
}
