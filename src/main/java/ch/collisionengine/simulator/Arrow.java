package ch.collisionengine.simulator;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.window.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class Arrow implements Drawable {

    public final Vector pos = new Vector();
    public final Vector vec = new Vector();

    @Override
    public void draw(Graphics2D g, double sizeScale) {
        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        g.translate(pos.x * sizeScale, pos.y * sizeScale);
        g.rotate(Math.atan2(vec.y, vec.x));
        sizeScale *= vec.length() / 2;

        final GeneralPath path = new GeneralPath();
        path.moveTo(0, -sizeScale * 0.1);
        path.lineTo(sizeScale * 0.7, -sizeScale * 0.1);
        path.lineTo(sizeScale * 0.7, -sizeScale * 0.3);
        path.lineTo(sizeScale, 0);
        path.lineTo(sizeScale * 0.7, sizeScale * 0.3);
        path.lineTo(sizeScale * 0.7, sizeScale * 0.1);
        path.lineTo(0, sizeScale * 0.1);
        g.fill(path);

        g.setTransform(transform);
    }

    public void drawTransparent(Graphics2D g, double sizeScale) {
        final AffineTransform transform = g.getTransform();
        g.setTransform(new AffineTransform(transform));
        final Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(4));
        g.translate(pos.x * sizeScale, pos.y * sizeScale);
        g.rotate(Math.atan2(vec.y, vec.x));
        sizeScale *= vec.length() / 2;

        final GeneralPath path = new GeneralPath();
        path.moveTo(0, -sizeScale * 0.1);
        path.lineTo(sizeScale * 0.7, -sizeScale * 0.1);
        path.lineTo(sizeScale * 0.7, -sizeScale * 0.3);
        path.lineTo(sizeScale, 0);
        path.lineTo(sizeScale * 0.7, sizeScale * 0.3);
        path.lineTo(sizeScale * 0.7, sizeScale * 0.1);
        path.lineTo(0, sizeScale * 0.1);
        path.lineTo(0, -sizeScale * 0.1);
        g.draw(path);

        g.setStroke(stroke);
        g.setTransform(transform);
    }
}
