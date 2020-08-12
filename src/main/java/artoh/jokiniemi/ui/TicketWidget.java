/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Vehicle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author arto
 */
public class TicketWidget {
    private Text amountText;
    private Text ticketText;
    private Rectangle rect;
    
    public TicketWidget(Group group, Vehicle ticket, int x, int y) {
        
        Group myGroup = new Group();
        
        this.rect = new Rectangle(100, 50, colorForTicket(ticket));
        this.rect.setTranslateX(100);
        this.ticketText = new Text(102, 32, LogWidget.ticketName(ticket));
        this.ticketText.setFont(Font.font(null, FontWeight.NORMAL, 24));
        
        this.amountText = new Text(0, 36, "");
        this.amountText.setFont(Font.font(null, FontWeight.BLACK, 36));
        
        myGroup.getChildren().add(this.rect);
        myGroup.getChildren().add(ticketText);
        myGroup.getChildren().add(amountText);
        
        myGroup.setTranslateX(x);
        myGroup.setTranslateY(y);
        group.getChildren().add(myGroup);
        
    }
    
    public static Color colorForTicket(Vehicle ticket) {
        if (ticket == Vehicle.TAXI) {
            return Color.YELLOW;
        } else if (ticket == Vehicle.BUS) {
            return Color.CYAN;
        } else if (ticket == Vehicle.UNDERGROUD) {
            return Color.RED;
        } else {
            return Color.WHITE;
        }            
    }    
    
    public void setAmount(int amount) {
        this.amountText.setText("" + amount + " X");
    }
    
}
