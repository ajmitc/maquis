package maquis.view;

import maquis.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnNewGame = new JButton("New Game");

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

    public JButton getBtnNewGame(){return btnNewGame;}
}
