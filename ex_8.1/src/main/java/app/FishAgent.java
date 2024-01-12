package app;

import app.obj.Puddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FishAgent extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(FishAgent.class);
    private final List<Fish> fishList = new ArrayList<>();
    private final PuddleManager puddleManager = new PuddleManager(this);

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
        for (int i=0; i<=30; i++) {
            final float initialAngle = random(TWO_PI);
            fishList.add(Fish.builder()
                    .fishAgent(this)
                    .fishList(fishList)
                    .position(new PVector(random(0, width), random(0, height)))
                    .acceleration(PVector.random2D())
                    .velocity(new PVector(cos(initialAngle), sin(initialAngle)))
                    .width(random(10, 15))
                    .height(random(40, 200))
                    .maxForce(random(0.02f, 0.05f))
                    .maxSpeed(random(1, 4))
                    .build());
        }
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);
        for (Fish f : fishList) {
            f.run();
        }
        updatePuddles();
    }

    private void updatePuddles() {
        for (Puddle puddle : puddleManager.getPuddles()) {
            if (puddle.isActive()) {
                puddleManager.updatePuddle(puddle);
                drawPuddle(puddle);

                if (puddle.shouldRipple()) {
                    puddleManager.addToList(puddleManager.createNextPuddle(puddle));
                }
            } else {
                puddleManager.remove(puddle);
            }
        }
    }

    private void drawPuddle(final Puddle puddle) {
        noFill();
        stroke(0, 200, 250, puddle.getLifeSpan());
        ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
    }
}
