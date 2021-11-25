package ch.collisionengine.simulator;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.window.Drawable;

import java.awt.*;

public class Particle implements Drawable {

    public final Vector position;
    public final double radius;
    public final double mass;
    public Vector velocity;
    public Color color;

    public Particle(Vector position, Vector velocity, double radius, double mass, Color color) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    public void update(double time) {
        position.addLocal(velocity.scale(time));
    }

    public double kineticEnergy() {
        return mass * velocity.lengthSquared() / 2;
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(color);
        g.fillOval((int) (sizeScale * (position.x - radius)),
                (int) (sizeScale * (position.y - radius)),
                (int) (2 * radius * sizeScale),
                (int) (2 * radius * sizeScale));
    }

    public boolean touches(Particle other) {
        return position.sub(other.position).length() < radius + other.radius;
    }

    public Particle duplicate() {
        return new Particle(position, velocity, radius, mass, color);
    }

    public double velocity() {
        return velocity.length();
    }

}
