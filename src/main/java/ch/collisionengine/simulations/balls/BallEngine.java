package ch.collisionengine.simulations.balls;

import ch.collisionengine.simulator.Arrow;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.SimpleGrid;

import java.awt.*;

public class BallEngine implements Engine {

    private final Ball a;
    private final Ball b;
    double oldSpeedA;
    double oldSpeedB;

    public BallEngine(Ball a, Ball b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void update(double time) {
        a.update(time);
        b.update(time);
        if (Math.abs(a.position - b.position) < a.radius + b.radius) {
            collide();
        }
    }

    private void collide() {
        final double v1 = a.velocity;
        final double v2 = b.velocity;
        a.velocity = (a.mass - b.mass) / (a.mass + b.mass) * v1 + 2 * b.mass / (a.mass + b.mass) * v2;
        b.velocity = 2 * a.mass / (a.mass + b.mass) * v1 + (b.mass - a.mass) / (a.mass + b.mass) * v2;
        oldSpeedA = v1;
        oldSpeedB = v2;
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        g.setColor(Color.RED);
        a.draw(g, sizeScale);
        g.setColor(Color.GREEN);
        b.draw(g, sizeScale);
        a.drawArrow(g, sizeScale);
        b.drawArrow(g, sizeScale);
        {
            g.setColor(Color.PINK);
            final Arrow arrow = new Arrow();
            arrow.pos.set(a.position, 0);
            arrow.vec.set(oldSpeedA, 0);
            arrow.drawTransparent(g, sizeScale);
        }
        {
            final Arrow arrow = new Arrow();
            arrow.pos.set(b.position, 0);
            arrow.vec.set(oldSpeedB, 0);
            arrow.drawTransparent(g, sizeScale);
        }
        new SimpleGrid(20).draw(g, sizeScale);
    }
}
