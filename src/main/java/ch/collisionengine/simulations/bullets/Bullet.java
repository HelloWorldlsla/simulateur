package ch.collisionengine.simulations.bullets;

import ch.collisionengine.maths.Vector;

import java.awt.*;


public class Bullet {

    public double mass;
    public double radius;
    public Vector position;
    public Vector velocity;

    public Bullet(double mass, double radius, Vector position, Vector velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
    }

    public void update(double time) {
        position.addLocal(velocity.scale(time));
    }

    public void draw(Graphics2D g, double sizeScale) {
        g.fillOval((int) ((position.x - radius ) * sizeScale), (int) ((position.y - radius) * sizeScale), (int) (radius * 2 * sizeScale), (int) (radius * 2 * sizeScale));
    }

    public double kineticEnergy() {
        return 0.5 * mass * velocity.lengthSquared();
    }
}
