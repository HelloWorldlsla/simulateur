package ch.collisionengine.simulations.advancedBalls;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.simulator.Arrow;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.SimpleGrid;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class AdvancedBallEngine implements Engine {

    private final AdvancedBall a;
    private final AdvancedBall b;
    private final List<Vector> listCMPos = new ArrayList<>();
    public double d;
    int sleep = 0;
    private Vector collisionLocation = null;
    private Vector collisionVector = null;
    private Vector collisionVectorV1 = null;
    private Vector collisionVectorV2 = null;
    private Vector oldVa = null;
    private Vector oldVb = null;
    private int nextPosTrace = 0;
    private boolean centered = false;

    public AdvancedBallEngine(AdvancedBall a, AdvancedBall b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void update(double time) {
        if (sleep > 0) {
            sleep--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nextPosTrace--;
        if (nextPosTrace < 0) {
            nextPosTrace = 20;
            listCMPos.add(getCentreOfMass());
        }
        a.update(time, centered ? getCentreOfMass().scaleLocal(-1) : new Vector());
        b.update(time, centered ? getCentreOfMass().scaleLocal(-1) : new Vector());
        if (collisionVector == null && a.position.sub(b.position).length() < a.radius + b.radius) {
            collide();
            sleep = 2;
        }
    }

    private Vector getCentreOfMass() {
        return a.position.scale(a.mass).addLocal(b.position.scale(b.mass)).scaleLocal(1 / (a.mass + b.mass));
    }

    public void reset(boolean centered) {
        this.centered = centered;
        listCMPos.clear();
        nextPosTrace = 0;
        a.reset();
        b.reset();
        collisionLocation = null;
        collisionVector = null;
    }

    private void collide() {
        this.oldVa = a.velocity;
        this.oldVb = b.velocity;
        final Vector collisionVector = a.position.sub(b.position).unitLocal();
        this.collisionVector = collisionVector;
        this.collisionLocation = centered ? getCentreOfMass().scaleLocal(-1).addLocal(a.position.add(b.position).scaleLocal(0.5)) : a.position.add(b.position).scaleLocal(0.5);
        final double v1 = a.velocity.projection(collisionVector);
        final double v2 = b.velocity.projection(collisionVector);
        final Vector v1y = a.velocity.sub(collisionVector.scale(v1));
        final Vector v2y = b.velocity.sub(collisionVector.scale(v2));
        this.collisionVectorV1 = collisionVector.scale(v1);
        this.collisionVectorV2 = collisionVector.scale(v2);


        final double v1f = (a.mass - b.mass) / (a.mass + b.mass) * v1 + 2 * b.mass / (a.mass + b.mass) * v2;
        final double v2f = 2 * a.mass / (a.mass + b.mass) * v1 + (b.mass - a.mass) / (a.mass + b.mass) * v2;

        a.velocity = collisionVector.scale(v1f).addLocal(v1y);
        b.velocity = collisionVector.scale(v2f).addLocal(v2y);
    }

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        final AffineTransform transform = g.getTransform();
        final Vector centreOfMass = getCentreOfMass().scaleLocal(-sizeScale);
        g.translate(centreOfMass.x, centreOfMass.y);
        final AffineTransform centreTransform = centered ? g.getTransform() : transform;
        g.setTransform(transform);


        g.setColor(Color.RED);
        a.draw(g, sizeScale, transform, centreTransform);
        g.setColor(Color.GREEN);
        b.draw(g, sizeScale, transform, centreTransform);
        a.drawArrow(g, sizeScale, transform, centreTransform);
        b.drawArrow(g, sizeScale, transform, centreTransform);

        if (collisionVector != null) {
            g.setColor(Color.BLUE);
            final Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(5));
            final Vector p = collisionLocation.scale(sizeScale);
            final Vector v = collisionVector.scale(sizeScale);
            g.drawLine((int) p.x, (int) p.y, (int) (v.y + p.x), (int) (-v.x + p.y));
            g.drawLine((int) p.x, (int) p.y, (int) (-v.y + p.x), (int) (v.x + p.y));

            g.setColor(Color.GRAY);
            p.subLocal(v.scaleLocal(100));
            v.scaleLocal(100);
            g.drawLine((int) p.x, (int) p.y, (int) (v.x + p.x), (int) (v.y + p.y));
            g.setStroke(s);
            if (!centered) {
                {
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(b.position);
                    arrow.vec.set(collisionVectorV1);
                    arrow.drawTransparent(g, sizeScale);
                }
                {
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(a.position);
                    arrow.vec.set(collisionVectorV2);
                    arrow.drawTransparent(g, sizeScale);
                }
                {
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(b.position);
                    arrow.vec.set(collisionVectorV2);
                    arrow.drawTransparent(g, sizeScale);
                }
                {
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(a.position);
                    arrow.vec.set(collisionVectorV1);
                    arrow.drawTransparent(g, sizeScale);
                }

                {
                    g.setColor(Color.PINK);
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(a.position);
                    arrow.vec.set(oldVa);
                    arrow.drawTransparent(g, sizeScale);
                }
                {
                    final Arrow arrow = new Arrow();
                    arrow.pos.set(b.position);
                    arrow.vec.set(oldVb);
                    arrow.drawTransparent(g, sizeScale);
                }
            }
        }

        g.setTransform(centreTransform);
        g.setColor(Color.LIGHT_GRAY);
        final double radius1 = a.radius / 20 + b.radius / 20;
        if (centered) {
            final Vector v = getCentreOfMass();
            g.drawOval((int) ((v.x - radius1) * sizeScale), (int) ((v.y - radius1) * sizeScale), (int) (radius1 * 2 * sizeScale), (int) (radius1 * 2 * sizeScale));
        } else {
            for (Vector v : listCMPos) {
                g.drawOval((int) ((v.x - radius1) * sizeScale), (int) ((v.y - radius1) * sizeScale), (int) (radius1 * 2 * sizeScale), (int) (radius1 * 2 * sizeScale));
            }
        }

        new SimpleGrid(20).draw(g, sizeScale);
    }
}
