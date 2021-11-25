package ch.collisionengine.window.time;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class FontAwesome {

    public static final Font font;
    public static final String PLAY_ICON = "\uf04b";
    public static final String PAUSE_ICON = "\uf04c";
    public static final String REPLAY_ICON = "\uf01e";

    static {
        try (InputStream stream = FontAwesome.class.getClassLoader().getResourceAsStream("fontawesome-webfont.ttf")) {
            final Font baseFont = Font.createFont(Font.TRUETYPE_FONT, requireNonNull(stream));
            font = baseFont.deriveFont(Font.PLAIN, 40f);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
