package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Vehicle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Widget, joka näyttää yksittäisen lokitapahtuman
 * 
 * @author arto
 */
public class LogWidget extends Group {
    private Rectangle rect;
    private Text vehicleText;
    private Text positionText;
    
    /**
     * Rakentaja
     */
    public LogWidget() {
        rect = new Rectangle(120, 60, Color.TRANSPARENT);
        vehicleText = new Text(20, 20, "");
        
        positionText = new Text(20, 48, "");
        vehicleText.setFont(Font.font(null, FontWeight.BOLD, 18));
        positionText.setFont(Font.font(null, FontWeight.NORMAL, 32));
        
        this.getChildren().add(rect);        
        this.getChildren().add(vehicleText);
        this.getChildren().add(positionText);
    }
    
    /**
     * Näyttää widgetin "Siirrä" -nappina, jolla käyttäjä saa
     * seuraavan vuoron valintaan käytettävän listan
     */
    public void askForMove() {
        rect.setVisible(true);
        rect.setFill(Color.DARKGREY);
        rect.setStroke(Color.BLACK);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        
        positionText.setText("Siirrä");
        positionText.setFill(Color.BLACK);
        vehicleText.setText("");
    }
    
    /**
     * Palauttaa lipputyyppiin liittyvän värin
     * @param ticket
     * @return 
     */
    public static Color ticketColor(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            return Color.YELLOW;
        } else if (ticket == Vehicle.BUS) {
            return Color.CYAN;
        } else if (ticket == Vehicle.UNDERGROUD) {
            return Color.RED;
        } else if (ticket == Vehicle.BLACK_CARD) {
            return Color.BLACK;
        } else if (ticket == Vehicle.DOUBLED) {
            return Color.BLUEVIOLET;
        } else {
            return Color.TRANSPARENT;
        }
    }
    
    public static String ticketName(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            return "TAKSI";
        } else if (ticket == Vehicle.BUS) {
            return "BUSSI";
        } else if (ticket == Vehicle.UNDERGROUD) {
            return "METRO";
        } else if (ticket == Vehicle.BLACK_CARD) {
            return "???";
        } else {
            return "";
        }        
    }
    
    /**
     * Näyttää yksittäisen siirron
     * 
     * @param vehicle Siirrossa käytettävä matkalippu tai kulkuneuvo
     * @param position Pelilaudan ruudun numero
     * @param hidden Tosi, jos tämä on Mr X:n salainen siirto, jossa ei näytetä sijaintia
     * @param button Näyttää tämän ruudun nappulana, jolla siirto valitaan
     */
    public void show(Vehicle vehicle, int position, boolean hidden, boolean button) {
        if (position == 0) {
            rect.setVisible(false);
            positionText.setText("");
            vehicleText.setText("");
        } else {
            rect.setVisible(true);
            rect.setArcHeight(button ? 20 : 0);
            rect.setArcWidth(button ? 20 : 0);
            rect.setStroke(button ? Color.BLACK : Color.TRANSPARENT);
            
            positionText.setText(hidden ? "" : "" + position);
            positionText.setFill(Color.BLACK);
            vehicleText.setFill(Color.BLACK);
            
            rect.setFill(ticketColor(vehicle));
            vehicleText.setText(ticketName(vehicle));
            
            if (vehicle == Vehicle.BLACK_CARD) {
                rect.setFill(Color.BLACK);
                vehicleText.setFill(Color.WHITE);
                positionText.setFill(Color.WHITE);
            } else if (vehicle == Vehicle.DOUBLED) {
                rect.setFill(Color.BLUEVIOLET);
                positionText.setFill(Color.WHITE);
                positionText.setText("2x");
            } else if (position < 0) {
                if (button) {
                    positionText.setText("Poista liikuntakyvytön nappula pelistä");
                    rect.setWidth(300);
                } else {
                    positionText.setText("X");
                }
            } else {
                rect.setFill(Color.WHITE);
            }
        }
    }
}
