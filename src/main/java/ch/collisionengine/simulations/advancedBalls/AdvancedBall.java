package ch.collisionengine.simulations.advancedBalls;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.simulator.Arrow;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;


public class AdvancedBall {

    private final List<Vector> positions = new ArrayList<>();
    private final Arrow velocityArrow = new Arrow();
    public double mass;
    public double radius;
    public Vector position;
    public Vector velocity;
    private int nextPosTrace = 0;

    public AdvancedBall(double mass, double radius, Vector position, Vector velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
    }

    public void update(double time, Vector centreOfMass) {
        nextPosTrace--;
        if (nextPosTrace < 0) {
            nextPosTrace = 10;
            positions.add(centreOfMass.add(position));
        }
        position.addLocal(velocity.scale(time));
        velocityArrow.pos.set(position);
        velocityArrow.vec.set(velocity);
    }

    public void reset() {
        positions.clear();
    }

    public void draw(Graphics2D g, double sizeScale, AffineTransform transform, AffineTransform centreTransform) {
        final double radius1 = this.radius / 20;
        for (Vector v : positions) {
            g.fillOval((int) ((v.x - radius1) * sizeScale), (int) ((v.y - radius1) * sizeScale), (int) (radius1 * 2 * sizeScale), (int) (radius1 * 2 * sizeScale));
        }
        g.setTransform(centreTransform);
        g.fillOval((int) ((position.x - radius) * sizeScale), (int) ((position.y - radius) * sizeScale), (int) (radius * 2 * sizeScale), (int) (radius * 2 * sizeScale));
        g.setTransform(transform);

    }

    public double kineticEnergy() {
        return 0.5 * mass * velocity.lengthSquared();
    }

    public void drawArrow(Graphics2D g, double sizeScale, AffineTransform transform, AffineTransform centreTransform) {
        if (centreTransform == transform) {
            g.setColor(Color.YELLOW);
            velocityArrow.draw(g, sizeScale);
        } else {
            g.setTransform(centreTransform);
//            final Vector vector = new Vector(centreTransform.getTranslateX(), centreTransform.getTranslateY());
//            vector.subLocal(new Vector(transform.getTranslateX(), transform.getTranslateY()));
//            velocityArrow.vec.subLocal(vector.scaleLocal(1 / sizeScale));
//            g.setColor(Color.YELLOW);
//            velocityArrow.draw(g, sizeScale);
//            g.setTransform(transform);
        }
    }
}
