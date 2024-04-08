package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FishAgent extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(FishAgent.class);
    private final List<Fish> fishList = new ArrayList<>();

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
        for (int i = 0; i < 100; i++) {
            final float initialAngle = random(TWO_PI);
            fishList.add(Fish.builder()
                    .fishAgent(this)
                    .fishList(fishList)
                    .position(new PVector(random(0, width), random(0, height)))
                    .acceleration(PVector.random2D())
                    .velocity(new PVector(cos(initialAngle), sin(initialAngle)))
                    .width(random(5, 18))
                    .length(random(10, 180))
                    .maxForce(random(0.02f, 0.04f))
                    .maxSpeed(random(1, 3))
                    .build());
        }
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);
        for (Fish f : fishList) {
            f.run();
        }
        PuddleSystem.makeItRain(this);
    }
}
