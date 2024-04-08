package app;

import processing.core.PApplet;

public class QuarterCircles extends PApplet {

    private final static int SQUARE_SIZE = 32;

    public void settings() {
        size(768, 512);
    }

    @Override
    public void setup() {
        background(255);
        stroke(255);
        strokeWeight(1f);
        for (int w = 0; w < width / 2; w += SQUARE_SIZE) {
            for (int h = 0; h < height; h += SQUARE_SIZE) {
                drawQuarters(w, h, true);
            }
        }
        for (int w = width / 2; w < width; w += SQUARE_SIZE) {
            for (int h = 0; h < height; h += SQUARE_SIZE) {
                drawQuarters(w, h, false);
            }
        }
    }

    private void drawQuarters(final int w, final int h, final boolean randomColor) {
        for (int ww = w; ww < w + SQUARE_SIZE; ww += SQUARE_SIZE / 2) {
            for (int hh = h; hh < h + SQUARE_SIZE; hh += SQUARE_SIZE / 2) {
                if (round(random(0, 100)) < 10) {
                    continue;
                }
                if (randomColor) {
                    fill(random(50, 255), random(50, 255), random(50, 255));
                } else {
                    fill(random(190, 255), random(0, 20), random(0, 20));
                }
                switch (floor(random(0, 4))) {
                    case 0:
                        drawQuarter(ww, hh, 2);
                        break;
                    case 1:
                        drawQuarter(ww + (SQUARE_SIZE / 2), hh, 3);
                        break;
                    case 2:
                        drawQuarter(ww, hh + (SQUARE_SIZE / 2), 1);
                        break;
                    case 3:
                        drawQuarter(ww + (SQUARE_SIZE / 2), hh + (SQUARE_SIZE / 2), 4);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void drawQuarter(final int centerX, final int centerY, final int direction) {
        switch (direction) {
            case 1:
                arc(centerX, centerY, (float) SQUARE_SIZE, (float) SQUARE_SIZE, 1.5f * PI, 2 * PI);
                break;
            case 2:
                arc(centerX, centerY, (float) SQUARE_SIZE, (float) SQUARE_SIZE, 0, PI / 2);
                break;
            case 3:
                arc(centerX, centerY, (float) SQUARE_SIZE, (float) SQUARE_SIZE, PI / 2, PI);
                break;
            case 4:
                arc(centerX, centerY, (float) SQUARE_SIZE, (float) SQUARE_SIZE, PI, 1.5f * PI);
                break;
            default:
                break;
        }
    }

    @Override
    public void draw() {
        frameRate(60);
    }
}