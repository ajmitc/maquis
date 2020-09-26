package maquis.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitGameAction extends AbstractAction {
    public ExitGameAction(){
        super("Exit");
        putValue(Action.SHORT_DESCRIPTION, "Exit the game");
        putValue(Action.ACCELERATOR_KEY, "E");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
