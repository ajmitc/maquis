package maquis.game;

import maquis.Model;
import maquis.game.mission.*;
import maquis.view.BoardPanel;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Game {
    private static Logger logger = Logger.getLogger(Game.class.getName());

    public static final int GAME_OVER_TURN = 16;

    // Current game phase
    private GamePhase phase = GamePhase.PLACE_AGENTS;

    // Board holding state of game
    private Board board;
    // Game end at 15
    private int turn;
    // Start at 6, max is 7
    private int morale;
    // Max soldiers is 5
    private int soldiers;
    // Number of Agents player has in the game
    private List<Agent> agents = new ArrayList<>();
    // Player's available resources
    private List<Resource> resources = new ArrayList<>();
    // Patrol Deck
    private PatrolDeck patrolDeck = new PatrolDeck();
    // Mission Deck
    private MissionDeck missionDeck;
    // Missions
    private Mission mission1;
    private Mission mission2;

    // Can the Agents eliminate Milice?
    // Some mission don't allow this until completed (ie. German Shepherds)
    private boolean allowMiliceElimination = true;

    public Game(){
        missionDeck = new MissionDeck(this);
        init();
    }

    public void init(){
        board = new Board();
        turn = 1;
        morale = 6;
        soldiers = 0;
        agents = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            agents.add(new Agent(i < 3));
            Point p = BoardPanel.LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).get(i);
            agents.get(i).setCoord(p.x, p.y);
        }
        agents = Collections.unmodifiableList(agents);

        // Place 3 Agents in Safe House
        board.getLocation(LocationType.SAFE_HOUSE_1).setAgents(agents.subList(0, 3));

        // Draw two missions
        mission1 = missionDeck.draw(Model.getProperty("game.setup.mission1.difficulty", 1));
        mission2 = missionDeck.draw(Model.getProperty("game.setup.mission2.difficulty", 2));
        List<Location> missionLocations = board.getLocationsWithTypes(LocationType.MISSION_1, LocationType.MISSION_2);
        ((MissionLocation) missionLocations.get(0)).setMission(mission1);
        ((MissionLocation) missionLocations.get(1)).setMission(mission2);

        // Set starting resources
        resources.clear();
        for (String resourceString: Model.getProperty("game.setup.resources", new ArrayList<>())) {
            Resource resource = Resource.valueOf(resourceString);
            resources.add(resource);
        }

        // Set starting phase
        phase = GamePhase.PLACE_AGENTS;

        // Setup the missions
        mission1.setup();
        mission2.setup();
    }

    public void restart(){
        board.restart();
        turn = 1;
        morale = 6;
        soldiers = 0;
        for (int i = 0; i < 5; ++i) {
            agents.get(i).setRecruited(i < 3);
            agents.get(i).setArrested(false);
            agents.get(i).setMovable(true);
            agents.get(i).setSelected(false);
            Point p = BoardPanel.LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).get(i);
            agents.get(i).setCoord(p.x, p.y);
        }

        // Place 3 Agents in Safe House
        board.getLocation(LocationType.SAFE_HOUSE_1).setAgents(agents.subList(0, 3));

        // Set starting resources
        resources.clear();
        resources.add(Resource.MONEY);
        resources.add(Resource.WEAPONS);

        // Set starting phase
        phase = GamePhase.PLACE_AGENTS;

        // Restart the missions
        mission1.restart();
        mission2.restart();
    }

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    /**
     * Call this method when the phase is over
     */
    public GamePhase endPhase(){
        int ord = (phase.ordinal() + 1) % GamePhase.values().length;
        phase = GamePhase.values()[ord];
        // Don't enter subphases
        if (phase == GamePhase.START_SUBPHASES)
            phase = GamePhase.PLACE_AGENTS;
        logger.info("Set phase to " + phase);
        return phase;
    }

    public List<Agent> getAgentsInPlay(){
        return agents.stream().filter(a -> a.isRecruited() && !a.isArrested()).collect(Collectors.toList());
    }

    public List<Agent> getMovableAgents(){
        return agents.stream().filter(a -> a.isRecruited() && !a.isArrested() && a.isMovable()).collect(Collectors.toList());
    }

    public Agent getUnrecruitedAgent(){
        Optional<Agent> opt = agents.stream().filter(a -> !a.isRecruited()).findFirst();
        return opt.isPresent()? opt.get(): null;
    }

    /**
     * Get number of milice to include in the round
     * @return
     */
    public int getNumMilice(){
        int totalMilice = Math.max(getAgentsInPlay().size(), getNumMiliceFromMorale());
        return totalMilice - soldiers;
    }

    public boolean shouldDecreaseMoraleForTurn(){
        return turn == 4 || turn == 7 || turn == 10 || turn == 13;
    }

    public int getNumMiliceFromMorale(){
        if (morale == 7)
            return 2;
        if (morale >= 5)
            return 3;
        if (morale >= 2)
            return 4;
        return 5;
    }

    public List<LocationType> getAvailableSpareRooms(){
        List<LocationType> locationTypes =
                Arrays.asList(
                        LocationType.INFORMANT,
                        LocationType.SAFE_HOUSE_2,
                        LocationType.PROPAGANDIST,
                        LocationType.COUNTERFEITER,
                        LocationType.SMUGGLER,
                        LocationType.CHEMISTS_LAB
                        );
        // Look at all locations on board
        board.getLocations().stream()
                // Extract the location types
                .map(l -> l.getType())
                // Only keep those in the spare room list above
                .filter(lt -> locationTypes.contains(lt))
                // Remove those location types
                .forEach(lt -> locationTypes.remove(lt));
        return locationTypes;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        if (this.turn < 1)
            this.turn = 1;
        if (this.turn > 16)
            this.turn = 16;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
        if (this.morale > 7)
            this.morale = 7;
        if (this.morale < 0)
            this.morale = 0;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
        if (this.soldiers < 0)
            this.soldiers = 0;
        if (this.soldiers > 5)
            this.soldiers = 5;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public boolean hasResources(Resource resource, int count){
        return resources.stream().filter(r -> r == resource).count() >= count;
    }

    public void discardResources(Resource resource, int count){
        for (int i = 0; i < count; ++i){
            resources.remove(resource);
        }
    }

    public PatrolDeck getPatrolDeck() {
        return patrolDeck;
    }

    public Mission getMission1() {
        return mission1;
    }

    public Mission getMission2() {
        return mission2;
    }

    public void visitLocationForMissions(LocationType locationType){
        if (mission1 != null)
            mission1.visitLocation(locationType);
        if (mission2 != null)
            mission2.visitLocation(locationType);
    }

    public boolean allowMiliceElimination() {
        return allowMiliceElimination;
    }

    public void setAllowMiliceElimination(boolean allowMiliceElimination) {
        this.allowMiliceElimination = allowMiliceElimination;
    }
}
