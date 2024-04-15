package app;

import app.enums.ScrollDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.List;

public class Processing extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(Processing.class);
    private final static long MAX_PRIME = 10000000;
    private final static float ZOOM_INC = 0.05f;

    private float zoom = 1f;

    @Override
    public void settings() {
        size(1024, 1024);
        smooth();
    }

    @Override
    public void setup() {
        frameRate(60);
        thread("calculatePrimes");
    }

    public void calculatePrimes() {
        long i = 2;
        while (true) {
            if (isPrimeBruteForce(i)) {
                SharedArrayListSingleton.getInstance().add(i);
                LOG.debug("Found prime {}", i);
            }
            i++;

            if (i > MAX_PRIME) {
                break;
            }

        }
    }

    private boolean isPrimeBruteForce(long number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void draw() {
        background(0);
        if (frameCount % 60 == 1) {
            LOG.info("Zoom is {}", zoom);
        }

        if (mousePressed) {
            if (mouseButton == LEFT) {
                zoom += ZOOM_INC;
            } else if (mouseButton == RIGHT) {
                zoom -= ZOOM_INC;
            }
        }


        final List<Long> primeList = SharedArrayListSingleton.getInstance().getAll();
        for (Long prime : primeList) {
            drawPrime(prime);
        }
    }

    private void drawPrime(final Long prime) {
        pushMatrix();

        translate((float) width / 2, (float) height / 2);

        float x = prime * cos(prime) / zoom;
        float y = prime * sin(prime) / zoom;
        float size = map(prime / zoom, 0, dist(0, 0, (float) width / 2, (float) height / 2), 0.03f, 10);
        noStroke();
        fill(200);
        ellipse(x, y, size, size);
        popMatrix();
    }

    @Override
    public void mouseWheel(final MouseEvent event) {

        final int scrollCount = event.getCount();
        if (scrollCount == -1) {
            updateZoom(ScrollDirection.IN);
        } else if (scrollCount == 1) {
            updateZoom(ScrollDirection.OUT);
        } else {
            LOG.warn("Scroll was undefined with value {}", scrollCount);
        }
    }

    private void updateZoom(final ScrollDirection scrollDirection) {
        if (scrollDirection.equals(ScrollDirection.IN) && zoom > 1) {
            zoom -= ZOOM_INC;
        }
        if (scrollDirection.equals(ScrollDirection.OUT)) {
            zoom += ZOOM_INC;
        }
    }
}