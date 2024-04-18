package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/*
 * Idee von https://www.reddit.com/r/PixelArt/comments/18y9y9y/
 * Partikelsystem von hier kopiert: https://processing.org/examples/smokeparticlesystem.html
 * */
public class MovingSquares extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(MovingSquares.class);

    // size must be dividable by square size
    private static final int SIZE = 512;
    // If square_size changes generatePixels() also needs an update
    private static final int SQUARE_SIZE = 64;
    private final int bgColor = color(145, 39, 27);
    private List<Square> squares = new ArrayList<>();

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    @Override
    public void setup() {
        frameRate(60);
        initializeSquares();
    }

    private void initializeSquares() {
        final int max = SIZE / SQUARE_SIZE;
        final List<Square> squares = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            if (i == 0 || i == max - 1) {continue;}
            for (int j = 0; j < max; j++) {
                if (j == 0 || j == max - 1) {continue;}
                squares.add(createSquare(i, j));
            }
        }
        this.squares = squares;
    }

    private Square createSquare(final int i, final int j) {
        return Square.builder()
                .position(new PVector(SQUARE_SIZE * i, SQUARE_SIZE * j))
                .goUp(i % 2 == 0)
                .pixels(generatePixels())
                .build();
    }

    private int[][] generatePixels() {
        final int[][] pixels = new int[SQUARE_SIZE][SQUARE_SIZE];
        final int steps = 8;
        final int start = 4;
        final int end = SQUARE_SIZE - start;
        for (int i = start; i < end; i += steps) {
            for (int j = start; j < end; j += steps) {
                int color = color(0);
                if (random(0, 1) > 0.5f) {
                    color = color(bgColor);
                }
                for (int innerI = i; innerI < i + steps; innerI++) {
                    for (int innerJ = j; innerJ < j + steps; innerJ++) {
                        pixels[innerI][innerJ] = color;
                    }
                }
            }
        }
        return pixels;
    }

    @Override
    public void draw() {
        background(bgColor);
        squares.forEach(this::drawSquare);
        squares.forEach(this::updateSquare);
    }

    private void drawSquare(final Square square) {
        int[][] squarePixelsArray = square.getPixels();
        for (int i = 0; i < squarePixelsArray.length; i++) {
            int[] squarePixels = squarePixelsArray[i];
            for (int j = 0; j < squarePixels.length; j++) {
                int pixelColor = squarePixels[j];
                pushMatrix();
                stroke(pixelColor);
                strokeWeight(1f);
                point(square.getPosition().x + i, square.getPosition().y + j);
                popMatrix();
            }
        }
    }

    private void updateSquare(final Square square) {
        int[][] squarePixelsArray = square.getPixels().clone();
        for (int i = 0; i < squarePixelsArray.length; i++) {
            final int[] line = square.getPixels()[i];
            if (square.isGoUp()) {
                if (i == 0) {
                    squarePixelsArray[squarePixelsArray.length - 1] = line;
                } else {
                    squarePixelsArray[i - 1] = line;
                }
            } else {
                if (i == squarePixelsArray.length - 1) {
                    squarePixelsArray[0] = line;
                } else {
                    squarePixelsArray[i + 1] = line;
                }
            }
        }
        square.setPixels(squarePixelsArray);
    }
}
