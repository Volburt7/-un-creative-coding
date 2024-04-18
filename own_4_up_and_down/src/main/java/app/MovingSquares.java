package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

/*
 * Idee von https://www.reddit.com/r/oddlysatisfying/comments/19bgdvr/the_chance_of_probability/?share_id=XF-G2q8ryQhpwprF2WaFH&utm_content=1&utm_medium=android_app&utm_name=androidcss&utm_source=share&utm_term=3
 * Partikelsystem von hier kopiert: https://processing.org/examples/smokeparticlesystem.html
 * */
public class MovingSquares extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(MovingSquares.class);

    // size must be dividable by square size
    private static final int SIZE = 512;
    private static final int SQUARE_SIZE = 32;
    private Square[][] squares;

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    @Override
    public void setup() {
        frameRate(60);
        if (SIZE % SQUARE_SIZE != 0) {
            LOG.error("size must be dividable by square size.");
            exit();
        }
        squares = initializeSquares();
    }

    private Square[][] initializeSquares() {
        final int max = SIZE / SQUARE_SIZE;
        final Square[][] squares = new Square[max][max];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                squares[i][j] = createSquare(i, j);
            }
        }
        return squares;
    }

    private Square createSquare(final int i, final int j) {
        return Square.builder()
                .position(new PVector(SQUARE_SIZE * i, SQUARE_SIZE * j))
                .goUp(i % 2 == 0)
                .pixels(generatePixels(j % 8))
                .build();
    }

    private int[][] generatePixels(int addifier) {
        final int[][] squares = new int[SQUARE_SIZE][SQUARE_SIZE];
        for (int i = 0; i<SQUARE_SIZE; i++) {
            for (int j = 0; j<SQUARE_SIZE; j++) {
                // TODO: apply some basic logic to set some random fields which are coherent
            }
        }
        return squares;
    }

    @Override
    public void draw() {
        background(0);
    }
}