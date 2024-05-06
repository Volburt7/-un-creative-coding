package app;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FishAgent extends PApplet {
    private final List<Fish> fishList = new ArrayList<>();
    private RainSystem rainSystem;

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);

        rainSystem = new RainSystem(this);

        for (int i = 0; i < 100; i++) {
            final float initialAngle = random(TWO_PI);
            fishList.add(
                    Fish.builder()
                            .fishAgent(this)
                            .fishList(fishList)
                            .position(new PVector(random(0, width), random(0, height)))
                            .acceleration(PVector.random2D())
                            .velocity(new PVector(cos(initialAngle), sin(initialAngle)))
                            .width(random(1.5f, 20))
                            .length(random(5, 220))
                            .maxForce(random(0.02f, 0.04f))
                            .maxSpeed(random(1, 5))
                            .build()
            );
        }
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);
        for (Fish f : fishList) {
            f.run();
        }
        rainSystem.update(this, 2);
    }
}
