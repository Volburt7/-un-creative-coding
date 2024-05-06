package app;

import processing.core.PApplet;

import java.util.List;

public class SpinAndSizeUpSquares extends PApplet {
    private final int MAX_SQUARE_SIZE = 32;
    private final float offsetDelta = 0.01f;
    private MySquare[][] squareParameters;
    private float offset = 1;

    public void settings() {
        size(512, 1024);
    }

    @Override
    public void setup() {
        background(255);
        frameRate(60);
        squareParameters = new MySquare[width / MAX_SQUARE_SIZE][height / MAX_SQUARE_SIZE];
        for (int wIndex = 0; wIndex < width / MAX_SQUARE_SIZE; wIndex++) {
            float rotation = random(-1, 1);
            float sizeChange = random(0.01f, 0.05f);
            final List<Float> color = List.of(random(0, 255), random(0, 255), random(0, 255));
            for (int hIndex = 0; hIndex < height / MAX_SQUARE_SIZE; hIndex++) {
                squareParameters[wIndex][hIndex] = new MySquare(rotation, sizeChange, color);
                if (rotation >= 0) {
                    rotation += random(0.1f, 0.3f);
                } else {
                    rotation -= random(0.1f, 0.3f);
                }
                sizeChange += random(0.01f, 0.05f);
            }
        }
    }

    @Override
    public void draw() {
        background(255);
        stroke(0);
        float stroke = map(offset, 0, 2, 1, 0);
        if (stroke > 2f) {
            stroke = 2f;
        }
        if (stroke < 0) {
            noStroke();
            stroke = 0;
        }
        strokeWeight(stroke);
        if (mousePressed) {
            if (mouseButton == LEFT) {
                offset += offsetDelta;
            } else if (mouseButton == RIGHT) {
                offset -= offsetDelta;
            }
        }

        for (int w = 0; w < width; w += MAX_SQUARE_SIZE) {
            for (int h = 0; h < height; h += MAX_SQUARE_SIZE) {
                final MySquare square = squareParameters[w / MAX_SQUARE_SIZE][h / MAX_SQUARE_SIZE];
                final List<Float> squareColor = square.getColor();
                final float squareSize = MAX_SQUARE_SIZE * square.getSizeScale() * offset;

                pushMatrix();
                float transparency;
                if (offset > 5) {
                    transparency = map(offset, 5, 8, 255, 30);
                    if (transparency < 3) {
                        transparency = 3;
                    }
                } else {
                    transparency = map(offset, 1, 5, 0, 255);
                }
                fill(squareColor.get(0), squareColor.get(1), squareColor.get(2), transparency);
                translate(w + MAX_SQUARE_SIZE, h + MAX_SQUARE_SIZE);
                rotate(square.getRotation() * offset);
                rect(0, 0, squareSize, squareSize);
                popMatrix();
            }
        }
    }
}