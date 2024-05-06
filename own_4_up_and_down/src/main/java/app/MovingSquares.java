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
    private static final int SQUARE_SIZE = 64;
    private static final int IT_STEP_SIZE = 8;

    private final int bgColor = color(145, 39, 27);
    private final int darkColor = color(0);
    private final int yellowColor = color(200, 135, 30);
    private List<Square> squares = new ArrayList<>();

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    @Override
    public void setup() {
        frameRate(60);
        this.squares = createSquares();
    }

    private List<Square> createSquares() {
        final int max = SIZE / SQUARE_SIZE;
        final List<Square> squareList = new ArrayList<>();
        // Iteration with 2 for loops, so we can skip row/col 1 and last
        for (int i = 0; i < max; i++) {
            if (i == 0 || i == max - 1) {
                continue;
            }
            for (int j = 0; j < max; j++) {
                if (j == 0 || j == max - 1) {
                    continue;
                }
                squareList.add(createSquare(i, j));
            }
        }
        return squareList;
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
        for (int i = 0; i < SQUARE_SIZE; i += IT_STEP_SIZE) {
            for (int j = 0; j < SQUARE_SIZE; j += IT_STEP_SIZE) {
                int color = darkColor;
                if (random(0, 1) > 0.5f) {
                    color = bgColor;
                }
                for (int innerI = i; innerI < i + IT_STEP_SIZE; innerI++) {
                    for (int innerJ = j; innerJ < j + IT_STEP_SIZE; innerJ++) {
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
        squares.forEach(this::drawBordersBetween);
        squares.forEach(this::updatePixelPositions);
        squares.forEach(this::colorizeTop);
    }

    private void drawSquare(final Square square) {
        int[][] squarePixelsArray = getPixelsSwapColAndRow(square);
        for (int i = 0; i < SQUARE_SIZE; i++) {
            int[] squarePixels = squarePixelsArray[i];
            for (int j = 0; j < SQUARE_SIZE; j++) {
                int pixelColor = squarePixels[j];
                pushMatrix();
                stroke(pixelColor);
                strokeWeight(1f);
                point(square.getPosition().x + i, square.getPosition().y + j);
                popMatrix();
            }
        }
    }

    private void drawBordersBetween(final Square square) {
        pushMatrix();
        fill(0, 0, 0, 0);
        stroke(bgColor, 255);
        strokeWeight(5f);
        rect(square.getPosition().x, square.getPosition().y, SQUARE_SIZE, SQUARE_SIZE);
        popMatrix();
    }

    private int[][] getPixelsSwapColAndRow(final Square square) {
        int[][] squarePixelsArray = square.getPixels().clone();
        int[][] swapped = new int[SQUARE_SIZE][SQUARE_SIZE];
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int j = 0; j < SQUARE_SIZE; j++) {
                swapped[j][i] = squarePixelsArray[i][j];
            }
        }
        return swapped;
    }

    private void updatePixelPositions(final Square square) {
        int[][] squarePixelsArray = square.getPixels().clone();
        if (square.isGoUp()) {
            for (int i = 0; i < SQUARE_SIZE; i++) {
                final int[] line = square.getPixels()[i];
                if (i == 0) {
                    squarePixelsArray[SQUARE_SIZE - 1] = line;
                } else {
                    squarePixelsArray[i - 1] = line;
                }
            }
        } else {
            for (int i = 0; i < SQUARE_SIZE; i++) {
                final int[] line = square.getPixels()[i];
                if (i == SQUARE_SIZE - 1) {
                    squarePixelsArray[0] = line;
                } else {
                    squarePixelsArray[i + 1] = line;
                }
            }
        }
        square.setPixels(squarePixelsArray);
    }

    private void colorizeTop(final Square square) {
        resetColorizedTop(square);
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int j = 0; j < SQUARE_SIZE; j++) {
                if (i == 0) {
                    if (square.getPixels()[i][j] == darkColor) {
                        square.getPixels()[i][j] = yellowColor;
                    }
                } else if (square.getPixels()[i-1][j] == bgColor && square.getPixels()[i][j] == darkColor) {
                    square.getPixels()[i][j] = yellowColor;
                }
            }
        }
    }

    private void resetColorizedTop(final Square square) {
        for (int i = 0; i < SQUARE_SIZE; i++) {
            for (int j = 0; j < SQUARE_SIZE; j++) {
                if (square.getPixels()[i][j] == yellowColor) {
                    square.getPixels()[i][j] = darkColor;
                }
            }
        }
    }
}
