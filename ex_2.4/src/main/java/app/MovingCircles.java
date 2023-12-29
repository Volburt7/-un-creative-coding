package app;

import processing.core.PApplet;

public class MovingCircles extends PApplet {

    private final static int SQUARE_SIZE = 32;
    private final static int BIG_CIRCLE_SIZE = SQUARE_SIZE - 1;
    private final static int SMALL_CIRCLE_SIZE = SQUARE_SIZE / 2;
    private Float[][] randoms;

    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        background(255);
        frameRate(60);
        randoms = new Float[width / SQUARE_SIZE][height / SQUARE_SIZE];
        for (int w = 0; w < width / SQUARE_SIZE; w++) {
            for (int h = 0; h < height / SQUARE_SIZE; h++) {
                randoms[w][h] = random(-1, 1);
            }
        }
    }

    @Override
    public void draw() {
        background(255);
        noStroke();
        for (int w = 0; w < width; w += SQUARE_SIZE) {
            for (int h = 0; h < height; h += SQUARE_SIZE) {
                drawBigCircles(w, h);
                drawSmallCircles(w, h);
            }
        }
    }

    private void drawBigCircles(final int w, final int h) {
        fill(0);
        final Float random = randoms[w / SQUARE_SIZE][h / SQUARE_SIZE];
        final float mappedX = map(mouseX, 0, width, -SQUARE_SIZE * 4, SQUARE_SIZE * 4) * random;
        final float mappedY = map(mouseY, 0, height, -SQUARE_SIZE * 4, SQUARE_SIZE * 4) * -random;
        ellipse(mappedX + w + (float) SQUARE_SIZE / 2, mappedY + h + (float) SQUARE_SIZE / 2, BIG_CIRCLE_SIZE, BIG_CIRCLE_SIZE);
    }

    private void drawSmallCircles(final int w, final int h) {
        fill(255);
        ellipse(w + (float) SQUARE_SIZE / 2, h + (float) SQUARE_SIZE / 2, SMALL_CIRCLE_SIZE, SMALL_CIRCLE_SIZE);
    }
}