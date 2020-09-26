package maquis.game.mission;

import maquis.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MissionDeck {
    private List<Mission> missions  = new ArrayList<>();
    private List<Mission> missions1;
    private List<Mission> missions2;
    private List<Mission> discard1  = new ArrayList<>();
    private List<Mission> discard2  = new ArrayList<>();

    public MissionDeck(Game game) {
        missions.add(new AidTheSpyMission(game));
        missions.add(new AssassinationMission(game));
        missions.add(new BombForTheOfficerMission(game));
        missions.add(new CodedMessagesMission(game));
        missions.add(new DestroyTheTrainMission(game));
        missions.add(new DoubleAgentMission(game));
        missions.add(new GermanShepherdsMission(game));
        missions.add(new InfiltrationMission(game));
        missions.add(new LiberateTownMission(game));
        missions.add(new MiliceParadeDayMission(game));
        missions.add(new OfficersMansionMission(game));
        missions.add(new SabotageMission(game));
        missions.add(new TakeOutTheBridgesMission(game));
        missions.add(new UndergroundNewspaperMission(game));

        missions1 = missions.stream().filter(m -> m.getDifficulty() == 1).collect(Collectors.toList());
        missions2 = missions.stream().filter(m -> m.getDifficulty() == 2).collect(Collectors.toList());
        reshuffle1();
        reshuffle2();
    }

    public Mission draw(int difficulty) {
        return difficulty == 1? draw1(): draw2();
    }

    public Mission draw1(){
        if (missions1.isEmpty())
            reshuffle1();
        Mission mission = missions1.remove(0);
        discard1.add(mission);
        return mission;
    }

    public Mission draw2(){
        if (missions2.isEmpty())
            reshuffle2();
        Mission mission = missions2.remove(0);
        discard2.add(mission);
        return mission;
    }

    public void reshuffle1() {
        missions1.addAll(discard1);
        discard1.clear();
        Collections.shuffle(missions1);
    }

    public void reshuffle2() {
        missions2.addAll(discard2);
        discard2.clear();
        Collections.shuffle(missions2);
    }
}
