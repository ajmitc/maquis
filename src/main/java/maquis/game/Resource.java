package maquis.game;

public enum Resource {
    FOOD("food.png"),
    MONEY("money.png"),
    WEAPONS("weapon.png"),
    EXPLOSIVES("explosive.png"),
    MEDICINE("medicine.png"),
    INTEL("intel.png");

    private String filename;
    Resource(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return name();
    }
}
