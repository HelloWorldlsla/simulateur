package ch.collisionengine.simulations.fixedBullets;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.Particle;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class FixedParticleEngine implements Engine {

    public final List<Particle> particles;
    private final BiConsumer<Particle, Double> wallCollisionListener;
    private final double width;
    private final double height;
    public double totalTime = 0;

    public FixedParticleEngine(double width, double height, List<Particle> particles) {
        this.width = width;
        this.height = height;
        this.particles = particles;
        wallCollisionListener = null;
    }

    public FixedParticleEngine(double width, double height, int particleCount, EngineSettings settings) {
        this(width, height, particleCount, settings, null);
    }

    public FixedParticleEngine(double width, double height, int particleCount, EngineSettings settings, BiConsumer<Particle, Double> wallCollisionListener) {
        this.width = width;
        this.height = height;
        this.wallCollisionListener = wallCollisionListener;
        this.particles = new ArrayList<>();
        int tryCount = 0;
        for (int i = 0; i < particleCount; i++) {
            if (!addParticle(settings)) {
                i = -1;
                tryCount++;
                if (tryCount >= 20) {
                    System.out.println("Pas assez de place pour toutes les particules");
                    return;
                }
                particles.clear();
                //JOptionPane.showMessageDialog(null, "Pas assez de place pour toutes les particules");
            }
        }
    }

    private boolean addParticle(EngineSettings settings) {
        for (int i = 0; i < 1000; i++) {
            final Random random = new SecureRandom();
            final Vector position = new Vector(random.nextDouble() * (width - 2) + 1, random.nextDouble() * (height - 2) + 1);
            final Vector velocity = new Vector(settings.maxSpeed, 0).rotate(random.nextDouble() + random.nextInt(100));
            final Particle particle = new Particle(position, velocity, settings.radius, settings.mass, settings.color);

            if (particles.stream().noneMatch(particle::touches)) {
                particles.add(particle);
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(double time) {
        final double firstCollisionTime = time;
        totalTime += firstCollisionTime;
        particles.forEach(particle -> particle.update(firstCollisionTime));
        for (int i = 0; i < particles.size(); i++) {
            final Particle a = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                tryCollide(a, particles.get(j));
            }
            bounceAgainstWalls(a, firstCollisionTime);
        }
    }

    private void bounceAgainstWalls(Particle particle, double time) {
        if (particle.position.x - particle.radius <= 0) {
            particle.velocity.x = Math.abs(particle.velocity.x);
        }
        if (particle.position.y - particle.radius <= 0) {
            particle.velocity.y = Math.abs(particle.velocity.y);
        }
        if (particle.position.x + particle.radius >= width) {
            if (wallCollisionListener != null && particle.velocity.x > 0) {
                wallCollisionListener.accept(particle, time);
            }
            particle.velocity.x = -Math.abs(particle.velocity.x);
        }
        if (particle.position.y + particle.radius >= height) {
            particle.velocity.y = -Math.abs(particle.velocity.y);
        }
    }

    private void tryCollide(Particle a, Particle b) {
        if (a.touches(b)) {
            collide(a, b);
        }
    }

    private void collide(Particle a, Particle b) {
        final Vector collisionVector = a.position.sub(b.position).unitLocal();
        final double v1 = a.velocity.projection(collisionVector);
        final double v2 = b.velocity.projection(collisionVector);
        if (v2 - v1 < 0) {
            return;
        }
        final Vector v1y = a.velocity.sub(collisionVector.scale(v1));
        final Vector v2y = b.velocity.sub(collisionVector.scale(v2));

        final double v1f = (a.mass - b.mass) / (a.mass + b.mass) * v1 + 2 * b.mass / (a.mass + b.mass) * v2;
        final double v2f = 2 * a.mass / (a.mass + b.mass) * v1 + (b.mass - a.mass) / (a.mass + b.mass) * v2;

        a.velocity = collisionVector.scale(v1f).addLocal(v1y);
        b.velocity = collisionVector.scale(v2f).addLocal(v2y);
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(Color.BLACK);
        particles.forEach(particle -> particle.draw(g, sizeScale));
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, (int) (width * sizeScale), (int) (height * sizeScale));
    }

    public static class EngineSettings {
        public final double radius;
        public final double mass;
        public final double maxSpeed;
        public final Color color;

        public EngineSettings(double radius, double mass, double maxSpeed, Color color) {
            this.radius = radius;
            this.mass = mass;
            this.maxSpeed = maxSpeed;
            this.color = color;
        }
    }
}
