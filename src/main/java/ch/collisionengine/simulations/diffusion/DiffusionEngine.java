package ch.collisionengine.simulations.diffusion;

import ch.collisionengine.simulations.fixedBullets.FixedParticleEngine;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.Graph;
import ch.collisionengine.simulator.Particle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.stream.Collectors;

public class DiffusionEngine implements Engine {

    private final FixedParticleEngine engineRed;

    private final FixedParticleEngine engineBlue;
    private final Graph blueGraph = new Graph("Énergie cinétique", "temps [s]", "Ecin [kJ]", 1000);
    private final Graph redGraph = new Graph("Énergie cinétique", "temps [s]", "Ecin [kJ]", 1000);
    private FixedParticleEngine mixed;

    public DiffusionEngine(double redMass, double redRadius, int redParticleCount, double redMaxSpeed, double blueMass, double blueRadius, int blueParticleCount, double blueMaxSpeed) {
        engineRed = new FixedParticleEngine(10, 20, redParticleCount,
                new FixedParticleEngine.EngineSettings(redRadius, redMass, redMaxSpeed, Color.RED));
        engineBlue = new FixedParticleEngine(10, 20, blueParticleCount,
                new FixedParticleEngine.EngineSettings(blueRadius, blueMass, blueMaxSpeed, Color.BLUE));
    }

    @Override
    public void update(double time) {
        if (mixed == null) {
            engineRed.update(time);
            engineBlue.update(time);
        } else {
            mixed.update(time);
        }
    }

    public void removeWall() {
        final List<Particle> particles = engineRed.particles.stream()
                .peek(particle -> particle.position.x += 10)
                .collect(Collectors.toList());
        particles.addAll(engineBlue.particles);
        mixed = new FixedParticleEngine(20, 20, particles);
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        if (mixed == null) {
            AffineTransform transform = g.getTransform();
            g.translate(-10 * sizeScale, -10 * sizeScale);
            engineBlue.draw(g, sizeScale);
            g.setTransform(transform);

            transform = g.getTransform();
            g.translate(0, -10 * sizeScale);
            engineRed.draw(g, sizeScale);
            g.setTransform(transform);
        } else {
            final AffineTransform transform = g.getTransform();
            g.translate(-10 * sizeScale, -10 * sizeScale);
            mixed.draw(g, sizeScale);
            g.setTransform(transform);
        }

        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect((int) (-10 * sizeScale), (int) (-10 * sizeScale), (int) (20 * sizeScale), (int) (20 * sizeScale));
        if (mixed == null) {
            g.setColor(Color.BLACK);
            g.drawLine(0, (int) (-10 * sizeScale), 0, (int) (10 * sizeScale));
        }
        drawGraph(g, sizeScale);
    }

    public void drawGraph(Graphics2D g, double sizeScale) {
        redGraph.verticalScale = 0.0002;
        blueGraph.verticalScale = 0.0002;
        redGraph.horizontalScale = 0.002;
        blueGraph.horizontalScale = 0.002;

        final double kineticEnergyBlue = engineBlue.particles.stream().mapToDouble(Particle::kineticEnergy).sum();
        blueGraph.values.addLast(kineticEnergyBlue);
        if (blueGraph.values.size() > 500) {
            blueGraph.values.removeFirst();
        }
        final double kineticEnergyRed = engineRed.particles.stream().mapToDouble(Particle::kineticEnergy).sum();
        redGraph.values.addLast(kineticEnergyRed);
        if (redGraph.values.size() > 500) {
            redGraph.values.removeFirst();
        }
        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        g.translate(10 * sizeScale, -10 * sizeScale);
        g.setColor(Color.RED);
        redGraph.draw(g, sizeScale);
        g.setColor(Color.BLUE);
        blueGraph.draw(g, sizeScale);
        g.setTransform(transform);
    }
}