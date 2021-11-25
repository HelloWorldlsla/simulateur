package ch.collisionengine.simulations.balls;

import ch.collisionengine.simulator.Arrow;

import java.awt.*;

public class Ball {

    private final Arrow velocityArrow = new Arrow();
    public double mass;
    public double radius;
    public double position;
    public double velocity;

    public Ball(double mass, double radius, double position, double velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
    }

    public void update(double time) {
        position += velocity * time;
        velocityArrow.pos.set(position, 0);
        velocityArrow.vec.set(velocity, 0);
    }

    public void draw(Graphics2D g, double sizeScale) {
        g.fillOval((int) ((position - radius) * sizeScale), (int) (sizeScale * -radius), (int) (radius * 2 * sizeScale), (int) (radius * 2 * sizeScale));
    }

    public void drawArrow(Graphics2D g, double sizeScale) {
        g.setColor(Color.YELLOW);
        velocityArrow.draw(g, sizeScale);
    }
}
