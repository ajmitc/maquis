package maquis.view;

import maquis.Model;
import maquis.view.BoardPanel;
import maquis.view.MissionDetailsPanel;
import maquis.view.View;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;
    private BoardPanel boardPanel;
    private MissionDetailsPanel missionDetailsPanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;
        boardPanel = new BoardPanel(model, view);
        missionDetailsPanel = new MissionDetailsPanel(model, view);

        add(boardPanel, BorderLayout.CENTER);
        add(missionDetailsPanel, BorderLayout.EAST);
    }

    public void init(){
        missionDetailsPanel.init();
    }

    public void refresh(){
        boardPanel.refresh();
        missionDetailsPanel.refresh();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
