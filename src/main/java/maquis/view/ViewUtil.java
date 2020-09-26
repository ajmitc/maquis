package maquis.view;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class ViewUtil {
    public static final Font FORM_KEY_FONT   = new Font("Serif", Font.BOLD, 12);
    public static final Font FORM_VALUE_FONT = new Font("Serif", Font.PLAIN, 12);
    public static final Font FORM_VALUE_FONT_ITALICS = new Font("Serif", Font.ITALIC, 12);
    public static final Font FORM_TITLE_FONT = new Font("Serif", Font.BOLD, 16);

    public static Rectangle getStringBounds(Graphics2D g2, String str, int x, int y) {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    public static Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }


    private ViewUtil(){}
}
