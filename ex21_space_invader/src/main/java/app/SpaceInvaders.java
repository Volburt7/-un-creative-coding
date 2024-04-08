package app;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvaders extends PApplet {

    private final static int PIXEL_AMOUNT = 9;
    private final static int PIXEL_SIZE = 45;
    private final static int MAX_COLORS = 3;

    public void settings() {
        size(PIXEL_AMOUNT * PIXEL_SIZE, PIXEL_AMOUNT * PIXEL_SIZE);
    }

    @Override
    public void setup() {
        background(255);
        stroke(0);
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
                drawSymetricRects(w, h);
            }
        }
    }

    private List<Float[]> getColors() {
        final List<Float[]> colors = new ArrayList<>();
        for (int i = 0; i < MAX_COLORS; i++) {
            final Float[] randomColor = {random(0, 255), random(0, 255), random(0, 255)};
            colors.add(randomColor);
        }
        return colors;
    }

    private void drawSymetricRects(int w, int h) {
        rect(w, h, PIXEL_SIZE, PIXEL_SIZE);
        rect(width - w - PIXEL_SIZE, height - h - PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
        rect(w, height - h - PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
        rect(width - w - PIXEL_SIZE, h, PIXEL_SIZE, PIXEL_SIZE);
    }

    @Override
    public void draw() {
        frameRate(60);
    }
}