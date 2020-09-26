package maquis.game;

import java.awt.Color;

public class Agent {
    public static final Color AGENT_COLOR = Color.WHITE;
    public static final Color AGENT_SELECTED_COLOR = Color.GRAY;
    public static final int AGENT_SIZE = 50;

    private boolean movable;
    private boolean arrested;
    private boolean recruited;
    private int x, y;
    private boolean selected;
    private int selectedXoffset, selectedYoffset;

    public Agent(boolean recruited){
        this.movable = true;
        this.arrested = false;
        this.recruited = recruited;
        this.selected = false;
        this.x = 0;
        this.y = 0;
        this.selectedXoffset = 0;
        this.selectedYoffset = 0;
    }

    public boolean contains(int mx, int my){
        return mx >= x && mx < x + AGENT_SIZE && my >= y && my < y + AGENT_SIZE;
    }

    public void setCoord(int x, int y){
        setX(x);
        setY(y);
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public boolean isArrested() {
        return arrested;
    }

    public void setArrested(boolean arrested) {
        this.arrested = arrested;
    }

    public boolean isRecruited() {
        return recruited;
    }

    public void setRecruited(boolean recruited) {
        this.recruited = recruited;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSelected(int mx, int my) {
        this.selected = true;
        this.selectedXoffset = mx - x;
        this.selectedYoffset = my - y;
    }

    public int getSelectedXoffset() {
        return selectedXoffset;
    }

    public void setSelectedXoffset(int selectedXoffset) {
        this.selectedXoffset = selectedXoffset;
    }

    public int getSelectedYoffset() {
        return selectedYoffset;
    }

    public void setSelectedYoffset(int selectedYoffset) {
        this.selectedYoffset = selectedYoffset;
    }
}
