package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* Idee von https://www.reddit.com/r/oddlysatisfying/comments/19bgdvr/
* Partikelsystem von hier kopiert: https://processing.org/examples/smokeparticlesystem.html
* */
public class BouncingBalls extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(BouncingBalls.class);

    private static final int WINDOW_SIZE = 600;
    private static final float ARENA_RADIUS = WINDOW_SIZE * 0.4f;
    private static final float BALL_RADIUS = ARENA_RADIUS * 0.07f;
    private static final float GRAVITY = 0.2f;
    private final List<ParticleSystem> collisions = new ArrayList<>();

    private final List<Ball> balls = new ArrayList<>();
    private int lastCollisionColor = color(0);

    private final List<MySoundPlayer> soundPlayers = new ArrayList<>();
    private int currentSoundIndex = 0;

    @Override
    public void settings() {
        size(WINDOW_SIZE, WINDOW_SIZE);
    }

    @Override
    public void setup() {
        frameRate(60);
        ellipseMode(RADIUS);
        spawnNewBall();
        initSound();
    }

    private void initSound() {
        for (int i = 1; i <= 24; i++) {
            final String fileName = "piano/key" + nf(i, 2) + ".wav";
            try {
                soundPlayers.add(new MySoundPlayer(fileName));
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                LOG.error("Could not instantiate Soundplayer for file: '{}'", fileName, e);
                exit();
            }
        }
        if (soundPlayers.isEmpty()) {
            LOG.warn("Define some audio files");
        }
    }

    @Override
    public void draw() {
        background(0);

        drawCollisionLightUp();
        drawCollisionParticles();
        drawBorder();
        drawBalls();

        updateBalls();
        balls.forEach(ball -> {
            ball.getVDir().add(0, GRAVITY);
            ball.getVPos().add(ball.getVDir());
        });
    }

    private void updateBalls() {
        final List<Ball> ballsCopy = new ArrayList<>(balls);
        for (final Ball toUpdate : ballsCopy) {
            if (checkBorderCollision(toUpdate)) {
                handleBorderCollision(toUpdate);
                playSound();
            }
            for (final Ball ball : ballsCopy) {
                if (toUpdate.equals(ball)) {
                    continue;
                }
                if (checkCollision(toUpdate, ball)) {
                    handleCollision(toUpdate, ball);
                }
            }
        }
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
        directionToCenter.setMag(ball.getVDir().mag());
        ball.getVDir().set(directionToCenter);
        ball.getVPos().add(ball.getVDir());

        final PVector directionOut = directionToCenter.copy().mult(-1);
        directionOut.setMag(BALL_RADIUS);
        collisions.add(new ParticleSystem(this, directionOut.add(ball.getVPos()), ball.getColor()));

        final float random = random(0, 1);
        if (random > 0.75f) {
            balls.remove(ball);
        } else if (random > 0.5f) {
            spawnNewBall();
        }
    }

    private void spawnNewBall() {
        for (int i = 0; i <= 10; i++) {
            final Ball ball = newBall();
            if (hasNoCollision(ball)) {
                balls.add(ball);
                return;
            }
        }
        LOG.warn("Couldn't spawn new ball due to collisions.");
    }

    private boolean hasNoCollision(final Ball ball) {
        for (Ball b : balls) {
            if (checkCollision(ball, b)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCollision(final Ball b1, final Ball b2) {
        final float distBetween = dist(b1.getVPos().x, b1.getVPos().y, b2.getVPos().x, b2.getVPos().y);
        final float radiusSum = b1.getRadius() + b2.getRadius();
        return distBetween < radiusSum;
    }

    private void handleCollision(final Ball ball, final Ball other) {
        // https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
        final PVector collisionNormalized = PVector.sub(ball.getVPos(), other.getVPos()).normalize();
        final float velocityAlongCollision = PVector.dot(ball.getVDir(), collisionNormalized);
        final PVector newVelDir = PVector.sub(ball.getVDir(), PVector.mult(collisionNormalized, 2 * velocityAlongCollision));
        ball.setVDir(newVelDir);
    }

    private Ball newBall() {
        return Ball.builder()
                .radius(BALL_RADIUS)
                .color(color(random(0, 255), random(0, 255), random(0, 150)))
                .vPos(new PVector(random(width * 0.3f, width * 0.7f), random(height * 0.3f, height * 0.7f)))
                .vDir(new PVector(random(-10, 10), random(-10, 1)))
                .build();
    }

    private void drawBorder() {
        fill(0);
        stroke(255);
        strokeWeight(3f);
        ellipse(width * 0.5f, height * 0.5f, width * 0.4f, height * 0.4f);
    }

    private void drawCollisionLightUp() {
        fill(color(red(lastCollisionColor), green(lastCollisionColor), blue(lastCollisionColor), 155));
        stroke(0);
        strokeWeight(0f);
        ellipse(width * 0.5f, height * 0.5f, width * 0.41f, height * 0.41f);
    }

    private void drawCollisionParticles() {
        collisions.forEach(collision -> {
            collision.applyForceToOuter();
            collision.run();
            for (int i = 0; i < 2; i++) {
                collision.addParticle();
            }

        });
    }

    private void drawBalls() {
        stroke(255);
        strokeWeight(2f);
        balls.forEach(ball -> {
            fill(ball.getColor());
            ellipse(ball.getVPos().x, ball.getVPos().y, ball.getRadius(), ball.getRadius());
        });
    }

    private void playSound() {
        soundPlayers.get(currentSoundIndex).play();

        currentSoundIndex += 1;
        if (soundPlayers.size() <= currentSoundIndex) {
            currentSoundIndex = 0;
        }
    }

    @Override
    public void exit() {
        soundPlayers.forEach(sp -> {
            try {
                sp.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        });
        super.exit();
    }
}