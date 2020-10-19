package maquis.game;

public enum GamePhase {
    GAME_OVER("GAME OVER"),
    SETUP("SETUP"),
    // Main Phases
    PLACE_AGENTS("PLACE AGENTS"),
    PERFORM_ACTIONS("PERFORM ACTIONS"),
    END_TURN("Next Phase");

    private String name;
    GamePhase(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
