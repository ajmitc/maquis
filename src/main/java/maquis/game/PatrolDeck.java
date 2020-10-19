package maquis.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatrolDeck {
    private List<Patrol> patrols = new ArrayList<>();
    private List<Patrol> discard = new ArrayList<>();

    public PatrolDeck(){
        patrols.add(new Patrol(LocationType.GROCER, LocationType.PONT_DU_NORD, LocationType.FENCE));
        patrols.add(new Patrol(LocationType.FENCE, LocationType.RUE_BARADAT, LocationType.POOR_DISTRICT));
        patrols.add(new Patrol(LocationType.FENCE, LocationType.PONT_DU_NORD, LocationType.PONT_LEVEQUE));
        patrols.add(new Patrol(LocationType.RADIO_A, LocationType.PONT_LEVEQUE, LocationType.BLACK_MARKET));
        patrols.add(new Patrol(LocationType.RADIO_A, LocationType.RUE_BARADAT, LocationType.GROCER));
        patrols.add(new Patrol(LocationType.RADIO_B, LocationType.RUE_BARADAT, LocationType.PONT_LEVEQUE));
        patrols.add(new Patrol(LocationType.GROCER, LocationType.POOR_DISTRICT, LocationType.DOCTOR));
        patrols.add(new Patrol(LocationType.PONT_LEVEQUE, LocationType.PONT_DU_NORD, LocationType.DOCTOR));
        patrols.add(new Patrol(LocationType.RADIO_B, LocationType.GROCER, LocationType.BLACK_MARKET));
        patrols.add(new Patrol(LocationType.PONT_LEVEQUE, LocationType.BLACK_MARKET, LocationType.DOCTOR));

        reshuffle();
    }

    public Patrol draw(){
        if (patrols.isEmpty())
            reshuffle();
        Patrol patrol = patrols.remove(0);
        discard.add(patrol);
        return patrol;
    }

    public Patrol peek(){
        if (patrols.isEmpty())
            reshuffle();
        return patrols.get(0);
    }

    public void reshuffle(){
        patrols.addAll(discard);
        discard.clear();
        Collections.shuffle(patrols);
    }
}
