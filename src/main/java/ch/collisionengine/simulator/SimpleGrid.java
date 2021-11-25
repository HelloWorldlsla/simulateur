package ch.collisionengine.simulator;

import ch.collisionengine.window.Drawable;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class SimpleGrid implements Drawable {

    private final double width;

    public SimpleGrid(double width) {
        this.width = width;
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(Color.LIGHT_GRAY);
        final GeneralPath path = new GeneralPath();
        for (double i = -sizeScale * width / 2; i < sizeScale * width / 2; i += sizeScale) {
            path.moveTo(i, sizeScale * width / 2);
            path.lineTo(i, -sizeScale * width / 2);
            path.moveTo(sizeScale * width / 2, i);
            path.lineTo(-sizeScale * width / 2, i);
        }
        g.draw(path);
    }
}
