package app;
import app.obj.Gear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PShape;

import java.util.ArrayList;
import java.util.List;

public class GearSimulation extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(GearSimulation.class);

    final List<Gear> gears = new ArrayList<>();


    float ROTATION = 0f;

    @Override
    public void settings() {
        size(1800, 300);
    }

    public void setup() {
        frameRate(60);
    }

    public void draw() {
        background(50);
        fill(100);
        noStroke();

        drawCogwheels(ROTATION);
        ROTATION += 0.01f;
    }


    private void drawCogwheels(float rotation) {
        pushMatrix();
        translate(150, 150);
        shape(getCogwheelShape(100, 2, rotation));
        popMatrix();

        pushMatrix();
        translate(450, 150);
        shape(getCogwheelShape(100, 3, rotation));
        popMatrix();
        pushMatrix();
        translate(750, 150);
        shape(getCogwheelShape(100, 4, rotation));
        popMatrix();

        pushMatrix();
        translate(1050, 150);
        shape(getCogwheelShape(100, 5, rotation));
        popMatrix();

        pushMatrix();
        translate(1350, 150);
        shape(getCogwheelShape(100, 10, rotation));
        popMatrix();

        pushMatrix();
        translate(1650, 150);
        shape(getCogwheelShape(100, 1, rotation));
        popMatrix();
    }

    private PShape getCogwheelShape(final float outerRadius, final int cogNum, final float rotation) {
        final PShape cogWheelShape = createShape();

        final PShape outerCircle = getOuterCircle(outerRadius);
        final List<PShape> cogs = getCogs(outerRadius, cogNum, rotation);

        cogWheelShape.beginShape();
        cogWheelShape.addChild(outerCircle);
        cogs.forEach(cogWheelShape::addChild);
        cogWheelShape.endShape(CLOSE);

        return cogWheelShape;
    }

    private PShape getOuterCircle(float outerRadius) {
        final PShape outerCircle = createShape();

        ellipseMode(RADIUS);
        fill(100);

        outerCircle.beginShape();
        ellipse(0, 0, outerRadius, outerRadius);
        outerCircle.beginContour();
        fill(g.backgroundColor);
        ellipse(0, 0, outerRadius/3, outerRadius/3);
        outerCircle.endContour();
        outerCircle.endShape(CLOSE);
        return outerCircle;
    }

    private List<PShape> getCogs(float outerRadius, int cogNum, float rotation) {
        final List<PShape> cogs = new ArrayList<>();
        final float cogDegree = TWO_PI / cogNum;

        fill(100);
        rectMode(RADIUS);

        for (int i = 1; i <= cogNum; i++) {
            final PShape cog = createShape();
            cog.beginShape();
            final float curRotation = cogDegree * i + rotation;
            final float x = outerRadius * cos(curRotation);
            final float y = outerRadius * sin(curRotation);
            pushMatrix();
            translate(x, y);
            rotate(curRotation);
            rect(0, 0, outerRadius/5, outerRadius/5);
            popMatrix();
            cog.endShape();
            cogs.add(cog);
        }
        return cogs;
    }
}
