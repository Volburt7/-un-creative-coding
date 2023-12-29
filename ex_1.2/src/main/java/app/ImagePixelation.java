package app;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImagePixelation extends PApplet {

    private final int stepsSize = 1;
    private int circlesMax;
    private PGraphics palette;
    private PImage img;

    public void settings() {
        size(1024, 1024);
    }

    @Override
    public void setup() {
        palette = createGraphics(width, height);
        img = loadImage("./jesus.png");
        img.resize(width, height);

        circlesMax = calculateMaxCircleSize();
    }

    private int calculateMaxCircleSize() {
        int sum = 0;
        int curr = 1;
        while (sum < width) {
            sum += curr;
            curr += stepsSize;
        }
        return curr;
    }

    @Override
    public void draw() {
        palette.beginDraw();
        palette.background(0);
        palette.noStroke();
        int circleSize = circlesMax;
        for (int left = 0; left <= width; left += circleSize) {
            int centerX = left + (circleSize / 2);
            for (int top = 0; top <= height; top += circleSize) {
                int centerY = top + (circleSize / 2);
                int pixel = img.get(centerX, centerY);
                float r = red(pixel);
                float g = green(pixel);
                float b = blue(pixel);
                float a = alpha(pixel);
                palette.fill(r, g, b, a);
                palette.ellipse(centerX, centerY, circleSize, circleSize);
            }
            if (circleSize > stepsSize) {
                circleSize -= stepsSize;
            }
        }
        palette.endDraw();
        background(palette);
    }
}