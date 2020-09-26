package maquis.action;

import maquis.Model;
import maquis.game.Game;
import maquis.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewGameAction extends AbstractAction {
    private Model model;
    private View view;

    public NewGameAction(Model model, View view){
        super("New Game");
        this.model = model;
        this.view = view;
        putValue(Action.SHORT_DESCRIPTION, "New game");
        putValue(Action.ACCELERATOR_KEY, "N");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        model.setGame(new Game());
        view.showGame();
        view.refresh();
    }
}
