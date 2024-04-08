package app;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersWithShapes extends PApplet {

    private final static int PIXEL_AMOUNT = 10;
    private final static int PIXEL_SIZE = 45;
    private final static int MAX_COLORS = 3;

    public void settings() {
        size(PIXEL_AMOUNT * PIXEL_SIZE, PIXEL_AMOUNT * PIXEL_SIZE);
    }

    @Override
    public void setup() {
        background(0);
        noStroke();
        final List<Float[]> colors = getColors();
        for (int w = 0; w < width / 2; w += PIXEL_SIZE) {
            for (int h = 0; h < height / 2; h += PIXEL_SIZE) {
                if (round(random(0, 1)) == 1) {
                    fill(0);
                } else {
                    final int colorIndex = floor(random(0, MAX_COLORS));
                    // Prevent out-of-bounce
                    if (colorIndex != MAX_COLORS) {
                        final Float[] color = colors.get(colorIndex);
                        fill(color[0], color[1], color[2]);
                    }
                }
                if (round(random(0, 1)) == 1) {
                    drawInvadersCircle(w, h);
                } else {
                    drawInvadersTriangle(w, h);
                }
            }
        }
    }

    private List<Float[]> getColors() {
        final List<Float[]> colors = new ArrayList<>();
        for (int i = 0; i < MAX_COLORS; i++) {
            final Float[] randomColor = {random(50, 255), random(50, 255), random(50, 255)};
            colors.add(randomColor);
        }
        return colors;
    }

    private void drawInvadersCircle(final int w, final int h) {
        float randomWidthDiv = random(1, 5);
        float randomHeightDiv = random(1, 5);
        ellipse(w + PIXEL_SIZE / 2, h + PIXEL_SIZE / 2, PIXEL_SIZE / randomWidthDiv, PIXEL_SIZE / randomHeightDiv);
        ellipse(width - w - PIXEL_SIZE + PIXEL_SIZE / 2, height - h - PIXEL_SIZE + PIXEL_SIZE / 2, PIXEL_SIZE / randomWidthDiv, PIXEL_SIZE / randomHeightDiv);
        ellipse(w + PIXEL_SIZE / 2, height - h - PIXEL_SIZE + PIXEL_SIZE / 2, PIXEL_SIZE / randomWidthDiv, PIXEL_SIZE / randomHeightDiv);
        ellipse(width - w - PIXEL_SIZE + PIXEL_SIZE / 2, h + PIXEL_SIZE / 2, PIXEL_SIZE / randomWidthDiv, PIXEL_SIZE / randomHeightDiv);
    }

    private void drawInvadersTriangle(final int w, final int h) {
        int p1X = round(random(w, w + PIXEL_SIZE));
        int p1Y = h;
        int p2X = round(random(w, w + PIXEL_SIZE));
        int p2Y = h + PIXEL_SIZE;
        int p3X = w;
        int p3Y = round(random(h, h + PIXEL_SIZE));
        int p4X = w + PIXEL_SIZE;
        int p4Y = round(random(h, h + PIXEL_SIZE));
        switch (floor(random(0, 4))) {
            case 0:
                p1X = p4X;
                p1Y = p4Y;
                break;
            case 1:
                p2X = p4X;
                p2Y = p4Y;
                break;
            case 2:
                p3X = p4X;
                p3Y = p4Y;
                break;
            default:
                break;
        }
        triangle(p1X, p1Y, p2X, p2Y, p3X, p3Y);
        triangle(width - p1X, height - p1Y, width - p2X, height - p2Y, width - p3X, height - p3Y);
        triangle(p1X, height - p1Y, p2X, height - p2Y, p3X, height - p3Y);
        triangle(width - p1X, p1Y, width - p2X, p2Y, width - p3X, p3Y);
    }

    @Override
    public void draw() {
        frameRate(60);
    }
}