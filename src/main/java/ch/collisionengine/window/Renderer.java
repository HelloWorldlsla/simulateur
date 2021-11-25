package ch.collisionengine.window;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    public final Object sync = new Object();
    private final double sizeScale;
    private Drawable drawable;

    public Renderer(Drawable drawable, double sizeScale) {
        this.sizeScale = sizeScale;
        this.drawable = drawable;
    }

    @Override
    public void paint(Graphics g1) {
        synchronized (sync) {
            final Graphics2D g = (Graphics2D) g1;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.translate(getWidth() / 2, getHeight() / 2);
            g.scale(1, -1);
            drawable.draw(g, sizeScale * getWidth()/2000);

        }
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
