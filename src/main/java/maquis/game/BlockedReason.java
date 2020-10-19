package maquis.game;

public enum BlockedReason {
    UNKNOWN("barricade3.png"),
    PARADE("parade2.jpg"),
    DESTROYED("explosion.png");

    private String iconFilename;
    BlockedReason(String iconFilename){
        this.iconFilename = iconFilename;
    }

    public String getIconFilename() {
        return iconFilename;
    }
}
