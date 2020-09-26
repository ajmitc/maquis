package maquis;

import maquis.view.View;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String ... args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                Model model = new Model();
                View view = new View(model);
                new Controller(model, view);
                view.getFrame().setVisible(true);
            }
        });
    }
}
