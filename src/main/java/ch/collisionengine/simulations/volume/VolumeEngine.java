package ch.collisionengine.simulations.volume;

import ch.collisionengine.simulations.fixedBullets.FixedParticleEngine;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.Graph;
import ch.collisionengine.simulator.Particle;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static ch.collisionengine.simulations.calculator.CalculatorEngine.R;

public class VolumeEngine implements Engine {

    public final double size;
    public final Graph graph;
    private final FixedParticleEngine engine;
    private double totalImpulse = 0;

    public VolumeEngine(double size, double temp) {
        this.engine = new FixedParticleEngine(size, size, 100,
                new FixedParticleEngine.EngineSettings(0.2, 1, Math.sqrt(3 * R * temp / 500), Color.RED), this::onCollision);
        this.size = size;
        this.graph = new Graph("Pression pour différents volumes", "Volume [m²]", "Pression [N/m]", 0);
        this.graph.verticalScale = 0.0005;
        this.graph.horizontalScale = 0.01;
    }

    public double getAveragePressure() {
        return totalImpulse / engine.totalTime / size / size;
    }

    private void onCollision(Particle particle, double time) {
        totalImpulse += particle.mass * particle.velocity.x * 2;
    }

    @Override
    public void update(double time) {
        engine.update(time);
        graph.values.addLast(getAveragePressure());
        if (graph.values.size() > 100) {
            graph.values.removeFirst();
        }
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        final AffineTransform transform = g.getTransform();
        g.translate(-size / 2 * sizeScale, -size / 2 * sizeScale);

        engine.draw(g, sizeScale);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, (int) (size * sizeScale), (int) (size * sizeScale));

        g.translate(-20 * sizeScale, 0);
        graph.draw(g, sizeScale * 2);
        g.setTransform(transform);
    }
}