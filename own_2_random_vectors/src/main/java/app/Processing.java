package app;

import app.objs.ChainedVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;


public class Processing extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(Processing.class);
    private final static int VECTOR_COUNT = 2;
    private final static float MAX_VELOCITY = 0.3f;
    private final static int MIN_LENGTH = 25;
    private final static int MAX_LENGTH = 100;
    private final long SEED = (long) random(0, 10000000);
    private final List<List<ChainedVector>> vectorLists = new ArrayList<>();

    private PGraphics consistentLayer;
    private PGraphics backgroundLayer;
    private boolean showVectors = false;

    @Override
    public void settings() {
        size(768, 768);
    }

    @Override
    public void setup() {
        frameRate(60);
        System.out.println("Seed: " + SEED);

        /*
            Nice seeds:
            3645L
            5209L
            9354774L

        * */

        randomSeed(SEED);
        consistentLayer = createGraphics(width, height);
        backgroundLayer = createGraphics(width, height);

        final List<ChainedVector> initialVectors = createInitialVectors();
        final List<ChainedVector> invertedVectors = createInvertedVectors(initialVectors);
        final List<List<ChainedVector>> scaledVectorsLists = createScaledVectorsLists(initialVectors);

        vectorLists.add(initialVectors);
        vectorLists.add(invertedVectors);
//        vectorLists.addAll(scaledVectorsLists);
    }

    private List<ChainedVector> createInitialVectors() {
        ChainedVector parent = createRandomBaseVector();
        parent.setX((float) width / 2);
        parent.setY((float) height / 2);
        final List<ChainedVector> initialVectorList = new ArrayList<>();
        initialVectorList.add(parent);
        for (int i = 1; i < VECTOR_COUNT; i++) {
            final ChainedVector vector = createRandomBaseVector();
            vector.setParent(parent);
            initialVectorList.add(vector);
            parent = vector;
        }
        return initialVectorList;
    }

    private ChainedVector createRandomBaseVector() {
        float initialAngle = random(TWO_PI);
        float length = random(MIN_LENGTH, MAX_LENGTH);
        float velocity = random(-MAX_VELOCITY, MAX_VELOCITY);
        return new ChainedVector(length, initialAngle, velocity);
    }

    private List<ChainedVector> createInvertedVectors(final List<ChainedVector> initialVectors) {
        final ChainedVector initialParent = initialVectors.get(0);
        ChainedVector parent = new ChainedVector(initialParent.getLength(), initialParent.getAngle(), -initialParent.getVelocity(), initialParent.getX(), initialParent.getY());
        final List<ChainedVector> invertedVectors = new ArrayList<>();
        invertedVectors.add(parent);
        for (int i = 1; i < VECTOR_COUNT; i++) {
            final ChainedVector initialVector = initialVectors.get(i);
            final ChainedVector vector = new ChainedVector(initialVector.getLength(), initialVector.getAngle(), -initialVector.getVelocity());
            vector.setParent(parent);
            invertedVectors.add(vector);
            parent = vector;
        }
        return invertedVectors;
    }

    private List<List<ChainedVector>> createScaledVectorsLists(final List<ChainedVector> initialVectors) {
        final List<ChainedVector> topLeftVectors = createSmallerVersion(initialVectors, (float) width / 4, (float) height / 4);
        final List<ChainedVector> topRightVectors = createSmallerVersion(initialVectors, (float) width / 4, (float) height - ((float) height / 4));
        final List<ChainedVector> bottomLeftVectors = createSmallerVersion(initialVectors, (float) width - ((float) width / 4), (float) height / 4);
        final List<ChainedVector> bottomRightVectors = createSmallerVersion(initialVectors, (float) width - ((float) width / 4), (float) height - ((float) height / 4));
        return new ArrayList<>(List.of(topLeftVectors, topRightVectors, bottomLeftVectors, bottomRightVectors));
    }

    private List<ChainedVector> createSmallerVersion(final List<ChainedVector> initialVectors, final float xStart, final float yStart) {
        final float scaling = 3f;
        final ChainedVector initialParent = initialVectors.get(0);
        ChainedVector parent = new ChainedVector(initialParent.getLength() / scaling, initialParent.getAngle(), initialParent.getVelocity(), xStart, yStart);
        final List<ChainedVector> scaledVectors = new ArrayList<>();
        scaledVectors.add(parent);
        for (int i = 1; i < VECTOR_COUNT; i++) {
            final ChainedVector initialVector = initialVectors.get(i);
            final ChainedVector vector = new ChainedVector(initialVector.getLength() / scaling, initialVector.getAngle(), initialVector.getVelocity());
            vector.setParent(parent);
            scaledVectors.add(vector);
            parent = vector;
        }
        return scaledVectors;
    }

    @Override
    public void draw() {
        if (frameCount % 60 == 0) {
            saveFrame("images/" + SEED + "_" + frameCount);
        }
        if (mousePressed) {
            if (mouseButton == LEFT) {
                showVectors = true;
            } else if (mouseButton == RIGHT) {
                showVectors = false;
            }
        }

        final List<Float[]> lastVectorPoints = new ArrayList<>();
        for (final List<ChainedVector> vectorList : vectorLists) {
            final ChainedVector entry = vectorList.get(vectorList.size() - 1);
            final Float[] point = {entry.getXTo(), entry.getYTo()};
            lastVectorPoints.add(point);
        }

        updateVectors();
        if (showVectors) {
            drawVectorLines();
        } else {
            hideVectorLines();
        }
        drawLastVectorPoint(lastVectorPoints);

    }

    private void updateVectors() {
        vectorLists.forEach(vectorList -> vectorList.forEach(vector -> {
            vector.rotate();
            vector.update();
        }));
    }

    private void drawVectorLines() {
        backgroundLayer.beginDraw();
        backgroundLayer.background(255);
        backgroundLayer.stroke(255, 0, 0);
        backgroundLayer.noFill();
        for (final List<ChainedVector> vectors : vectorLists) {
            vectors.forEach(vector -> backgroundLayer.line(vector.getX(), vector.getY(), vector.getXTo(), vector.getYTo()));
        }
        backgroundLayer.endDraw();
        image(backgroundLayer, 0, 0);
    }

    private void hideVectorLines() {
        backgroundLayer.beginDraw();
        backgroundLayer.background(255);
        backgroundLayer.endDraw();
        image(backgroundLayer, 0, 0);
    }

    private void drawLastVectorPoint(final List<Float[]> lastPoints) {
        consistentLayer.beginDraw();
        consistentLayer.stroke(0);
        consistentLayer.strokeWeight(2);
        consistentLayer.fill(0);
        for (int i = 0; i < vectorLists.size(); i++) {
            final List<ChainedVector> vectorList = vectorLists.get(i);
            final ChainedVector lastVector = vectorList.get(vectorList.size() - 1);
            consistentLayer.line(lastPoints.get(i)[0], lastPoints.get(i)[1], lastVector.getXTo(), lastVector.getYTo());
        }
        consistentLayer.endDraw();
        image(consistentLayer, 0, 0);
    }
}