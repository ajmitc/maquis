package maquis.view;

import maquis.Model;

import javax.swing.*;
import java.awt.*;

public class MissionDetailsPanel extends JPanel {
    private Model model;
    private View view;

    private MissionDetailPanel missionDetailPanel1;
    private MissionDetailPanel missionDetailPanel2;

    public MissionDetailsPanel(Model model, View view){
        super(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        this.model = model;
        this.view = view;

        missionDetailPanel1 = new MissionDetailPanel();
        missionDetailPanel2 = new MissionDetailPanel();
        add(new JScrollPane(missionDetailPanel1));
        add(new JScrollPane(missionDetailPanel2));
    }

    public void init(){
        missionDetailPanel1.init(model.getGame().getMission1());
        missionDetailPanel2.init(model.getGame().getMission2());
    }

    public void refresh(){
        missionDetailPanel1.refresh();
        missionDetailPanel2.refresh();
    }
}
