package app;

import processing.core.PApplet;

public class RotatePyramid extends PApplet {

    public void settings() {
        size(1024, 512);
    }

    @Override
    public void setup() {
        background(255);
        frameRate(60);

        noFill();
        stroke(0);
        strokeWeight(0f);

    }

    @Override
    public void draw() {
        background(255);

        final float count = map(mouseX, 0, width, 1, 100);
        final float rotation = map(mouseY, 0, height, 0, 0.05f);
        final float widthChange = width / count;
        final float heightChange = height / count;
        for (int i = 1; i <= count; i++) {
            pushMatrix();
            final float xOffset = -i * widthChange / 2;
            final float yOffset = -i * heightChange / 2;
            translate((float) width / 2, (float) height / 2);
            rotate(rotation * (count - i) * 2);
            rect(xOffset, yOffset, widthChange * i, heightChange * i);
            popMatrix();
        }
    }
}