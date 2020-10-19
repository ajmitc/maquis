package maquis.game;

public class Road {
    private Location location1;
    private Location location2;

    // Some missions block of roads (ie. Milice Parade Day)
    private boolean blocked;
    private BlockedReason blockedReason;

    public Road(Location location1, Location location2){
        this.location1     = location1;
        this.location2     = location2;
        this.blocked       = false;
        this.blockedReason = BlockedReason.UNKNOWN;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public boolean hasLocation(Location location){
        return location == location1 || location == location2;
    }

    public boolean hasLocationType(LocationType locationType){
        return locationType == location1.getType() || locationType == location2.getType();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public BlockedReason getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(BlockedReason blockedReason) {
        this.blockedReason = blockedReason;
    }
}
