package ch.collisionengine.simulator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

public class Graph {

    private static final int SIZE = 10;
    public final LinkedList<Double> values = new LinkedList<>();
    private final String name;
    private final String xname;
    private final String yname;
    private final double precision;
    private final double xPrec;
    public double verticalScale = 10;
    public double horizontalScale = 10;

    public Graph(String name, String xname, String yname, double precision) {
        this(name, xname, yname, precision, 0);
    }

    public Graph(String name, String xname, String yname, double precision, double xPrec) {
        this.name = name;
        this.xname = xname;
        this.yname = yname;
        this.precision = precision;
        this.xPrec = xPrec;
    }

    public void draw(Graphics2D g, double sizeScale) {
        final Color color = g.getColor();
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));

        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        g.scale(0.8, 0.8);
        g.translate(0.1 * SIZE * sizeScale, 0.1 * SIZE * sizeScale);
        g.translate(SIZE * sizeScale / 2, SIZE * sizeScale / 2);
        new SimpleGrid(SIZE).draw(g, sizeScale);
        g.drawRect((int) (-SIZE * sizeScale / 2), (int) (-SIZE * sizeScale / 2), (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));
        g.setColor(Color.BLACK);
        g.setTransform(transform);
        g.scale(0.8, 0.8);
        g.translate(0.1 * SIZE * sizeScale, 0.1 * SIZE * sizeScale);
        g.scale(1, -1);

        if (precision != 0) {
            final Font font = g.getFont();
            g.setFont(font.deriveFont(40.0f));
            g.drawString(name, (float) (SIZE * sizeScale / 2) - 100, (float) (-SIZE * sizeScale) - 70);
            g.setFont(font);
            g.drawString(yname, -100, (float) (-SIZE * sizeScale) - 40);
            g.drawString(xname, (float) (SIZE * sizeScale) - 200, 50);
        }

        if (values.isEmpty()) {
            g.setTransform(transform);
            return;
        }
        final GeneralPath path = new GeneralPath();
        path.moveTo(0, -values.get(0) * sizeScale * verticalScale * SIZE);
        for (int i = 0; i < values.size(); i++) {
            path.lineTo(i * sizeScale * horizontalScale * SIZE, -values.get(i) * sizeScale * verticalScale * SIZE);
        }
        if (precision != 0) {
            for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
                g.drawString(String.valueOf((int) (i / verticalScale / precision)), -50, (float) (-i * sizeScale));
                g.drawLine(0, (int) (-i * sizeScale), -10, (int) (-i * sizeScale));
            }
        }

        if (xPrec != 0) {
            for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
                g.drawString(String.valueOf((int) (i / horizontalScale / xPrec)), (float) (i * sizeScale), 20);
                g.drawLine((int) (i * sizeScale), 0, (int) (i * sizeScale), 10);
            }
        }
        g.setColor(color);
        final Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(2));
        g.draw(path);
        g.setStroke(stroke);
        g.setTransform(transform);
    }
}
