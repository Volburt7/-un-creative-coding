package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;


public class SmokeOnTheWater extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(SmokeOnTheWater.class);


    @Override
    public void settings() {
        size(777, 444);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {
        background(50);
        fill(130);
        noStroke();
    }
}
