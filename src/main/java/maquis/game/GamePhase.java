package maquis.game;

public enum GamePhase {
    // Main Phases
    PLACE_AGENTS("PLACE AGENTS"),
    PERFORM_ACTIONS("PERFORM ACTIONS"),
    NEXT_PHASE("Next Phase"),

    // Subphases
    START_SUBPHASES("NEVER SHOW THIS PHASE"),
    PERFORM_ACTIONS_SHOOT_MILICE("SHOOT MILICE"),
    GAME_OVER("GAME OVER");

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
