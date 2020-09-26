package maquis.view;

import maquis.Model;
import maquis.action.ExitGameAction;
import maquis.action.NewGameAction;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private Model model;
    private View view;

    private JButton btnExit;
    private JButton btnNewGame;

    public MainMenuPanel(Model model, View view){
        super(new GridLayout(1, 2));
        this.model = model;
        this.view = view;

        Image bi = ImageUtil.load("cover3.jpg");
        JLabel coverLabel = new JLabel(new ImageIcon(bi));

        btnExit = new JButton(new ExitGameAction());
        btnNewGame = new JButton(new NewGameAction(model, view));

        JPanel buttonpanel = new JPanel();
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(400, 100, 400, 100));
        new GridBagLayoutHelper(buttonpanel)
                .setAnchor(GridBagConstraints.CENTER)
                .setFill(GridBagConstraints.NONE)
                .setWeightY(1)
                .add(btnNewGame)
                .nextRow()
                .add(btnExit)
                .nextRow();

        add(coverLabel);
        add(buttonpanel);
    }
}
