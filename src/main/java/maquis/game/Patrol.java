package maquis.game;

public class Patrol {
    private LocationType location1;
    private LocationType location2;
    private LocationType location3;

    public Patrol(LocationType l1, LocationType l2, LocationType l3){
        this.location1 = l1;
        this.location2 = l2;
        this.location3 = l3;
    }

    public LocationType getLocation1() {
        return location1;
    }

    public LocationType getLocation2() {
        return location2;
    }

    public LocationType getLocation3() {
        return location3;
    }
}
