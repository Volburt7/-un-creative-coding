package app;

import static processing.core.PApplet.dist;

public class Circle {
    private int x;
    private int y;
    private float size;
    private boolean collisionDetected;

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 1;
        this.collisionDetected = false;
    }

    public boolean doesCollideWith(final Circle circle) {
        return (dist(this.x, this.y, circle.getX(), circle.getY()) < circle.getSize() / 2 + this.size / 2);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean isCollisionDetected() {
        return collisionDetected;
    }

    public void setCollisionDetected(boolean collisionDetected) {
        this.collisionDetected = collisionDetected;
    }
}
