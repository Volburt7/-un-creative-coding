package app;

import processing.core.PApplet;

import java.util.List;

public class MisusedRotation extends PApplet {
    private final int SQUARE_SIZE = 32;
    private final int usedWidth = 1024;
    private final int usedHeight = 1024;
    private MySquare[][] squareParameters;
    private float offset = 1f;
    private float offsetDelta = 0f;

    public void settings() {
        size(usedWidth, usedHeight);
    }

    @Override
    public void setup() {
        background(255);
        frameRate(144);
        squareParameters = new MySquare[usedWidth / SQUARE_SIZE][usedHeight / SQUARE_SIZE];
        for (int w = 0; w < usedWidth / SQUARE_SIZE; w++) {
            float sizeChange = random(0.01f, 0.05f);
            final List<Float> color = List.of(random(0, 255), random(0, 50), random(0, 50));
            for (int h = 0; h < usedHeight / SQUARE_SIZE; h++) {
                squareParameters[w][h] = new MySquare(sizeChange, color);
                sizeChange += random(0.01f, 0.05f);
            }
        }
    }

    @Override
    public void draw() {
        background(255);
        noStroke();
        if (mousePressed) {
            if (mouseButton == LEFT) {
                offsetDelta += 0.000001f;
            } else if (mouseButton == RIGHT) {
                offsetDelta -= 0.000001f;
            } else if (mouseButton == CENTER) {
                offsetDelta = 0;
            }
        }

        for (int w = 32; w < usedWidth - 32; w += SQUARE_SIZE) {
            for (int h = 32; h < usedHeight - 32; h += SQUARE_SIZE) {
                final MySquare square = squareParameters[w / SQUARE_SIZE - 1][h / SQUARE_SIZE - 1];
                final List<Float> color = square.getColor();
                fill(color.get(0), color.get(1), color.get(2));

                final float size = SQUARE_SIZE * square.getSizeScale() * offset * 3;
                rotate(offset);
                ellipse(w, h, size, size);
            }
        }
        offset += offsetDelta;
    }
}