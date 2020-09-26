package maquis;

import maquis.game.Difficulty;
import maquis.game.Game;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Model {
    private static final List<String> BOOLEAN_VALUES = Arrays.asList("true", "t", "1", "y", "yes");

    private static Logger logger = Logger.getLogger(Model.class.getName());

    private Game game;
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(Model.class.getClassLoader().getResourceAsStream("application.properties"));
        }
        catch (Exception e){
            logger.severe("" + e);
        }
    }

    public Model(){

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Difficulty getDifficulty(){
        String diff = getProperty("game.difficulty", "normal");
        return Difficulty.valueOf(diff.toUpperCase());
    }

    public static String getProperty(String name, String def){
        return properties.getProperty(name, def);
    }

    public static boolean getProperty(String name, boolean def){
        String value = properties.getProperty(name, (String) null);
        if (value == null)
            return def;
        return BOOLEAN_VALUES.contains(value.toLowerCase());
    }

    public static int getProperty(String name, int def){
        String value = properties.getProperty(name, (String) null);
        if (value == null)
            return def;
        return Integer.decode(value);
    }

    public static List<String> getProperty(String name, List<String> def){
        String value = getProperty(name, (String) null);
        if (value == null)
            return def;
        String[] parts = value.split(",");
        return Arrays.asList(parts).stream().map(p -> p.trim()).collect(Collectors.toList());
    }
}
