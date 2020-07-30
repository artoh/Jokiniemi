
package artoh.jokiniemi.ui;

import artoh.jokiniemi.game.Vehicle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Widget to ask detectives next move
 * @author arto
 */
public class MoveWidget extends LogWidget {
    
    /**
     * Init and show the widged and init event filter for mouse click
     * @param vehicle Vehicle to choise
     * @param square Targer square to choise
     * @param moveWindow MoveWindow object
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
