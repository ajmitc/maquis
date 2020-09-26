package maquis.view;

import maquis.Model;

import javax.swing.*;
import java.awt.*;

public class View {
    public static final String MAINMENU = "Mainmenu";
    public static final String GAME = "Game";

    private static final int MISSION_DETAILS_WIDTH = 470;

    private Model model;
    private JFrame frame;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    public View(Model model){
        this.model = model;
        this.frame = new JFrame();

        mainMenuPanel = new MainMenuPanel(model, this);
        gamePanel = new GamePanel(model, this);

        frame.setTitle("Maquis");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setSize(1200 + MISSION_DETAILS_WIDTH, 885 + BoardPanel.MISSION_AREA_HEIGHT);
        frame.setSize(ViewUtil.getScreenSize());
        frame.getContentPane().setLayout(new CardLayout());
        frame.getContentPane().add(mainMenuPanel, MAINMENU);
        frame.getContentPane().add(gamePanel, GAME);
    }

    public void refresh(){
        gamePanel.refresh();
    }

    public void showMainMenu(){
        showPanel(MAINMENU);
    }

    public void showGame(){
        gamePanel.init();
        showPanel(GAME);
    }

    public void showPanel(String name){
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), name);
    }

    public Model getModel() {
        return model;
    }

    public JFrame getFrame() {
        return frame;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
