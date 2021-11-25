package ch.collisionengine.maths;

import java.util.Objects;

public class Vector {

    public double x;
    public double y;

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector addLocal(Vector other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector scaleLocal(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector sub(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    public Vector subLocal(Vector other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector scale(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector rotateLocal(double rotation) {
        final double cos = Math.cos(rotation);
        final double sin = Math.sin(rotation);
        final double x = this.x * cos - this.y * sin;
        final double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector rotate(double rotation) {
        final double cos = Math.cos(rotation);
        final double sin = Math.sin(rotation);
        return new Vector(x * cos - y * sin, x * sin + y * cos);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y;
    }

    public Vector set(Vector other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector normalizeLocal() {
        return scaleLocal(1 / length());
    }

    public Vector normalize() {
        return scale(1 / length());
    }

    public double cross(Vector other) {
        return this.x * other.y - other.x * this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double projection(Vector unitVector) {
        return dot(unitVector);
    }

    public Vector unit() {
        return scale(1/length());
    }

    public Vector unitLocal() {
        return scaleLocal(1/length());
    }
}
