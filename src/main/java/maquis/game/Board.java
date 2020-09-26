package maquis.game;

import maquis.Model;
import maquis.game.mission.Mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {
    private List<Location> locations = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();

    public Board(){
        // Left of Cafe
        Location field1 = new FieldLocation(LocationType.FIELD_1);

        // Right of Safe House
        Location field2 = new FieldLocation(LocationType.FIELD_2);

        // Next to Doctor
        Location spareRoom1 = new Location(LocationType.SPARE_ROOM_1);

        // Next to Radio B
        Location spareRoom2 = new Location(LocationType.SPARE_ROOM_2);

        // Next to Grocer
        Location spareRoom3 = new Location(LocationType.SPARE_ROOM_3);

        // Next to Fence
        Location mission1 = new MissionLocation(LocationType.MISSION_1);

        // Next to Pont du Nord
        Location mission2 = new MissionLocation(LocationType.MISSION_2);

        locations.add(field1);
        locations.add(field2);
        locations.add(spareRoom1);
        locations.add(spareRoom2);
        locations.add(spareRoom3);
        locations.add(mission1);
        locations.add(mission2);
        locations.add(new Location(LocationType.CAFE));
        locations.add(new Location(LocationType.SAFE_HOUSE_1));
        locations.add(new Location(LocationType.PONT_LEVEQUE));
        locations.add(new Location(LocationType.GROCER));
        locations.add(new Location(LocationType.DOCTOR));
        locations.add(new Location(LocationType.RADIO_A));
        locations.add(new Location(LocationType.RUE_BARADAT));
        locations.add(new Location(LocationType.FENCE));
        locations.add(new Location(LocationType.POOR_DISTRICT));
        locations.add(new Location(LocationType.PONT_DU_NORD));
        locations.add(new Location(LocationType.BLACK_MARKET));
        locations.add(new Location(LocationType.RADIO_B));

        spareRoom1.setGuaranteedAction(true);
        spareRoom2.setGuaranteedAction(true);
        spareRoom3.setGuaranteedAction(true);
        getLocation(LocationType.POOR_DISTRICT).setGuaranteedAction(true);

        if (Model.getProperty("game.rules.radio.guaranteed_action", false)) {
            // These are not traditionally guaranteed actions, but they should be :)
            getLocation(LocationType.RADIO_A).setGuaranteedAction(true);
            getLocation(LocationType.RADIO_B).setGuaranteedAction(true);
        }

        roads.add(new Road(field1, getLocation(LocationType.CAFE)));

        roads.add(new Road(getLocation(LocationType.SAFE_HOUSE_1), getLocation(LocationType.CAFE)));
        roads.add(new Road(getLocation(LocationType.SAFE_HOUSE_1), field2));
        roads.add(new Road(getLocation(LocationType.SAFE_HOUSE_1), getLocation(LocationType.PONT_LEVEQUE)));
        roads.add(new Road(getLocation(LocationType.SAFE_HOUSE_1), getLocation(LocationType.GROCER)));

        roads.add(new Road(getLocation(LocationType.GROCER), spareRoom3));
        roads.add(new Road(getLocation(LocationType.GROCER), getLocation(LocationType.BLACK_MARKET)));

        roads.add(new Road(getLocation(LocationType.BLACK_MARKET), spareRoom2));
        roads.add(new Road(getLocation(LocationType.BLACK_MARKET), getLocation(LocationType.RADIO_B)));
        roads.add(new Road(getLocation(LocationType.BLACK_MARKET), getLocation(LocationType.PONT_DU_NORD)));
        roads.add(new Road(getLocation(LocationType.BLACK_MARKET), getLocation(LocationType.POOR_DISTRICT)));

        roads.add(new Road(spareRoom2, getLocation(LocationType.RADIO_B)));

        roads.add(new Road(getLocation(LocationType.POOR_DISTRICT), getLocation(LocationType.PONT_DU_NORD)));
        roads.add(new Road(getLocation(LocationType.POOR_DISTRICT), getLocation(LocationType.FENCE)));
        roads.add(new Road(getLocation(LocationType.POOR_DISTRICT), getLocation(LocationType.PONT_LEVEQUE)));

        roads.add(new Road(getLocation(LocationType.PONT_LEVEQUE), getLocation(LocationType.DOCTOR)));

        roads.add(new Road(getLocation(LocationType.DOCTOR), spareRoom1));
        roads.add(new Road(getLocation(LocationType.DOCTOR), getLocation(LocationType.RADIO_A)));
        roads.add(new Road(getLocation(LocationType.DOCTOR), getLocation(LocationType.RUE_BARADAT)));

        roads.add(new Road(spareRoom1, getLocation(LocationType.RADIO_A)));

        roads.add(new Road(getLocation(LocationType.RUE_BARADAT), getLocation(LocationType.FENCE)));

        roads.add(new Road(mission1, getLocation(LocationType.FENCE)));
        roads.add(new Road(mission2, getLocation(LocationType.PONT_DU_NORD)));
    }

    public void restart(){
        locations.stream().forEach(l -> {
            l.setSpareRoomType(null);
            l.setBlocked(false);
            l.setSoldier(false);
            l.setMilice(false);
            l.getAgents().clear();
        });
        roads.stream().forEach(r -> {
            r.setBlocked(false);
        });
    }

    public Location getLocation(LocationType type){
        for (Location location: locations){
            if (location.getType() == type)
                return location;
        }
        return null;
    }

    public List<Location> getLocationsWithTypes(LocationType ... types){
        List<LocationType> typeList = Arrays.asList(types);
        return locations.stream().filter(l -> typeList.contains(l.getType())).collect(Collectors.toList());
    }

    public Location getLocationWithAgent(Agent agent){
        Optional<Location> opt = locations.stream().filter(l -> l.getAgents().contains(agent)).findFirst();
        return opt.isPresent()? opt.get(): null;
    }

    public Location getLocationWithMission(Mission mission){
        Optional<Location> opt = locations.stream().filter(l -> l instanceof MissionLocation).filter(l -> ((MissionLocation) l).getMission() == mission).findFirst();
        return opt.isPresent()? opt.get(): null;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Road> getSafeRouteBetween(LocationType location1, LocationType location2){
        Location currentLocation = getLocation(location1);
        Location endLocation = getLocation(location2);
        return AStarAlgorithm.findShortestUnblockedPath(currentLocation, endLocation, this);
    }

    public List<Road> getRouteBetween(LocationType location1, LocationType location2){
        Location currentLocation = getLocation(location1);
        Location endLocation = getLocation(location2);
        return AStarAlgorithm.findShortestPath(currentLocation, endLocation, this);
    }

    public Road getRoad(LocationType locationType1, LocationType locationType2){
        Optional<Road> road = roads.stream()
                .filter(r -> r.hasLocationType(locationType1) && r.hasLocationType(locationType2))
                .findFirst();
        return road.isPresent()? road.get(): null;
    }

    public List<Road> getRoads() {
        return roads;
    }
}
