package ch.collisionengine.simulations.bullets;

import ch.collisionengine.maths.Vector;
import ch.collisionengine.simulator.Engine;
import ch.collisionengine.simulator.SimpleGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BulletEngine implements Engine {

    private final List<Bullet> bullets = new ArrayList<>();


    public BulletEngine(double mass, double velocity, double radius, int count) {
        final Random random = new Random();
        for (int i = 0; i < count; i++) {
            final Vector position = new Vector(random.nextDouble() * 18 - 9, random.nextDouble() * 18 - 9);
            final Vector velocityVector = new Vector(velocity, 0).rotate(random.nextDouble() + random.nextInt(100));
            final Bullet bullet = new Bullet(mass, radius, position, velocityVector);
            bullets.add(bullet);
        }
    }

    @Override
    public void update(double time) {
        bullets.forEach(bullet -> bullet.update(time));
        for (int i = 0; i < bullets.size(); i++) {
            final Bullet a = bullets.get(i);
            for (int j = i + 1; j < bullets.size(); j++) {
                tryCollide(a, bullets.get(j));

            }
            bounceAgainstWalls(a);
        }
    }

    private void bounceAgainstWalls(Bullet bullet) {
        if (bullet.position.x - bullet.radius <= -10 || bullet.position.x + bullet.radius >= 10) {
            bullet.velocity.x = Math.abs(bullet.velocity.x);
            if (bullet.position.x > 0) {
                bullet.velocity.x = -bullet.velocity.x;
            }
        }
        if (bullet.position.y - bullet.radius <= -10 || bullet.position.y + bullet.radius >= 10) {
            bullet.velocity.y = Math.abs(bullet.velocity.y);
            if (bullet.position.y > 0) {
                bullet.velocity.y = -bullet.velocity.y;
            }
        }
    }

    private void tryCollide(Bullet a, Bullet b) {
        if (a.position.sub(b.position).length() < a.radius + b.radius) {
            collide(a, b);
        }
    }

    private void collide(Bullet a, Bullet b) {
        final Vector collisionVector = a.position.sub(b.position).unitLocal();
        final double v1 = a.velocity.projection(collisionVector);
        final double v2 = b.velocity.projection(collisionVector);
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
        bullets.forEach(bullet -> bullet.draw(g, sizeScale));
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect((int) (-10 * sizeScale), (int) (-10 * sizeScale), (int) (20 * sizeScale), (int) (20 * sizeScale));

        new SimpleGrid(20).draw(g, sizeScale);
    }
}
