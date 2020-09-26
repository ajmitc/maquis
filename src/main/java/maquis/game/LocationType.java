package maquis.game;

public enum LocationType {
    SAFE_HOUSE_1("Safe House 1"),
    SAFE_HOUSE_2("Safe House 2"),
    GROCER("Grocer"),
    PONT_LEVEQUE("Pont Leveque"),
    CAFE("Cafe"),
    FIELD_1("Field 1"),
    FIELD_2("Field 2"),
    SPARE_ROOM_1("Spare Room 1"),
    SPARE_ROOM_2("Spare Room 2"),
    SPARE_ROOM_3("Spare Room 3"),
    DOCTOR("Doctor"),
    RADIO_A("Radio A"),
    RADIO_B("Radio B"),
    POOR_DISTRICT("Poor District"),
    BLACK_MARKET("Black Market"),
    PONT_DU_NORD("Pont du Nord"),
    FENCE("Fence"),
    RUE_BARADAT("Rue Baradat"),

    // Spare Rooms
    CHEMISTS_LAB("Chemist's Lab"), // Convert 1 Medicine into 1 Explosive
    PROPAGANDIST("Propagandist"), // Gain 1 Morale
    INFORMANT("Informant"), // Gain 1 Intel
    COUNTERFEITER("Counterfeiter"), // Gain 1 Money
    SMUGGLER("Smuggler"), // Gain 3 medicine or 3 food

    // Mission Locations
    MISSION_1("Mission 1"),
    MISSION_2("Mission 2");

    private String name;
    LocationType(String name){
        this.name = name;
    }

    public static LocationType fromName(String name){
        for (LocationType locationType: values()){
            if (locationType.getName().equals(name))
                return locationType;
        }
        return null;
    }

    public boolean isSafeHouse(){
        return this == SAFE_HOUSE_1 || this == SAFE_HOUSE_2;
    }

    public boolean isSpareRoom(){
        return this == SPARE_ROOM_1 || this == SPARE_ROOM_2 || this == SPARE_ROOM_3;
    }

    public String getName(){
        return name;
    }
}
