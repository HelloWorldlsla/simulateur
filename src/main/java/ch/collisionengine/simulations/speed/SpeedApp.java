package ch.collisionengine.simulations.speed;

import ch.collisionengine.simulations.fixedBullets.FixedParticleEngine;
import ch.collisionengine.simulator.App;
import ch.collisionengine.simulator.Graph;
import ch.collisionengine.simulator.Particle;
import ch.collisionengine.window.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class SpeedApp implements App {

    private final JTextField particleCount = new JTextField("1000");
    private final JTextField particleSpeed = new JTextField("20");
    private final int COUNT = 125;
    private FixedParticleEngine engine;
    private double[] values;
    private double timeout = 0;

    @Override
    public void setup(JPanel toolBar) {
        toolBar.removeAll();
        toolBar.setLayout(new GridLayout(2, 2, 10, 10));
        toolBar.add(new JLabel("Nombre"));
        toolBar.add(particleCount);
        toolBar.add(new JLabel("Vitesse"));
        toolBar.add(particleSpeed);
        toolBar.revalidate();
        engine = new FixedParticleEngine(50, 50, 1000,
                new FixedParticleEngine.EngineSettings(0.2, 1, 20, Color.RED));
        values = new double[COUNT];
    }

    @Override
    public void reset() {
        engine = new FixedParticleEngine(50, 50, Integer.parseInt(particleCount.getText()),
                new FixedParticleEngine.EngineSettings(0.2, 1, Double.parseDouble(particleSpeed.getText()), Color.RED));
        values = new double[COUNT];
        timeout = 0;
    }

    @Override
    public void update(double time) {
        if (timeout == 0) {
            for (int i = 0; i < 200; i++) {
                engine.update(time);
            }
        }
        timeout += time;

        engine.update(time);
        final double[] newValues = new double[values.length];
        engine.particles.stream().map(Particle::velocity)
                .mapToLong(Math::round)
                .mapToInt(l -> (int) l)
                .filter(i -> i < values.length)
                .forEach(i -> newValues[i] = newValues[i] + 1);
        for (int i = 0; i < values.length; i++) {
            values[i] = (values[i] * (timeout - time) + newValues[i] * time) / timeout;
        }
    }

    @Override
    public Drawable drawable() {
        return (g, sizeScale) -> {
            final AffineTransform transform = g.getTransform();
            g.setTransform(new AffineTransform(transform));
            g.translate(-8 * sizeScale, -5 * sizeScale);
            g.scale(0.2, 0.2);
            engine.draw(g, sizeScale);


            final Graph graph = new Graph("Distribution des vitesses", "Vitesse [m/s]", "nombre de particules", 10, 10);
            for (double value : values) {
                graph.values.add(value);
            }
            graph.horizontalScale = 1.0 / values.length;
            graph.verticalScale = 0.02;

            g.setColor(Color.BLACK);
            g.setTransform(transform);
            g.translate(2 * sizeScale, -0 * sizeScale);
            graph.draw(g, sizeScale / 2);
        };

    }

    @Override
    public String toString() {
        return "Courbe de MaxWell";
    }
}
