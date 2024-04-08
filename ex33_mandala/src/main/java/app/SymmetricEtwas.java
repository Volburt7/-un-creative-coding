package app;

import processing.core.PApplet;

public class SymmetricEtwas extends PApplet {

    public void settings() {
        size(1024, 1024);
    }

    @Override
    public void setup() {
        background(0);
        frameRate(144);
    }

    @Override
    public void draw() {
        final int pointSize = 6;
        final int splits = 12;
        final float rotation = 2 * PI / splits;
        final float alpha = map(frameCount % frameRate, 0, frameRate - 1, 100, 200);
        fill(255);

        if (mousePressed) {
            for (int i = 1; i <= splits; i++) {
                pushMatrix();

                translate((float) width / 2, (float) height / 2);
                rotate(i * rotation);

//                stroke(255);
                stroke(255, alpha);
                strokeWeight(pointSize);
                line((float) width / 2 - pmouseX, (float) height / 2 - pmouseY, (float) width / 2 - mouseX, (float) height / 2 - mouseY);

//            noStroke();
//            ellipse((float) width / 2 - pmouseX, (float) height / 2 - pmouseY, pointSize, pointSize);

                popMatrix();
            }
        }
    }
}