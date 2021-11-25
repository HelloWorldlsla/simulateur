package ch.collisionengine.simulations.calculator;

import ch.collisionengine.simulations.fixedBullets.FixedParticleEngine;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.Graph;
import ch.collisionengine.simulator.Particle;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class CalculatorEngine implements Engine {
    /**
     * Speed [m/s] | Pressure [N/m]
     * 0.1    |    0.1
     * 0.25   |    2.58
     * 1      |    10
     * 2      |    40
     * 4      |    163
     * <p>
     * <p>
     * v      |    ~ 10 * (v^2)
     * v      |    p = N 8m * v^2 / L = 200 * 1 * Vx^2 / 20 = 10 * Vx^2
     */

    public static final double R = 8.31446;
    private static final double SIZE = 10;
    private final FixedParticleEngine engine;
    private final Graph graph;
    private final double width;
    private double totalImpulse = 0;

    public CalculatorEngine(double temp) {
        this.width = SIZE;
        this.engine = new FixedParticleEngine(SIZE, SIZE, 100,
                new FixedParticleEngine.EngineSettings(0.2, 1.67262192369 * Math.pow(10, -27), Math.sqrt(3 * R * temp / 1), Color.RED), this::onCollision);
        this.graph = new Graph("", "Pressure [N/m]", "Time [s]", 0);
        this.graph.verticalScale = Math.pow(10, 22);
        this.graph.horizontalScale = 0.01;
    }

    public CalculatorEngine(double temp, double width) {
        this.width = width;
        this.engine = new FixedParticleEngine(width, width, 100,
                new FixedParticleEngine.EngineSettings(0.2, 1.67262192369 * Math.pow(10, -27), Math.sqrt(3 * R * temp / 1), Color.RED), this::onCollision);
        this.graph = new Graph("", "Pressure [N/m]", "Time [s]", 0);
        this.graph.verticalScale = Math.pow(10, 22);
        this.graph.horizontalScale = 0.01;
    }

    public double getAveragePressure() {
        return totalImpulse / engine.totalTime / width / width;
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
        g.translate(-SIZE / 2 * sizeScale, -SIZE / 2 * sizeScale);

        engine.draw(g, sizeScale);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, (int) (SIZE * sizeScale), (int) (SIZE * sizeScale));

        g.translate(-20 * sizeScale, 0);
        graph.draw(g, sizeScale * 2);
        g.setTransform(transform);
    }
}