package ch.collisionengine.simulations.multicalc;

import ch.collisionengine.simulator.App;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class MultiCalcApp implements App {

    private JPanel toolBar;
    private List<MultiCalcEngine> engines;

    @Override
    public void setup(JPanel toolBar) {
        this.toolBar = toolBar;
        this.toolBar.removeAll();

        toolBar.revalidate();
        engines = new ArrayList<>();
        for (int i = 7; i < 20; i++) {
            engines.add(new MultiCalcEngine(i));
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
            g.setTransform(new AffineTransform(transform));
            g.translate(-5 * sizeScale, -5 * sizeScale);
            engines.forEach(e -> e.draw(g, sizeScale));
            g.setTransform(transform);
        };
    }

    @Override
    public String toString() {
        return "Multi-calculator";
    }
}
