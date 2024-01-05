package app;

import app.fourier.FourierTransform;
import app.objs.ChainedVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

// inspiration from https://andymac-2.github.io/fourier-polygon/writeup/
public class Processing extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(Processing.class);
    private final int FRAMERATEE = 60;
    private List<ChainedVector> vectors = new ArrayList<>();
    private FourierTransform fourierTransform;
    private PGraphics consistentLayer;
    private PGraphics backgroundLayer;
    private boolean showVectors = false;

    @Override
    public void settings() {
        size(1024, 1024);
    }

    @Override
    public void setup() {
        frameRate(FRAMERATEE);
        consistentLayer = createGraphics(width, height);
        backgroundLayer = createGraphics(width, height);
//        createInitialFourierTransform();
        createInitialVectorsRandom();
    }

    void createInitialFourierTransform() {
        double[][] rectangleCords = {{50, 0}, {50, 50}, {0, 50}, {-50, 50}, {-50, 0}, {-50, -50}, {0, -50}, {50, -50}};
        fourierTransform = new FourierTransform(rectangleCords, (double) 1 / FRAMERATEE);
    }

    void createInitialVectorsRandom() {
        final int vectorCount = 2;

        ChainedVector parent = createRandomBaseVector();
        parent.setX((float) width / 2);
        parent.setY((float) height / 2);
        vectors.add(parent);
        for (int i = 1; i < vectorCount; i++) {
            final ChainedVector vector = createRandomBaseVector();
            vector.setParent(parent);
            vectors.add(vector);
            parent = vector;
        }
    }

    private ChainedVector createRandomBaseVector() {
        float initialAngle = random(TWO_PI);
        float length = random(10, 50);
        float velocity = random(-0.05f, 0.05f);
        return new ChainedVector(length, initialAngle, velocity);
    }

    @Override
    public void draw() {

        if (mousePressed) {
            if (mouseButton == LEFT) {
                showVectors = true;
            } else if (mouseButton == RIGHT) {
                showVectors = false;
            }
        }

//        if (vectors.isEmpty()) {
//            LOG.warn("Vectors is empty.");
//            return;
//        }

        if (fourierTransform != null) {
            int n = (int) (frameCount % frameRate);
            ChainedVector lastVector;
            if (vectors.isEmpty()) {
                lastVector = fourierTransform.getLastTransformers();
            } else {
                lastVector = vectors.get(vectors.size() - 1);
            }
            final float toX = lastVector.getXTo();
            final float toY = lastVector.getYTo();
            vectors = fourierTransform.calculateVectors(n);
            hideVectorLines();

            drawFourier(toX, toY);

            if (n == 0) {
                resetConsistentLayer();
            }
        } else {
            final ChainedVector lastVector = vectors.get(vectors.size() - 1);
            final float toX = lastVector.getXTo();
            final float toY = lastVector.getYTo();
            updateVectors();
            if (showVectors) {
                drawVectorLines();
            } else {
                hideVectorLines();
            }
            drawLastVectorPoint(toX, toY);
        }

    }

    void updateVectors() {
        vectors.forEach(vector -> {
            vector.rotate();
            vector.update();
        });
    }

    private void drawVectorLines() {
        backgroundLayer.beginDraw();
        backgroundLayer.background(255);
        backgroundLayer.stroke(255, 0, 0);
        backgroundLayer.noFill();
        vectors.forEach(vector -> backgroundLayer.line(vector.getX(), vector.getY(), vector.getXTo(), vector.getYTo()));
        backgroundLayer.endDraw();
        image(backgroundLayer, 0, 0);
    }

    private void hideVectorLines() {
        backgroundLayer.beginDraw();
        backgroundLayer.background(255);
        backgroundLayer.endDraw();
        image(backgroundLayer, 0, 0);
    }

    private void drawLastVectorPoint(final float lastX, final float lastY) {
        consistentLayer.beginDraw();
        consistentLayer.stroke(0);
        consistentLayer.strokeWeight(2);
        consistentLayer.fill(0);
        final ChainedVector lastVector = vectors.get(vectors.size() - 1);
        consistentLayer.line(lastX, lastY, lastVector.getXTo(), lastVector.getYTo());
        consistentLayer.endDraw();
        image(consistentLayer, 0, 0);
    }

    private void drawFourier(final float lastX, final float lastY) {
        consistentLayer.beginDraw();
        consistentLayer.stroke(0);
        consistentLayer.strokeWeight(2);
        consistentLayer.fill(0);
        final ChainedVector lastVector = vectors.get(vectors.size() - 1);
        consistentLayer.line(lastX, lastY, lastVector.getXTo(), lastVector.getYTo());
        consistentLayer.endDraw();
        image(consistentLayer, 0, 0);
//        LOG.info("\nX '{}' to '{}' \nY '{}' to '{}'", lastX, lastVector.getXTo(), lastY, lastVector.getYTo());
    }

    private void resetConsistentLayer() {
        consistentLayer.beginDraw();
        consistentLayer.background(255);
        consistentLayer.endDraw();
        image(consistentLayer, 0, 0);
    }
}