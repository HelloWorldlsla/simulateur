package ch.collisionengine.simulations.multivol;

import ch.collisionengine.simulations.volume.VolumeEngine;
import ch.collisionengine.simulator.Engine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class MultiVolumeEngine implements Engine {

    private static final double SIZE = 10;
    private final List<VolumeEngine> engines = new ArrayList<>();

    public MultiVolumeEngine(double temp) {
        for (int i = 7; i < 55; i++) {
            engines.add(new VolumeEngine(i, temp));
        }
    }

    @Override
    public void update(double time) {
        engines.forEach(e -> e.update(time));
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(Color.BLACK);
        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        g.translate(3 * sizeScale, sizeScale);
//        g.setFont(g.getFont().deriveFont(40.0f));
//
//        final String name = "P/V";
//        final double verticalScale = 0.0015;
//        final double horizontalScale = 0.02 / engines.size();
//
//        final double SIZE = 10;
//        g.drawRect(0, 0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));
//
//        final AffineTransform transform1 = g.getTransform();
//        g.setTransform(new AffineTransform(transform1));
//        g.scale(1, -1);
//        g.drawString(name, 0, 0);
//
//        if (engines.isEmpty()) {
//            g.setTransform(transform1);
//            return;
//        }
//
//        final GeneralPath path = new GeneralPath();
//        path.moveTo(engines.get(0).size * engines.get(0).size * horizontalScale * sizeScale * SIZE, -engines.get(0).getAveragePressure() * sizeScale * verticalScale * SIZE);
//
//        engines.forEach(e -> path.lineTo(
//                e.size * e.size * horizontalScale * sizeScale * SIZE,
//                -e.getAveragePressure() * sizeScale * verticalScale * SIZE));
//
//        g.draw(path);
//        g.setTransform(transform);
//
//
        final AffineTransform transform1 = g.getTransform();
        g.setTransform(new AffineTransform(transform1));
        g.scale(1, -1);

        if (engines.isEmpty()) {
            g.setTransform(transform1);
            return;
        }
        final double verticalScale = 0.2;
        final double horizontalScale = 0.0001;
        final GeneralPath path = new GeneralPath();
        path.moveTo(engines.get(0).size * engines.get(0).size*engines.get(0).size * horizontalScale * sizeScale * SIZE, -engines.get(0).getAveragePressure() * sizeScale * verticalScale * SIZE);

        engines.forEach(e -> path.lineTo(
                e.size * e.size*e.size * horizontalScale * sizeScale * SIZE,
                -e.getAveragePressure() * sizeScale * verticalScale * SIZE));

        final Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(1));
        g.draw(path);
        g.setStroke(stroke);
        g.setColor(Color.WHITE);
        g.fillRect((int) (SIZE * sizeScale) + 1, -(int) (SIZE * sizeScale)/2, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));
        g.fillRect(-(int) (SIZE * sizeScale), -(int) (SIZE * sizeScale)*2, (int) (SIZE * sizeScale)*2, (int) (SIZE * sizeScale) - 1);
        g.setColor(Color.BLACK);
        g.drawString("Pression [N/m²]", -20, -(int) (SIZE * sizeScale) - 45);
        g.drawString("Volume [m³]", (int) (SIZE * sizeScale) - 200, 80);
        g.drawRect(0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));
        g.drawRect(-100, -100 - (int) (SIZE * sizeScale), (int) (SIZE * sizeScale) + 200, (int) (SIZE * sizeScale) + 200);

        for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
            g.drawString(String.valueOf((int) i), (float) (i * sizeScale), 50);
            g.drawLine((int) (i * sizeScale), (int) (-SIZE * sizeScale), (int) (i * sizeScale), 10);
        }
        for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
            g.drawString(String.valueOf((int) i), -70, (float) (-i * sizeScale));
            g.drawLine((int) (SIZE * sizeScale), (int) (-i * sizeScale), -10, (int) (-i * sizeScale));
        }

        g.setTransform(transform);


    }
}
