package app;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class BouncingBalls extends PApplet {
    private final List<Ball> balls = new ArrayList<>();
    private final int BALL_AMOUNT = 100;

    @Override
    public void settings() {
        size(768, 512);
    }

    @Override
    public void setup() {
        frameRate(60);
        for (int i = 1; i <= BALL_AMOUNT; i++) {
            final PVector startPos = new PVector(random(0, width), random(0, height));
            balls.add(new Ball(startPos, height, width, 2.2f));
        }
    }

    @Override
    public void draw() {
        background(0);
        fill(255);
        stroke(255);
        drawBalls();

        handleMouse();
        handleBallCollision();
    }

    private void handleMouse() {
        if (mousePressed) {
            if (mouseButton == LEFT) {
                balls.forEach(ball -> ball.attract(new PVector(mouseX, mouseY)));
            } else {
                balls.forEach(ball -> ball.repel(new PVector(mouseX, mouseY)));
            }
        } else {
            balls.forEach(Ball::fall);
        }
    }

    private void handleBallCollision() {
        for (Ball ball : balls) {
            final List<Ball> collisions = ball.getCollisions(balls);
            final PVector collDirSum = new PVector();
            for (Ball coll : collisions) {
                final PVector collPos = new PVector(coll.getVPos().x, coll.getVPos().y);
                final PVector collPosNeg = new PVector(-collPos.x, -collPos.y);
                final PVector collDir = collPosNeg.add(ball.getVPos());
                collDir.normalize();
                collDirSum.add(collDir);
            }
            if (!collisions.isEmpty() && collDirSum.x == 0 && collDirSum.y == 0) {
                collDirSum.add(new PVector(random(0, 1), random(0, 1)));
            }
            collDirSum.mult(collisions.size());
            ball.getVPos().add(collDirSum);
        }
    }

    private void drawBalls() {
        balls.forEach(ball -> ellipse(ball.getVPos().x, ball.getVPos().y, ball.getDiameter(), ball.getDiameter()));
    }
}