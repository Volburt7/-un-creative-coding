package app;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.dist;


public class Ball {
    private final PVector vPos;
    private final PVector vDir;
    final private float GRAVITY_ACCELERATION = 9.81f;

    private float accel;
    private int diameter;
    private int bottom;
    private int right;

    public Ball(final PVector vStart, final int bottom, final int right, final float accel) {
        this.vPos = vStart;
        this.vDir = new PVector(0, 0);
        this.bottom = bottom;
        this.right = right;
        this.diameter = 15;
        this.accel = accel;
    }

    public void attract(final PVector vAttractTo) {
        final PVector vDestination = vAttractTo.sub(vPos);
        this.vDir.add(vDestination.normalize().mult(accel));
        this.updatePos();
    }

    public void repel(final PVector vRepelFrom) {
        final PVector vAttractTo = vRepelFrom.sub(vPos);
        this.vDir.sub(vAttractTo.normalize().mult(accel));
        this.updatePos();
    }

    public void fall() {
        if (this.vPos.y < this.bottom - this.diameter) {
            final PVector vDestination = new PVector(this.vPos.x, this.bottom - this.diameter).sub(vPos);
            this.vDir.add(vDestination.normalize());
            this.vDir.normalize().mult(GRAVITY_ACCELERATION);
            this.updatePos();
        } else {
            this.vDir.setMag(0);
        }
    }

    public List<Ball> getCollisions(final List<Ball> balls) {
        final List<Ball> collidingBalls = new ArrayList<>();
        for (Ball ball : balls) {
            if (this.equals(ball)) {
                continue;
            }
            if (doesCollideWith(ball)) {
                collidingBalls.add(ball);
            }
        }
        return collidingBalls;
    }

    private void updatePos() {
        this.vPos.add(this.vDir);
    }

    private boolean doesCollideWith(final Ball ball) {
        final float dist = dist(vPos.x, vPos.y, ball.getVPos().x, ball.getVPos().y);
        final int radiusSum = ball.getDiameter() / 2 + this.diameter / 2;
        return dist < radiusSum;
    }

    private boolean checkBorderCollision() {
        final boolean withinHorizontalBounds = (vPos.x >= 0) && (vPos.x <= right);
        final boolean withinVerticalBounds = (vPos.y >= 0) && (vPos.y <= bottom);
        return withinHorizontalBounds && withinVerticalBounds;
    }

    public PVector getVPos() {
        return vPos;
    }

    public PVector getVDir() {
        return vDir;
    }

    public float getGRAVITY_ACCELERATION() {
        return GRAVITY_ACCELERATION;
    }

    public float getAccel() {
        return accel;
    }

    public void setAccel(float accel) {
        this.accel = accel;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
