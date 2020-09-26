package maquis.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ImageUtil {
    private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
    private static final String IMAGE_DIR = "image/";

    private static Map<String, Image> cache = new HashMap<>();

    public static Image load(String name, Integer w, Integer h){
        if (cache.containsKey(name))
            return cache.get(name);
        try {
            Image bi = ImageIO.read(ImageUtil.class.getClassLoader().getResourceAsStream(IMAGE_DIR + name));
            if (w != null || h != null){
                int iw = bi.getWidth(null);
                int ih = bi.getHeight(null);
                if (h == null){
                    double scale = ((double) w) / ((double) iw);
                    h = (int) (ih * scale);
                }
                else if (w == null){
                    double scale = h / ih;
                    w = (int) (iw * scale);
                }
                bi = bi.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
            }
            cache.put(name, bi);
            return bi;
        }
        catch (Exception e){
            logger.severe("Unable to load image '" + IMAGE_DIR + name + "': " + e);
        }
        return null;
    }

    public static Image load(String name){
        return load(name, null, null);
    }

    public static Image load(String name, Integer w){
        return load(name, w, null);
    }

    private ImageUtil(){}
}
