package app;
import app.objects.Gear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class GearSimulation extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(GearSimulation.class);

    final DynamicGearHolder dynamicGearHolder = new DynamicGearHolder();
    final List<Gear> gears = new ArrayList<>();
    boolean creationInProgress = false;
    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && !creationInProgress) {
            dynamicGearHolder.initializeDynamicGear(mouseEvent.getX(), mouseEvent.getY());
            creationInProgress = true;
        }
    }

    @Override
    public void mouseDragged(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && creationInProgress) {
            dynamicGearHolder.update(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override
    public void mouseWheel(final MouseEvent mouseEvent) {
        if (creationInProgress) {
            LOG.info("Scrolled {} times.", mouseEvent.getCount());
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && creationInProgress) {
            gears.add(dynamicGearHolder.getGear(mouseEvent.getX(), mouseEvent.getY()));
            creationInProgress = false;
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        LOG.info("Pressed keycode {}", keyEvent.getKeyCode());
        if(keyEvent.getKeyCode() == BACKSPACE && creationInProgress) {
            creationInProgress = false;
        }
    }

    public void setup() {
        frameRate(60);
        gears.clear();
        gears.add(new Gear(400, 300, 400, PI));
    }

    public void draw() {
        background(50);
        fill(100);
        stroke(255);
        gears.forEach(this::drawGear);
        if (creationInProgress) {
            final Gear snapshotGear = dynamicGearHolder.getSnapshotGear();
            drawSnapshotGear(snapshotGear);
        }
    }


    private void drawGear(final Gear gear) {
        pushMatrix();
        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
        popMatrix();
    }
    private void drawSnapshotGear(final Gear snapshotGear) {
        pushMatrix();
        translate(snapshotGear.getPositionX(), snapshotGear.getPositionY());
        shape(getGearShape(snapshotGear));
        popMatrix();
    }

    private PShape getGearShape(final Gear gear) {
        if(gear.getToothCount() < 2) {
            return new PShape();
        }

        final PShape gearShape = createShape();


        gearShape.beginShape();
        drawTeeth(gear, gearShape);

        gearShape.beginContour();
        removeInnerCircle(gear, gearShape);
        gearShape.endContour();

        gearShape.endShape(CLOSE);
        return gearShape;
    }

    private void drawTeeth(final Gear gear, final PShape gearShape) {
        gearShape.noStroke();
        // Logic from https://math.stackexchange.com/questions/225351/equation-of-sine
        for (float rad = 0.0f; rad < TWO_PI; rad += TWO_PI / 720) {
            float xPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * rad)) * cos(rad);
            float yPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * rad)) * sin(rad);
            gearShape.vertex(xPos, yPos);
        }
    }

    private void removeInnerCircle(final Gear gear,final  PShape gearShape) {
        for (float rad=TWO_PI; rad>=0; rad-=TWO_PI/60) {
            float xPos =  cos(rad) * gear.getRadius() / 2;
            float yPos =  sin(rad) * gear.getRadius() / 2;
            gearShape.vertex(xPos, yPos);
        }
    }
}
