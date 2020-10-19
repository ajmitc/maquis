package maquis.view;

import maquis.Model;
import maquis.view.BoardPanel;
import maquis.view.MissionDetailsPanel;
import maquis.view.View;

import javax.swing.*;

public class GamePanel extends JSplitPane{
    private Model model;
    private View view;
    private BoardPanel boardPanel;
    private MissionDetailsPanel missionDetailsPanel;

    public GamePanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
        boardPanel = new BoardPanel(model, view);
        missionDetailsPanel = new MissionDetailsPanel(model, view);

        setLeftComponent(boardPanel);
        setRightComponent(missionDetailsPanel);
        //add(boardPanel, BorderLayout.CENTER);
        //add(missionDetailsPanel, BorderLayout.EAST);
    }

    public void init(){
        missionDetailsPanel.init();
        setDividerLocation(1200);
    }

    public void refresh(){
        boardPanel.refresh();
        missionDetailsPanel.refresh();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
