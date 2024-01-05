package app;

import processing.core.PApplet;

public class Fractal extends PApplet {

    @Override
    public void settings() {
        size(1024, 1024);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {
        background(0);
        int recursionDepth = (int) map(mouseX, 0, width, 1, 6);
        addLayer(0, 0, width, height, width / 2, recursionDepth);
    }

    private void addLayer(final int startX, final int startY, final int limitX, final int limitY, final int inc, final int level) {
        rotate(map(frameCount % 240, 0, 239 * 3, 0, 2 * PI));
        translate(startX + inc, startY + inc);

        for (int w = startX; w < limitX; w += inc) {
            for (int h = startY; h < limitY; h += inc) {
                if (level <= 0) {
                    ellipse(w, h, inc, inc);
                    for (float sX = -1; sX < 2; sX += 2) {
                        for (float sY = -1; sY < 2; sY += 2) {
                            ellipse(w, h, inc, inc);
                        }
                    }
                    return;
                } else {
                    pushMatrix();

                    addLayer(w, h, limitX - w, limitY - h, inc / 2, level - 1);
                    popMatrix();
                }
            }
        }
    }
}