package ch.collisionengine.simulator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class ArrayGraph {

    private static final int SIZE = 10;
    public final double[] values;
    private final String name;
    public double verticalScale = 10;
    public double horizontalScale = 10;

    public ArrayGraph(String name, double[] values) {
        this.name = name;
        this.values = values;
    }

    public void draw(Graphics2D g, double sizeScale) {
        g.drawRect(0, 0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));

        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        g.scale(1, -1);
        g.drawString(name, 0, 0);

        final GeneralPath path = new GeneralPath();
        path.moveTo(0, -values[0] * sizeScale * verticalScale * SIZE);
        for (int i = 0; i < values.length; i++) {
            path.lineTo(i * sizeScale * horizontalScale * SIZE, -values[i] * sizeScale * verticalScale * SIZE);
        }

        g.draw(path);
        g.setTransform(transform);
    }
}
