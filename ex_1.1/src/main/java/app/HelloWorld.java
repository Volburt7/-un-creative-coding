package app;

import processing.core.PApplet;

public class HelloWorld extends PApplet {

    private int nextCheck = 5;

    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        background(45, 45, 70);
    }

    @Override
    public void draw() {
        frameRate(60);
        if (frameCount % nextCheck == 0) {
            noStroke();
            fill(random(0, 100), random(100, 180), random(80, 160));
            float radius = random(12, 30);
            ellipse(random(0, width), random(0, height), radius, radius);
            nextCheck = (int) random(5, 30);
        }
    }
}