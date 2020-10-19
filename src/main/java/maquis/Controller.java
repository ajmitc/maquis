package maquis;

import maquis.game.*;
import maquis.game.mission.AssassinationMission;
import maquis.view.BoardPanel;
import maquis.view.PopupUtil;
import maquis.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

public class Controller {
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        view.getMainMenuPanel().getBtnNewGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setGame(new Game());
                view.showGame();
                view.refresh();
                run();
            }
        });

        view.getGamePanel().getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                if (model.getGame().getPhaseStep() == GamePhaseStep.PERFORM_ACTIONS) {
                    if (view.getGamePanel().getBoardPanel().inShootMiliceActionArea(e.getX(), e.getY())) {
                        if (model.getGame().allowMiliceElimination()) {
                            if (model.getGame().getResources().contains(Resource.WEAPONS) &&
                                    model.getGame().getBoard().getLocations().stream().anyMatch(l -> l.hasMilice())) {
                                model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS_SHOOT_MILICE);
                                logger.info("Set phase to Shoot Milice");
                                PopupUtil.popupNotification(view.getFrame(), "Shoot Milice", "Select a location with a Milice to shoot it");
                            } else {
                                PopupUtil.popupNotification(view.getFrame(), "Shoot Milice", "You must have at least one weapon and a milice on the board.");
                            }
                        }
                        else {
                            PopupUtil.popupNotification(view.getFrame(), "Shoot Milice", "You cannot eliminate Milice right now (see Mission Details).");
                        }
                    }
                }

                if (view.getGamePanel().getBoardPanel().inRestartActionArea(e.getX(), e.getY())){
                    if (PopupUtil.popupConfirm(view.getFrame(), "Restart Game", "Are you sure you want to restart the game?")) {
                        logger.info("Restarting game");
                        model.getGame().restart();
                        view.refresh();
                    }
                }
                else if (view.getGamePanel().getBoardPanel().inNewGameActionArea(e.getX(), e.getY())){
                    if (model.getGame().getPhase() == GamePhase.GAME_OVER || PopupUtil.popupConfirm(view.getFrame(), "New Game", "Are you sure you want to start a new game?")) {
                        logger.info("Starting new game");
                        model.setGame(new Game());
                        view.showGame();
                        view.refresh();
                        run();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (model.getGame().getPhaseStep() == GamePhaseStep.PLACE_AGENTS) {
                    // Determine if we selected an Agent
                    for (Agent agent : model.getGame().getAgentsInPlay()) {
                        if (agent.isMovable() && agent.contains(e.getX(), e.getY())) {
                            agent.setSelected(e.getX(), e.getY());
                            view.refresh();
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (model.getGame().getPhaseStep() == GamePhaseStep.PLACE_AGENTS) {
                    // Deselect agent
                    for (Agent agent : model.getGame().getAgentsInPlay()) {
                        if (agent.isSelected()) {
                            agent.setSelected(false);

                            // Attempt to move agent
                            Location newLocation = view.getGamePanel().getBoardPanel().getLocationAt(e.getX(), e.getY());
                            if (newLocation != null && !newLocation.hasMilice() && !newLocation.hasSoldier() && !newLocation.isBlocked()) {
                                Location oldLocation = model.getGame().getBoard().getLocationWithAgent(agent);
                                if (oldLocation == newLocation)
                                    break;
                                agent.setCoord(e.getX() - agent.getSelectedXoffset(), e.getY() - agent.getSelectedYoffset());
                                // remove from old location
                                oldLocation.getAgents().remove(agent);
                                // add to new location
                                newLocation.getAgents().add(agent);
                                agent.setMovable(false);
                                logger.info("Moved Agent from " + oldLocation.getType() + " to " + newLocation.getType());
                                view.refresh();
                                placeMilice();
                                view.refresh();
                            }
                            break;
                        }
                    }
                    run();
                } else if (model.getGame().getPhaseStep() == GamePhaseStep.PERFORM_ACTIONS) {
                    Location location = view.getGamePanel().getBoardPanel().getLocationAt(e.getX(), e.getY());
                    logger.info("Performing Actions on " + location);
                    if (location != null) {
                        if (location.getAgents().isEmpty())
                            return;
                        performAction(location);
                        run();
                    }
                } else if (model.getGame().getPhaseStep() == GamePhaseStep.PERFORM_ACTIONS_SHOOT_MILICE) {
                    Location location = view.getGamePanel().getBoardPanel().getLocationAt(e.getX(), e.getY());
                    if (location.hasMilice()) {
                        model.getGame().getResources().remove(Resource.WEAPONS);
                        location.setMilice(false);
                        model.getGame().setSoldiers(model.getGame().getSoldiers() + 1);
                        model.getGame().setMorale(model.getGame().getMorale() - 1);
                        logger.info("Shot Milice!  -1 Weapons, +1 Soldiers, -1 Morale");
                        PopupUtil.popupNotification(view.getFrame(), "Shoot Milice", "Milice in " + location.getType() + " has been shot!  You throw away the weapon.  Soldier presence incrased, morale decreased!");
                        model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS);
                        run();
                    }
                    else {
                        if (!PopupUtil.popupConfirm(view.getFrame(), "Shoot Milice", "Location does not have a Milice.  Do you still want to shoot a Milice?")) {
                            model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS);
                            run();
                        }
                    }
                }
            }
        });


        view.getGamePanel().getBoardPanel().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (model.getGame().getPhase() == GamePhase.PLACE_AGENTS) {
                    // Move selected agent
                    for (Agent agent : model.getGame().getAgentsInPlay()) {
                        if (agent.isSelected()) {
                            agent.setCoord(e.getX() - agent.getSelectedXoffset(), e.getY() - agent.getSelectedYoffset());
                            logger.info("Set Agent coord to (" + agent.getX() + ", " + agent.getY() + ")");
                            view.refresh();
                            break;
                        }
                    }
                }
            }
        });
    }


    public void run(){
        while (true) {
            switch (model.getGame().getPhase()) {
                case SETUP:
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            model.getGame().getMission1().setup();
                            model.getGame().getMission2().setup();
                            model.getGame().setPhaseStep(GamePhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(GamePhase.PLACE_AGENTS);
                            break;
                        default:
                            break;
                    }
                    break;
                case PLACE_AGENTS: {
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            PopupUtil.popupNotification(view.getFrame(), "Place Agents Phase", "Move Agents");
                            if (model.getGame().allowPeekTopPatrolCard()) {
                                Patrol patrol = model.getGame().getPatrolDeck().peek();
                                PopupUtil.popupNotification(view.getFrame(), "Patrol Card", patrol.getLocation1() + "\nElse, " + patrol.getLocation2() + "\nElse, " + patrol.getLocation3());
                            }
                            model.getGame().setPhaseStep(GamePhaseStep.PLACE_AGENTS);
                            break;
                        case PLACE_AGENTS:
                            for (Agent agent : model.getGame().getAgentsInPlay()) {
                                if (agent.isMovable())
                                    return;
                            }
                            model.getGame().setPhaseStep(GamePhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            // Set all Agents movable unless they are at a Mission Location that says they should not return to safe house
                            model.getGame().getAgentsInPlay().stream().forEach(a -> {
                                Location agentLocation = model.getGame().getBoard().getLocationWithAgent(a);
                                if (agentLocation instanceof MissionLocation && !((MissionLocation) agentLocation).getMission().returnAgentToSafeHouse()) {
                                    a.setMovable(false);
                                } else
                                    a.setMovable(true);
                            });
                            model.getGame().setPhase(GamePhase.PERFORM_ACTIONS);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                case PERFORM_ACTIONS:
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            PopupUtil.popupNotification(view.getFrame(), "Peform Actions Phase", "Click Agents to perform actions");
                            model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS);
                            break;
                        case PERFORM_ACTIONS:
                            if (model.getGame().getBoard().getLocations().stream()
                                    .filter(l -> !l.getType().isSafeHouse() && !l.getAgents().isEmpty() && l.getAgents().stream().anyMatch(a -> a.isMovable()))
                                    .count() > 0) {
                                return;
                            }
                            model.getGame().setPhaseStep(GamePhaseStep.END_PHASE);
                            break;
                        case PERFORM_ACTIONS_SHOOT_MILICE:
                            return;
                        case END_PHASE:
                            // Check for agents at mission locations that should not return to a safe house, but we still need to visit the location
                            MissionLocation missionLocation1 = (MissionLocation) model.getGame().getBoard().getLocationWithMission(model.getGame().getMission1());
                            MissionLocation missionLocation2 = (MissionLocation) model.getGame().getBoard().getLocationWithMission(model.getGame().getMission2());
                            if (!missionLocation1.getAgents().isEmpty() && !model.getGame().getMission1().returnAgentToSafeHouse()) {
                                model.getGame().getMission1().visitLocation(missionLocation1.getType());
                            }
                            if (!missionLocation2.getAgents().isEmpty() && !model.getGame().getMission2().returnAgentToSafeHouse()) {
                                model.getGame().getMission2().visitLocation(missionLocation2.getType());
                            }
                            // If Assassination Mission, ask to shoot milice before end of phase
                            if (model.getGame().getMission1() instanceof AssassinationMission || model.getGame().getMission2() instanceof AssassinationMission) {
                                if (model.getGame().allowMiliceElimination() && model.getGame().getSoldiers() < 5 && model.getGame().getResources().contains(Resource.WEAPONS)) {
                                    if (PopupUtil.popupConfirm(view.getFrame(), "Assassination Mission", "Do you want to shoot a Milice?")) {
                                        model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS_SHOOT_MILICE);
                                        return;
                                    }
                                }
                            }
                            // Check end game
                            if (!checkGameOver()) {
                                model.getGame().setPhase(GamePhase.END_TURN);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case END_TURN:
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            // Let the mission update things
                            model.getGame().getMission1().turnTeardown();
                            model.getGame().getMission2().turnTeardown();

                            // Increment turn and decrease morale if necessary
                            model.getGame().setTurn(model.getGame().getTurn() + 1);
                            if (model.getGame().shouldDecreaseMoraleForTurn()) {
                                model.getGame().setMorale(model.getGame().getMorale() - 1);
                            }

                            // Remove milice and soldiers
                            model.getGame().getBoard().getLocations().stream().forEach(l -> {
                                l.setMilice(false);
                                l.setSoldier(false);
                            });

                            // Allow all Agents in play at a Safe House to move again
                            model.getGame().getAgentsInPlay().stream()
                                    .filter(a -> model.getGame().getBoard().getLocationWithAgent(a).getType().isSafeHouse())
                                    .forEach(a -> a.setMovable(true));

                            // Let the mission update things
                            model.getGame().getMission1().turnSetup();
                            model.getGame().getMission2().turnSetup();
                            view.refresh();
                            model.getGame().setPhaseStep(GamePhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            if (!checkGameOver()) {
                                model.getGame().setPhase(GamePhase.PLACE_AGENTS);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case GAME_OVER:
                default:
                    return;
            }
        }
    }

    public void placeMilice(){
        logger.info("Placing Milice");
        int numTotalMilice    = model.getGame().getNumMilice();
        int numTotalSoldiers  = model.getGame().getSoldiers();
        int numMilicePlaced   = (int) model.getGame().getBoard().getLocations().stream().filter(l -> l.hasMilice()).count();
        int numSoldiersPlaced = (int) model.getGame().getBoard().getLocations().stream().filter(l -> l.hasSoldier()).count();
        int numAgentsToPlace  = model.getGame().getMovableAgents().size();

        int numMiliceLeft   = numTotalMilice - numMilicePlaced;
        int numSoldiersLeft = numTotalSoldiers - numSoldiersPlaced;

        logger.info("Total Milice to place: " + numTotalMilice + " (" + numMilicePlaced + " already placed; " + numMiliceLeft + " left)");
        logger.info("Total Soldiers to place: " + numTotalSoldiers + " (" + numSoldiersPlaced + " already placed; " + numSoldiersLeft + " left)");
        logger.info("Agents left to place: " + numAgentsToPlace);

        while (numMiliceLeft + numSoldiersLeft > 0){
            // Draw Patrol card and place milice or soldier
            Patrol patrol = model.getGame().getPatrolDeck().draw();
            logger.info("Patrol Card: " + patrol.getLocation1() + ", " + patrol.getLocation2() + ", " + patrol.getLocation3());

            boolean placed = false;
            boolean arrest = false;
            for (int j = 0; j < 2; ++j) { // Loop through twice, first - don't arrest, second - arrest!
                // Try location 1
                LocationType locationType1 = patrol.getLocation1();
                if (numMiliceLeft > 0) {
                    placed = placeMiliceOrSoldier(locationType1, true, arrest);
                    if (placed) numMiliceLeft -= 1;
                } else if (numSoldiersLeft > 0) {
                    placed = placeMiliceOrSoldier(locationType1, false, arrest);
                    if (placed) numSoldiersLeft -= 1;
                }

                // If not placed, try location 2
                if (!placed) {
                    LocationType locationType2 = patrol.getLocation2();
                    if (numMiliceLeft > 0) {
                        placed = placeMiliceOrSoldier(locationType2, true, arrest);
                        if (placed) numMiliceLeft -= 1;
                    } else if (numSoldiersLeft > 0) {
                        placed = placeMiliceOrSoldier(locationType2, false, arrest);
                        if (placed) numSoldiersLeft -= 1;
                    }
                }

                // If not placed, try location 3
                if (!placed) {
                    LocationType locationType3 = patrol.getLocation3();
                    if (numMiliceLeft > 0) {
                        placed = placeMiliceOrSoldier(locationType3, true, arrest);
                        if (placed) numMiliceLeft -= 1;
                    } else if (numSoldiersLeft > 0) {
                        placed = placeMiliceOrSoldier(locationType3, false, arrest);
                        if (placed) numSoldiersLeft -= 1;
                    }
                }

                // OK, not placed in any location, start arresting!
                if (!placed)
                    arrest = true;
                else
                    break;
                logger.info("No Milice/Soldiers placed, start arresting!");
            }
            view.refresh();
            if (placed && numAgentsToPlace > 0)
                break;
        }

        run();
    }

    private boolean placeMiliceOrSoldier(LocationType locationType, boolean placeMilice, boolean arrestIfAgentFound){
        logger.info("Attempting to place " + (placeMilice? "Milice": "Soldier") + " at " + locationType + " (arrest: " + arrestIfAgentFound + ")");
        Location location = model.getGame().getBoard().getLocation(locationType);
        if (arrestIfAgentFound && !location.getAgents().isEmpty()){
            if (placeMilice)
                location.setMilice(true);
            else
                location.setSoldier(true);
            location.getAgents().stream().forEach(a -> a.setArrested(true));
            location.getAgents().clear();
            logger.info("Arrested Agent at " + locationType);
            PopupUtil.popupNotification(view.getFrame(), "Arrested!", "Arrested agent at " + locationType);
            return true;
        }
        if (location.getAgents().isEmpty() && !location.hasMilice() && !location.hasSoldier()){
            if (placeMilice)
                location.setMilice(true);
            else
                location.setSoldier(true);
            logger.info("Placed " + (placeMilice? "Milice": "Soldier") + " at " + locationType);
            return true;
        }
        return false;
    }

    public void performAction(Location location){
        if (location.getType().isSafeHouse() || location.getAgents().isEmpty())
            return;
        Agent agent = location.getAgents().get(0);

        boolean returnToSafeHouse = (location instanceof MissionLocation)? ((MissionLocation) location).getMission().returnAgentToSafeHouse(): true;
        if (returnToSafeHouse){
            // Otherwise, only perform the action if the agent can get to a safe house
            LocationType safeHouseType = getSafeRouteToSafeHouse(location);
            if (safeHouseType != null) {
                // Agent can get back to SAFE HOUSE
                performLocationAction(agent, location);
                // Move Agent to safehouse
                location.getAgents().remove(agent);
                model.getGame().getBoard().getLocation(safeHouseType).getAgents().add(agent);
                int numAgentsAtSafeHouse =
                        (int) model.getGame().getBoard().getLocation(safeHouseType).getAgents().stream()
                                .filter(a -> a.isRecruited() && !a.isArrested())
                                .count();
                Point newLoc = BoardPanel.LOCATION_FIGURE_COORDS.get(safeHouseType).get(numAgentsAtSafeHouse - 1);
                agent.setCoord(newLoc.x, newLoc.y);
                logger.info("Agent made it back to Safe House!");
                view.refresh();
            }
            else {
                // No safe route to safe house, check if player wants to shoot milice
                List<Road> shortestRoute = null;
                for (LocationType safeHouseType1: new LocationType[]{LocationType.SAFE_HOUSE_1, LocationType.SAFE_HOUSE_2}) {
                    if (model.getGame().getBoard().getLocation(safeHouseType1) == null)
                        continue;
                    List<Road> roads = model.getGame().getBoard().getRouteBetween(location.getType(), safeHouseType1);
                    if (roads != null && (shortestRoute == null || roads.size() < shortestRoute.size())) {
                        shortestRoute = roads;
                    }
                }

                // Check if there's any milice in route.  It could be that only soldiers block the path.
                boolean hasMiliceInRoute = shortestRoute.stream().anyMatch(r -> r.getLocation1().hasMilice() || r.getLocation2().hasMilice());

                // If player has weapon and Milice blocking path, ask if they want to shoot milice, then return
                if (hasMiliceInRoute && model.getGame().getResources().contains(Resource.WEAPONS) && model.getGame().allowMiliceElimination()){
                    if (PopupUtil.popupConfirm(view.getFrame(), "Shoot Milice?", "Do you want to shoot a Milice to clear the path to the Safe House?")){
                        model.getGame().setPhaseStep(GamePhaseStep.PERFORM_ACTIONS_SHOOT_MILICE);
                        // exit this method, which allows the player to click the agent again to perform the action
                        return;
                    }
                }

                // Well, we can't get back to the safe house and we didn't shoot a Milice
                // If the location is a guaranteed action, then do it before we are arrested
                if (location.isGuaranteedAction())
                    performLocationAction(agent, location);

                // Arrest the player
                agent.setArrested(true);
                agent.setMovable(false);
                location.getAgents().remove(agent);
                logger.info("Agent at " + (location.getType().isSpareRoom()? location.getSpareRoomType(): location.getType()) + " has been arrested!  No route to Safe House!");
                PopupUtil.popupNotification(view.getFrame(), "Arrested!", "Agent at " + (location.getType().isSpareRoom()? location.getSpareRoomType(): location.getType()) + " has been arrested!");
            }
        }
        // The mission says we can't go back to the safe house, so execute the location action if it's guaranteed
        else if (location.isGuaranteedAction()) {
            performLocationAction(agent, location);
        }
    }

    private LocationType getSafeRouteToSafeHouse(Location location){
        for (LocationType safeHouseType : new LocationType[]{LocationType.SAFE_HOUSE_1, LocationType.SAFE_HOUSE_2}) {
            if (model.getGame().getBoard().getLocation(safeHouseType) == null)
                continue;
            List<Road> roads = model.getGame().getBoard().getSafeRouteBetween(location.getType(), safeHouseType);
            if (roads != null)
                return safeHouseType;
        }
        return null;
    }

    private void performLocationAction(Agent agent, Location location){
        model.getGame().visitLocationForMissions(location.getSpareRoomType() != null? location.getSpareRoomType(): location.getType());
        switch(location.getType()){
            case CAFE:
                visitCafe();
                break;
            case FENCE:
                visitFence();
                break;
            case DOCTOR:
                visitDoctor();
                break;
            case GROCER:
                visitGrocer();
                break;
            case FIELD_1:
            case FIELD_2:
                visitField(location.getType());
                break;
            case RADIO_A:
            case RADIO_B:
                visitRadio(location.getType());
                break;
            case MISSION_1:
            case MISSION_2:
                visitMission(location.getType());
                break;
            case RUE_BARADAT:
                visitRueBaradat();
                break;
            case BLACK_MARKET:
                visitBlackMarket();
                break;
            case PONT_DU_NORD:
                visitPontDuNord();
                break;
            case PONT_LEVEQUE:
                visitPontLeveque();
                break;
            case SPARE_ROOM_1:
            case SPARE_ROOM_2:
            case SPARE_ROOM_3:
                if (location.getSpareRoomType() == null)
                    visitSpareRoom(location.getType());
                else if (location.getSpareRoomType() == LocationType.CHEMISTS_LAB)
                    visitChemistLab();
                else if (location.getSpareRoomType() == LocationType.COUNTERFEITER)
                    visitCounterfeiter();
                else if (location.getSpareRoomType() == LocationType.INFORMANT)
                    visitInformant();
                else if (location.getSpareRoomType() == LocationType.PROPAGANDIST)
                    visitPropagandist();
                else if (location.getSpareRoomType() == LocationType.SMUGGLER)
                    visitSmuggler();
                break;
            case POOR_DISTRICT:
                visitPoorDistrict();
                break;
            default:
                break;
        }
        view.refresh();
    }

    private void visitCafe(){
        // Discard a food
        if (!model.getGame().getResources().contains(Resource.FOOD)){
            logger.info("Unable to recruit Agent, no food available!");
            PopupUtil.popupNotification(view.getFrame(), "Unable to perform action", "Unable to recruit Agent, no food available.");
            return;
        }
        model.getGame().getResources().remove(Resource.FOOD);
        // Find available Agent
        Agent recruit = model.getGame().getUnrecruitedAgent();
        if (recruit != null){
            recruit.setRecruited(true);
            Point p = BoardPanel.LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).get(BoardPanel.LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).size() - 1);
            recruit.setCoord(p.x, p.y);
            model.getGame().getBoard().getLocation(LocationType.SAFE_HOUSE_1).getAgents().add(recruit);
            logger.info("Recruited Agent at Cafe!");
            PopupUtil.popupNotification(view.getFrame(), "Action", "Agent Recruited!");
        }
        else {
            logger.info("Unable to recruit Agent, no Agents available!");
            PopupUtil.popupNotification(view.getFrame(), "Unable to perform action", "Unable to recruit Agent, no Agents available.");
        }
    }

    private void visitFence(){
        // Discard a money
        if (!model.getGame().getResources().contains(Resource.MONEY)){
            logger.info("Unable to purchase weapons, no money available!");
            PopupUtil.popupNotification(view.getFrame(), "Unable to perform action", "Unable to purchase weapons, no money available.");
            return;
        }
        if (PopupUtil.popupConfirm(view.getFrame(), "Action", "Purchase 1 weapon for 1 money?")) {
            model.getGame().getResources().remove(Resource.MONEY);
            model.getGame().getResources().add(Resource.WEAPONS);
            logger.info("Purchased Weapons with Money");
            PopupUtil.popupNotification(view.getFrame(), "Action", "Purchased 1 weapon for 1 money.");
        }
    }

    private void visitDoctor(){
        model.getGame().getResources().add(Resource.MEDICINE);
        logger.info("Acquired medicine from the Doctor");
        PopupUtil.popupNotification(view.getFrame(), "Action", "Acquired medicine from the Doctor.");
    }

    private void visitGrocer(){
        model.getGame().getResources().add(Resource.FOOD);
        logger.info("Acquired food from the Grocer");
        PopupUtil.popupNotification(view.getFrame(), "Action", "Acquired food from the Grocer.");
    }

    /**
     * TODO Cannot pick up resources same turn they were dropped!
     * @param locationType
     */
    private void visitField(LocationType locationType){
        FieldLocation fieldLocation = (FieldLocation) model.getGame().getBoard().getLocation(locationType);
        if (fieldLocation.getResources().isEmpty()){
            logger.info("No resources at " + locationType + ", nothing to do.");
            return;
        }
        if (fieldLocation.getTurnDropped() == model.getGame().getTurn()){
            PopupUtil.popupNotification(view.getFrame(), "Action", "Cannot pick up resources dropped the same turn!");
            return;
        }
        model.getGame().getResources().addAll(fieldLocation.getResources());
        logger.info("Acquired " + fieldLocation.getResources().size() + " " + (fieldLocation.getResources().size() == 1? "resource": "resources") + " from " + locationType);
        PopupUtil.popupNotification(view.getFrame(), "Action",
                "Acquired " + fieldLocation.getResources().size() + " " + (fieldLocation.getResources().size() == 1? "resource": "resources") + " from " + locationType);
        fieldLocation.getResources().clear();
        fieldLocation.setTurnDropped(0);
    }

    private void visitRadio(LocationType locationType){
        // Check if a Field is empty
        FieldLocation field1 = (FieldLocation) model.getGame().getBoard().getLocation(LocationType.FIELD_1);
        FieldLocation field2 = (FieldLocation) model.getGame().getBoard().getLocation(LocationType.FIELD_2);
        if (!field1.getResources().isEmpty() && !field2.getResources().isEmpty()){
            logger.warning("Both fields have resources, cannot drop more!");
            PopupUtil.popupNotification(view.getFrame(), "Action", "Both fields already have resources, cannot drop more!");
            return;
        }

        // TODO Check the rules on these amounts
        int foodDrop = 3;
        int moneyDrop = 1;
        if (model.getDifficulty() == Difficulty.EASY){
            foodDrop = 3;
            moneyDrop = 3;
        }
        else if (model.getDifficulty() == Difficulty.HARD){
            foodDrop = 1;
            moneyDrop = 1;
        }

        int choice = PopupUtil.popupQuestion(
                view.getFrame(),
                locationType.getName(),
                "Request which resources to drop in a Field?",
                new String[]{
                        "1 " + Resource.INTEL.name(),
                        foodDrop + " " + Resource.FOOD.name(),
                        moneyDrop + " " + Resource.MONEY.name(),
                        "1 " + Resource.WEAPONS.name()
                }
        );

        if (choice == JOptionPane.CLOSED_OPTION)
            return;
        FieldLocation field = field1.getResources().isEmpty()? field1: field2;
        switch(choice){
            case 0: {
                field.getResources().add(Resource.INTEL);
                logger.info("Dropped 1 Intel in " + field.getType());
                break;
            }
            case 1: {
                for (int i = 0; i < foodDrop; ++i)
                    field.getResources().add(Resource.FOOD);
                logger.info("Dropped " + foodDrop + " Food in " + field.getType());
                break;
            }
            case 2: {
                for (int i = 0; i < moneyDrop; ++i)
                    field.getResources().add(Resource.MONEY);
                logger.info("Dropped " + moneyDrop + " Money in " + field.getType());
                break;
            }
            case 3: {
                field.getResources().add(Resource.WEAPONS);
                logger.info("Dropped 1 Weapon in " + field.getType());
                break;
            }
        }
        field.setTurnDropped(model.getGame().getTurn());
    }

    private void visitMission(LocationType locationType){

    }

    private void visitRueBaradat(){
    }

    private void visitBlackMarket(){
        // Discard medicine/food for money and -1 morale
        int choice = PopupUtil.popupQuestion(view.getFrame(), "Action", "Sell which resource?", new String[]{"Nothing", "Medicine", "Food"});
        if (choice == JOptionPane.CLOSED_OPTION || choice == 0)
            return;
        if (choice == 1) {
            if (model.getGame().getResources().contains(Resource.MEDICINE))
                model.getGame().getResources().remove(Resource.MEDICINE);
            else{
                logger.warning("Player does not have any medicine, aborting action");
                PopupUtil.popupNotification(view.getFrame(), "Action", "You don't have any medicine.");
                return;
            }
        }
        if (choice == 2) {
            if (model.getGame().getResources().contains(Resource.FOOD))
                model.getGame().getResources().remove(Resource.FOOD);
            else{
                logger.warning("Player does not have any food, aborting action");
                PopupUtil.popupNotification(view.getFrame(), "Action", "You don't have any food.");
                return;
            }
        }
        model.getGame().getResources().add(Resource.MONEY);
        model.getGame().setMorale(model.getGame().getMorale() - 1);
        logger.info("Sold food/medicine on Black Market, gained Money, but lost Morale");
        PopupUtil.popupNotification(view.getFrame(), "Action", "Sold food/medicine on Black Market. +1 Money. -1 Morale.");
    }

    private void visitPontDuNord(){
    }

    private void visitPontLeveque(){
    }

    private void visitSpareRoom(LocationType locationType){
        // Check if player has two money
        int numMoney = (int) model.getGame().getResources().stream().filter(r -> r == Resource.MONEY).count();
        if (numMoney < 2){
            logger.warning("Player doesn't have enough money to purchase Spare Room!");
            PopupUtil.popupNotification(view.getFrame(), "Action", "You don't have enough money to purchase Spare Room");
            return;
        }

        // Ask if player wants to purchase a room for 2 money
        List<LocationType> availableSpareRooms = model.getGame().getAvailableSpareRooms();
        String[] names = new String[availableSpareRooms.size()];
        for (int i = 0; i < availableSpareRooms.size(); ++i) {
            names[i] = availableSpareRooms.get(i).getName();
        }
        int choice = PopupUtil.popupQuestion(view.getFrame(), "Build Spare Room", "Which Spare Room do you want to build?", names);
        if (choice == JOptionPane.CLOSED_OPTION)
            return;

        model.getGame().getResources().remove(Resource.MONEY);
        model.getGame().getResources().remove(Resource.MONEY);
        LocationType spareRoomType = availableSpareRooms.get(choice);
        Location location = model.getGame().getBoard().getLocation(locationType);
        location.setSpareRoomType(spareRoomType);
        logger.info("Built " + spareRoomType + " at " + location.getType());
        view.refresh();
    }

    private void visitPoorDistrict(){
        if (model.getGame().hasResources(Resource.FOOD, 1) && model.getGame().hasResources(Resource.MEDICINE, 1)) {
            if (PopupUtil.popupConfirm(view.getFrame(), "Action", "Deliver food and medicine to Poor District?")) {
                model.getGame().getResources().remove(Resource.FOOD);
                model.getGame().getResources().remove(Resource.MEDICINE);
                model.getGame().setMorale(model.getGame().getMorale() + 1);
                logger.info("Delivered food and medicine to Poor District, gained morale");
                PopupUtil.popupNotification(view.getFrame(), "Action", "Delivered food and medicine to Poor District. +1 Morale.");
            }
        }
    }

    private void visitChemistLab(){
        if (model.getGame().hasResources(Resource.MEDICINE, 1)) {
            model.getGame().getResources().remove(Resource.MEDICINE);
            model.getGame().getResources().add(Resource.EXPLOSIVES);
            PopupUtil.popupNotification(view.getFrame(), "Action", "Converted 1 medicine to 1 explosive.");
        }
        else
            PopupUtil.popupNotification(view.getFrame(), "Action", "Failed.  You must have 1 medicine to convert to 1 explosive.");
    }

    private void visitCounterfeiter(){
        model.getGame().getResources().add(Resource.MONEY);
        PopupUtil.popupNotification(view.getFrame(), "Action", "You gained 1 money.");
    }

    private void visitInformant(){
        model.getGame().getResources().add(Resource.INTEL);
        PopupUtil.popupNotification(view.getFrame(), "Action", "You gained 1 intel.");
    }

    private void visitPropagandist(){
        model.getGame().setMorale(model.getGame().getMorale() + 1);
        PopupUtil.popupNotification(view.getFrame(), "Action", "You gained +1 morale.");
    }

    private void visitSmuggler(){
        // Gain 3 food or 3 medicine
        int choice = PopupUtil.popupQuestion(view.getFrame(), "Action", "What do you want to gain?", new String[]{"3 Food", "3 Medicine"});
        if (choice == JOptionPane.CLOSED_OPTION)
            return;
        if (choice == 0){
            model.getGame().getResources().add(Resource.FOOD);
            model.getGame().getResources().add(Resource.FOOD);
            model.getGame().getResources().add(Resource.FOOD);
            PopupUtil.popupNotification(view.getFrame(), "Action", "You gained 3 food.");
        }
        else if (choice == 1){
            model.getGame().getResources().add(Resource.MEDICINE);
            model.getGame().getResources().add(Resource.MEDICINE);
            model.getGame().getResources().add(Resource.MEDICINE);
            PopupUtil.popupNotification(view.getFrame(), "Action", "You gained 3 medicine.");
        }
    }

    private boolean checkGameOver(){
        if (model.getGame().getMission1().isCompleted() && model.getGame().getMission2().isCompleted()){
            // Game Over, player wins!
            model.getGame().setPhase(GamePhase.GAME_OVER);
            PopupUtil.popupNotification(view.getFrame(), "Game Over", "You Win!\nThe German occupiers have decided you're not worth the trouble and moved on.\nYour town is liberated!");
            return true;
        }
        else if (model.getGame().getAgentsInPlay().size() == 0){
            // Game Over, player loses!
            model.getGame().setPhase(GamePhase.GAME_OVER);
            PopupUtil.popupNotification(view.getFrame(), "Game Over", "All Agents arrested.  Game Over!");
            return true;
        }
        else if (model.getGame().getTurn() == Game.GAME_OVER_TURN){
            model.getGame().setPhase(GamePhase.GAME_OVER);
            PopupUtil.popupNotification(view.getFrame(), "Game Over", "You failed to complete both missions.  Game Over!");
            return true;
        }
        else if (model.getGame().getMorale() == 0){
            model.getGame().setPhase(GamePhase.GAME_OVER);
            PopupUtil.popupNotification(view.getFrame(), "Game Over", "Morale has reached 0.  Game Over!");
            return true;
        }
        return false;
    }
}
