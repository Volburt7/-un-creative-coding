package app;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class CircleCollision extends PApplet {

    final List<Circle> circleList = new ArrayList<>();

    public void settings() {
        size(512, 1024);
    }

    /*
        Make Circles go big but no touchy!
    * */

    @Override
    public void setup() {
        background(0);
        frameRate(60);
    }

    @Override
    public void draw() {
        background(255);
        increaseCircleSizes();
        drawCircles();
        addNewCircle();
    }

    private void increaseCircleSizes() {
        for (final Circle circle : circleList) {
            if (!circle.isCollisionDetected() && !checkForCollision(circle)) {
                circle.setSize(circle.getSize() + 1f);
            }
        }
    }

    private void drawCircles() {
        fill(0);
        noStroke();
        circleList.forEach(circle -> ellipse(circle.getX(), circle.getY(), circle.getSize(), circle.getSize()));
    }

    private void addNewCircle() {
        final Circle newCircle = new Circle((int) random(0, width), (int) random(0, height));
        if (!newCircle.isCollisionDetected() && !checkForCollision(newCircle)) {
            circleList.add(newCircle);
        }
    }

    private boolean checkForCollision(final Circle checkCircle) {
        for (final Circle circle : circleList) {
            if (circle.equals(checkCircle)) {
                continue;
            }
            if (checkCircle.doesCollideWith(circle)) {
                checkCircle.setCollisionDetected(true);
                return true;
            }
        }
        return false;
    }
}