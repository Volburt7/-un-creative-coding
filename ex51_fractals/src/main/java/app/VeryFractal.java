package app;

import processing.core.PApplet;

public class VeryFractal extends PApplet {

    // Codebase from gitlab!
    // IMPORTANT: The logic here is not mine
    private int t1x;
    private int t1y;
    private int t2x;
    private int t2y;
    private int t3x;
    private int t3y;

    @Override
    public void settings() {
        size(1024, 1024);
    }

    @Override
    public void setup() {
        frameRate(60);
//        randomSeed(0L);
        smooth();
        noFill();
        stroke(255);
        strokeWeight(3L);

        t1x = (int) random(0, width);
        t1y = (int) random(0, height);
        t2x = (int) random(0, width);
        t2y = (int) random(0, height);
        t3x = (int) random(0, height);
        t3y = (int) random(0, width);
    }

    @Override
    public void draw() {
        background(0);
        translate((float) width / 2, (float) height / 2);
        int recursionDepth = (int) map(mouseX, 0, width, 0, 7);
        doSomething_butBeRecursively(recursionDepth);
    }

    private void doSomething_butBeRecursively(final int layer) {
        if (layer <= 0) {
            drawWhatever();
            return;
        }

        rotate(millis() * 0.0005f);
        scale(0.5f);
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                pushMatrix();
                scale(i, j);
                translate((float) width / 2, (float) height / 2);
                doSomething_butBeRecursively(layer - 1);
                popMatrix();
            }
        }
    }

    private void drawWhatever() {
//        triangle(t1x, t1y, t2x, t2y, t3x, t3y);
        triangle(0, height, width, 0, width, height);
//        ellipse(0, 0, width, height);
    }
}