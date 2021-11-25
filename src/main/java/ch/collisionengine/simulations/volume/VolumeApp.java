package ch.collisionengine.simulations.volume;

import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class VolumeApp implements App {

    private List<VolumeEngine> engines;
    private JPanel toolBar;

    @Override
    public void setup(JPanel toolBar) {
        this.toolBar = toolBar;
        this.toolBar.removeAll();
        toolBar.revalidate();
        engines = new ArrayList<>();
        for (int i = 7; i < 40; i++) {
            engines.add(new VolumeEngine(i, 100 + 273.15));
        }
    }

    @Override
    public void reset() {
        setup(toolBar);
    }

    @Override
    public void update(double time) {
        engines.parallelStream().forEach(e -> e.update(time));
    }

    @Override
    public Drawable drawable() {
        return (g, sizeScale) -> {
            final AffineTransform transform = g.getTransform();
            final int colSize = (int) (engines.size() / Math.sqrt(engines.size()));
            final double scale = 32;
            final double engineSize = 20.0;

            final double tableHeight = -colSize * engineSize / scale * sizeScale * 2;

            g.translate(-engines.size() * engineSize / scale / colSize * sizeScale * 2 * 2, tableHeight / 2);

            g.setFont(g.getFont().deriveFont(5.0f));
            bigLoop:
            for (int col = 0; ; col++) {
                g.translate(engineSize / scale * sizeScale * 2 * 2, 0);
                for (int row = 0; row < colSize; row++) {

                    if (col * colSize + row >= engines.size()) {
                        break bigLoop;
                    }

                    g.translate(0, engineSize / scale * sizeScale * 2);
                    final VolumeEngine engine = engines.get(col * colSize + row);
                    engine.draw(g, sizeScale / scale);
                }
                g.translate(0, tableHeight);
            }

            g.setTransform(transform);
            g.translate(3 * sizeScale, 0);
            g.setFont(g.getFont().deriveFont(40.0f));

            final String name = "P/V";
            final double verticalScale = 0.15;
            final double horizontalScale = 0.02 / engines.size();

            g.scale(0.5, 0.5);
            final double SIZE = 10;
            g.drawRect(0, 0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));
            g.drawRect(-100, -100, (int) (SIZE * sizeScale) + 200, (int) (SIZE * sizeScale) + 200);

            final AffineTransform transform1 = g.getTransform();
            g.setTransform(new AffineTransform(transform1));
            g.scale(1, -1);
            g.drawString("Pression [N/m²]", -20, -(int) (SIZE * sizeScale) - 45);
            g.drawString("Volume [m³]", (int) (SIZE * sizeScale) - 200, 80);

            for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
                g.drawString(String.valueOf((int)i), (float) (i * sizeScale), 50);
                g.drawLine((int) (i * sizeScale), (int) (-SIZE * sizeScale), (int) (i * sizeScale), 10);
            }
            for (double i = 0; i <= SIZE; i += SIZE / 10.0) {
                g.drawString(String.valueOf((int)i), -70, (float) (-i * sizeScale));
                g.drawLine((int) (SIZE * sizeScale), (int) (-i * sizeScale), -10, (int) (-i * sizeScale));
            }

            if (engines.isEmpty()) {
                g.setTransform(transform1);
                return;
            }

            final GeneralPath path = new GeneralPath();
            path.moveTo(engines.get(0).size * engines.get(0).size * horizontalScale * sizeScale * SIZE, -engines.get(0).getAveragePressure() * sizeScale * verticalScale * SIZE);

            engines.forEach(e -> path.lineTo(
                    e.size * e.size * horizontalScale * sizeScale * SIZE,
                    -e.getAveragePressure() * sizeScale * verticalScale * SIZE));

            final Stroke stroke = g.getStroke();
            g.setStroke(new BasicStroke(4));
            g.draw(path);
            g.setStroke(stroke);
            g.setTransform(transform);


            g.setTransform(transform);
        };
    }

    @Override
    public String toString() {
        return "Volume";
    }
}
