package maquis.view;

import maquis.game.mission.Mission;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

public class MissionDetailPanel extends JPanel {
    private Mission mission = null;
    private JLabel lblName;
    private JLabel lblDifficulty;
    private JTextArea lblDescription;
    private JTextArea lblRequirements;
    private JLabel lblCompleted;
    private GridBagLayoutHelper gridBagLayoutHelper;
    private List<MissionRequirementPanel> missionRequirementPanelList = new ArrayList<>();

    public MissionDetailPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(BoardPanel.MISSION_AREA_HEIGHT, 0, 0, 0));
    }

    public void init(Mission mission){
        this.mission = mission;

        //JLabel lblNameKey = new JLabel("Name");
        //lblNameKey.setFont(ViewUtil.FORM_KEY_FONT);
        JLabel lblDifficultyKey = new JLabel("Difficulty");
        lblDifficultyKey.setFont(ViewUtil.FORM_KEY_FONT);
        JLabel lblDescriptionKey = new JLabel("Description");
        lblDescriptionKey.setFont(ViewUtil.FORM_KEY_FONT);
        JLabel lblRequirementsKey = new JLabel("Completion");
        lblRequirementsKey.setFont(ViewUtil.FORM_KEY_FONT);
        JLabel lblCompletedKey = new JLabel("Completed?");
        lblCompletedKey.setFont(ViewUtil.FORM_KEY_FONT);

        lblName = new JLabel();
        lblName.setFont(ViewUtil.FORM_TITLE_FONT);
        lblDifficulty = new JLabel();
        lblDifficulty.setFont(ViewUtil.FORM_VALUE_FONT);

        lblDescription = new JTextArea();
        lblDescription.setFont(ViewUtil.FORM_VALUE_FONT_ITALICS);
        lblDescription.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lblDescription.setColumns(20);
        lblDescription.setLineWrap(true);
        lblDescription.setWrapStyleWord(true);

        lblRequirements = new JTextArea();
        lblRequirements.setFont(ViewUtil.FORM_VALUE_FONT);
        lblRequirements.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lblRequirements.setColumns(20);
        lblRequirements.setLineWrap(true);
        lblRequirements.setWrapStyleWord(true);

        lblCompleted = new JLabel();
        lblCompleted.setFont(ViewUtil.FORM_VALUE_FONT);

        lblName.setText(mission.getName());
        lblDifficulty.setText(mission.getDifficulty() == 1 ? "Normal" : "Hard");
        lblDescription.setText(mission.getDescription());
        lblRequirements.setText(mission.getCompletionRequirements());
        lblCompleted.setText("No");

        // Build Mission Requirements panel
        JPanel missionRequirementsPanel = new JPanel(new GridLayout(this.mission.getMissionRequirements().size(), 1));
        for (Mission.MissionRequirement missionRequirement: this.mission.getMissionRequirements()){
            MissionRequirementPanel mrp = new MissionRequirementPanel(missionRequirement);
            missionRequirementsPanel.add(mrp);
            missionRequirementPanelList.add(mrp);
        }

        gridBagLayoutHelper =
                new GridBagLayoutHelper(this, true)
                        .setExternalPadding(0, 0, 10, 10)
                        //.add(lblNameKey)
                        .setGridWidth(2)
                        .add(lblName).nextRow()
                        .resetGridWidth()
                        .add(lblDifficultyKey).add(new JScrollPane(lblDifficulty)).nextRow()
                        .add(lblDescriptionKey).add(new JScrollPane(lblDescription)).nextRow()
                        .add(lblRequirementsKey).add(lblRequirements).nextRow()
                        .setGridWidth(2).add(missionRequirementsPanel).resetGridWidth().nextRow()
                        .add(lblCompletedKey).add(lblCompleted).nextRow()
        ;
    }

    public void refresh(){
        if (mission != null){
            if (mission.isCompleted()){
                lblCompleted.setText("Yes");
            }
            for (MissionRequirementPanel missionRequirementPanel: missionRequirementPanelList){
                missionRequirementPanel.refresh();
            }
        }
    }

    public boolean isInitialized(){
        return mission != null;
    }

    public static class MissionRequirementPanel extends JPanel{
        private Mission.MissionRequirement missionRequirement;
        private JCheckBox cbCompleted = new JCheckBox();

        public MissionRequirementPanel(Mission.MissionRequirement missionRequirement){
            super(new FlowLayout(FlowLayout.LEFT));
            this.missionRequirement = missionRequirement;
            cbCompleted.setText(missionRequirement.getDescription());
            this.missionRequirement.setCheckBox(cbCompleted);

            // Make the checkbox read-only
            MouseListener[] ml = (MouseListener[]) cbCompleted.getListeners(MouseListener.class);
            for (int i = 0; i < ml.length; i++)
                cbCompleted.removeMouseListener( ml[i] );
            InputMap im = cbCompleted.getInputMap();
            im.put(KeyStroke.getKeyStroke("SPACE"), "none");
            im.put(KeyStroke.getKeyStroke("released SPACE"), "none");

            add(cbCompleted);
        }

        public void refresh(){
            cbCompleted.setSelected(missionRequirement.isCompleted());
        }
    }
}
