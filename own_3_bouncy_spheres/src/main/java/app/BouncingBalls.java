package app;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class BouncingBalls extends PApplet {
    private static final int WINDOW_SIZE = 1024;
    private static final float ARENA_RADIUS = WINDOW_SIZE * 0.4f;
    private static final float BALL_RADIUS = ARENA_RADIUS * 0.08f;

    private final List<Ball> balls = new ArrayList<>();
    private int lastCollisionColor = color(255, 0, 0);

    @Override
    public void settings() {
        size(WINDOW_SIZE, WINDOW_SIZE);
    }

    @Override
    public void setup() {
        frameRate(60);
        ellipseMode(RADIUS);
        addInitialBalls();
    }

    private void addInitialBalls() {
        balls.add(newBall()
                .vPos(new PVector(width * 0.5f, height * 0.5f))
                .build()
        );
        balls.add(newBall()
                .vPos(new PVector(width * 0.5f, height * 0.5f - BALL_RADIUS * 5))
                .build()
        );
    }

    @Override
    public void draw() {
        background(0);

        drawCollisionLightUp();
        drawBorder();
        drawBalls();

        updateBalls();
    }

    private void updateBalls() {
        final List<Ball> balls_cpy = new ArrayList<>(balls);
        for (final Ball check : balls_cpy) {
            if (checkBorderCollision(check)) {
                handleBorderCollision(check);
            }
            for (final Ball ball : balls_cpy) {
                if (check.equals(ball)) {
                    continue;
                }
                if (checkCollision(check, ball)) {
                    handleCollision(check, ball);
                }
            }
        }
        balls.forEach(ball -> ball.getVPos().add(ball.getVDir()));
    }

    private boolean checkBorderCollision(final Ball b) {
        final float distToCenter = dist(width * 0.5f, height * 0.5f, b.getVPos().x, b.getVPos().y);
        final float radiusSum = b.getRadius() + ARENA_RADIUS;
        return distToCenter > (radiusSum - (b.getRadius() * 2));
    }

    private void handleBorderCollision(final Ball ball) {
        lastCollisionColor = ball.getColor();
        final PVector center = new PVector(width * 0.5f, height * 0.5f);
        final PVector directionToCenter = PVector.sub(center, ball.getVPos()).normalize();
        ball.getVDir().set(directionToCenter);
        ball.getVPos().add(ball.getVDir());
        if (random(0, 1) > 0.75f) {
            balls.remove(ball);
        }
        if (random(0, 1) > 0.75f) {
            spawnNewBallInCenter();
        }
    }

    private void spawnNewBallInCenter() {
        balls.add(newBall().build()
        );
    }

    private boolean checkCollision(final Ball b1, final Ball b2) {
        final float distBetween = dist(b1.getVPos().x, b1.getVPos().y, b2.getVPos().x, b2.getVPos().y);
        final float radiusSum = b1.getRadius() + b2.getRadius();
        return distBetween < radiusSum;
    }

    private static void handleCollision(final Ball check, final Ball ball) {
        final PVector directionAway = PVector.sub(check.getVPos(), ball.getVPos()).normalize();
        check.getVDir().set(directionAway);
    }

    private Ball.BallBuilder newBall() {
        return Ball.builder()
                .radius(BALL_RADIUS)
                .color(color(random(0, 255), random(0, 255), random(0, 255)))
                .vPos(new PVector(width * 0.5f, height * 0.5f))
                .vDir(new PVector(random(-2, 2), random(-5, -2)));
    }

    private void drawBorder() {
        fill(0);
        stroke(255);
        strokeWeight(3f);
        ellipse(width * 0.5f, height * 0.5f, width * 0.4f, height * 0.4f);
    }

    private void drawCollisionLightUp() {
        fill(color(red(lastCollisionColor), green(lastCollisionColor), blue(lastCollisionColor), 100));
        stroke(0);
        strokeWeight(0f);
        ellipse(width * 0.5f, height * 0.5f, width * 0.415f, height * 0.415f);
    }

    private void drawBalls() {
        strokeWeight(0f);
        balls.forEach(ball -> {
            fill(ball.getColor());
            ellipse(ball.getVPos().x, ball.getVPos().y, ball.getRadius(), ball.getRadius());
        });
    }
}