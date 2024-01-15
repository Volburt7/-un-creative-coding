package app;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class HelloWorld extends PApplet {

    private final int CHECKS = 20;
    private List<Integer> nextCheckList = new ArrayList<>();

    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        background(45, 45, 70);
        setCheckList();
    }

    private void setCheckList() {
        for (int i = 0; i < CHECKS; i++) {
            nextCheckList.add(getCheck());
        }
    }

    private int getCheck() {
        return (int) random(10, 200);
    }

    @Override
    public void draw() {
        frameRate(60);
        new ArrayList<>(nextCheckList).forEach(check -> {
            if (frameCount % check == 0) {
                noStroke();
                fill(random(0, 100), random(100, 180), random(80, 160));
                float radius = random(12, 30);
                ellipse(random(0, width), random(0, height), radius, radius);

                nextCheckList.remove(check);
                nextCheckList.add(getCheck());
            }
        });
    }


}