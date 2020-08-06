
package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Vehicle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Widget, jolla kysytään pelaajan seuraavaa siirtoa
 * @author arto
 */
public class MoveWidget extends LogWidget {
    
    /**
     * Alustaa ja näyttää widgetin, jolla kysytään pelaajan seuraavaa
     * siirtoa.
     * 
     * @param vehicle Kulkuneuvo, jota käytettäisiin
     * @param square Pelilaudan ruudun numero, johon siirryttäisiin
     * @param moveWindow MoveWindow-olio
     */
    public MoveWidget(Vehicle vehicle, int square, MoveWindow moveWindow) {        
        
        show(vehicle, square, false, true);
        
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                moveWindow.doMove(vehicle, square);
            }            
        };
        
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }        
}
