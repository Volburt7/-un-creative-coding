package app.objs;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ChainedVector {
    private float x;
    private float y;
    private float length;
    private float angle;
    private float velocity;
    private ChainedVector parent;

    public ChainedVector(final float length, final float angle, final float velocity) {
        this.length = length;
        this.angle = angle;
        this.velocity = velocity;
    }

    public ChainedVector() {
    }

    public ChainedVector(final double xFrom, final double yFrom, final double xTo, final double yTo) {
        this.x = (float) xTo;
        this.y = (float) yTo;
        this.parent = new ChainedVector();
        this.parent.x = (float) xFrom;
        this.parent.y = (float) yFrom;
    }

    public ChainedVector(final float xTo, final float yTo) {
        this.x = xTo;
        this.y = yTo;
    }

    public void rotate() {
        this.angle += this.velocity;
    }

    public void update() {
        if (parent != null) {
            this.x = this.parent.getX() + this.parent.getLength() * (float) cos(this.parent.getAngle());
            this.y = this.parent.getY() + this.parent.getLength() * (float) sin(this.parent.getAngle());
        }
    }

    public float getXTo() {
        return this.x + this.length * (float) cos(this.angle);
    }

    public float getYTo() {
        return this.y + this.length * (float) sin(this.angle);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public ChainedVector getParent() {
        return parent;
    }

    public void setParent(final ChainedVector parent) {
        this.x = parent.getXTo();
        this.y = parent.getYTo();
        this.parent = parent;
    }
}
