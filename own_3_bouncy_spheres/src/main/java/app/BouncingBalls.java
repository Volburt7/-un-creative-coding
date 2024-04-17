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
                .vDir(new PVector(0, 2f))
                .build()
        );
        balls.add(newBall()
                .vPos(new PVector(width * 0.5f, height * 0.5f - BALL_RADIUS * 5))
                .vDir(new PVector(0, 4f))
                .build()
        );
    }

    @Override
    public void draw() {
        background(0);

        updateInnerCircles();

        drawOuterCircle();
        drawInnerCircles();
    }

    private void updateInnerCircles() {
        final List<Ball> balls_cpy = new ArrayList<>(balls);
        for (final Ball check : balls_cpy) {
            if (checkBorderCollision(check)) {

                PVector center = new PVector(width / 2, height / 2);
                PVector directionToCenter = PVector.sub(center, check.getVPos()).normalize();

                check.getVDir().set(directionToCenter);
                if (random(0, 1) > 0.75f) {
                    // 25% to remove ball
                    System.out.println("removing ball");
                    balls.remove(check);
                }
                if (random(0, 1) > 0.75f) {
                    // 25% to spawn new ball
                    System.out.println("spawn new ball");
                    spawnNewBallInApproximateArea(check);
                }
            }
            for (final Ball ball : balls_cpy) {
                if (check.equals(ball)) {
                    continue;
                }
                if (checkCollision(check, ball)) {
                    PVector directionAway = PVector.sub(check.getVPos(), ball.getVPos()).normalize();
                    check.getVDir().set(directionAway);
                }
            }
        }
        balls.forEach(ball -> ball.getVPos().add(ball.getVDir()));
    }

    private void spawnNewBallInApproximateArea(final Ball ball) {
        final PVector pos = new PVector(ball.getVPos().x, ball.getVPos().y);
        final PVector dir = new PVector(ball.getVDir().x, ball.getVPos().y);
        balls.add(newBall()
                .vPos(pos)
                .vDir(dir)
                .build()
        );
    }

    private Ball.BallBuilder newBall() {
        return Ball.builder()
                .radius(BALL_RADIUS)
                .color(color(random(0, 255), random(0, 255), random(0, 255)));
    }

    private boolean checkCollision(final Ball b1, final Ball b2) {
        final float distBetween = dist(b1.getVPos().x, b1.getVPos().y, b2.getVPos().x, b2.getVPos().y);
        final float radiusSum = b1.getRadius() + b2.getRadius();
        return distBetween < radiusSum;
    }

    private boolean checkBorderCollision(final Ball b) {
        final float distToCenter = dist(width * 0.5f, height * 0.5f, b.getVPos().x, b.getVPos().y);
        final float radiusSum = b.getRadius() + ARENA_RADIUS;
        return distToCenter > (radiusSum - (b.getRadius() * 2));
    }


    private void drawOuterCircle() {
        fill(0);
        stroke(255);
        strokeWeight(3f);
        ellipse(width * 0.5f, height * 0.5f, width * 0.4f, height * 0.4f);
    }

    private void drawInnerCircles() {
        strokeWeight(0f);
        balls.forEach(ball -> {
            fill(ball.getColor());
            ellipse(ball.getVPos().x, ball.getVPos().y, ball.getRadius(), ball.getRadius());
        });
    }
}