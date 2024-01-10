package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PImage;


public class AgendSpaghettiCode extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(AgendSpaghettiCode.class);

    private PImage pImageOriginal;

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        pImageOriginal = loadImage("chicken.png");
    }

    @Override
    public void draw() {
        background(50);
        fill(130);
        noStroke();
        image(pImageOriginal, 100, 0);
        for (int i = 15; i >= 6 ; i--) {
            drawImage(i, (i - 6) * 75);
        }
    }

    private void drawImage(final int size, final int y) {
        PImage copy = pImageOriginal.copy();
        copy.resize(size * 4, size * 5);
        image(copy, 0, y);
    }
}
