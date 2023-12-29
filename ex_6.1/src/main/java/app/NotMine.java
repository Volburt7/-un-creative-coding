package app;

import processing.core.PApplet;

public class NotMine extends PApplet {
    @Override
    public void settings() {
        size(960, 1200);
    }

    public void setup() {
        stroke(0);
        strokeWeight(2);
        noFill();
        colorMode(HSB, 100.0f, 100.0f, 100.0f);
    }

    void drawRose(float amplitude, float n, float d, float offset) {
        float k = n / d;
        randomSeed((int) (k * 10000.0)); // choose a random color unique for each rose
        stroke(random(0.0f, 100.0f), random(0.0f, 80.0f), 100);
        beginShape();
        for (float theta = 0; theta < TWO_PI * d; theta += 0.1f) {
            float r = amplitude * cos(k * theta) + offset;
            float x = r * cos(theta);
            float y = r * sin(theta);
            vertex(x, y);
        }
        endShape(CLOSE);
    }

    public void draw() {
        background(0);
        float cellSize = 120;
        float animation = (1 + sin(0.001f * millis())) * cellSize / 8; // aesthetic choice for animation
        for (int n = 1; n < 8; n++) {
            for (int d = 1; d < 10; d++) {
                pushMatrix();
                translate(n * cellSize, d * cellSize);
                drawRose(cellSize / 3 - animation, n, d, animation); // aesthetic choice of cellSize / 3 - offset
                popMatrix();
            }
        }
        //saveFrame("output-####.png");
    }

}
