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
        final PShape gearShape = createShape();

        gearShape.beginShape();
        for (float rad=0.0f; rad<=TWO_PI; rad+=TWO_PI/180) {
            float xPos = cos(rad) * gear.getRadius();
            float yPos = sin(rad) * gear.getRadius();
            gearShape.vertex(xPos, yPos);
        }

        gearShape.beginContour();
        for (float rad=TWO_PI; rad>0; rad-=TWO_PI/180) {
            float xPos =  cos(rad) * gear.getRadius() / 2;
            float yPos =  sin(rad) * gear.getRadius() / 2;
            gearShape.vertex(xPos, yPos);
        }
        gearShape.endContour();
        gearShape.endShape(CLOSE);
        return gearShape;
    }

    private List<PShape> getTeeth(final Gear gear) {
        final List<PShape> teeth = new ArrayList<>();

        fill(100);
        rectMode(RADIUS);

        final float deltaRadians = TWO_PI / gear.getToothCount();

        for (float radians = gear.getCurrentRotation(); radians <= TWO_PI + gear.getCurrentRotation(); radians+=deltaRadians) {
            final PShape tooth = createShape();
            tooth.beginShape();
            pushMatrix();
            translate(gear.getRadius() * cos(radians), gear.getRadius() * sin(radians));
            rotate(radians);
            rect(0, 0, Gear.TOOTH_SIZE*2.5f, Gear.TOOTH_SIZE);
            popMatrix();
            tooth.endShape();
            teeth.add(tooth);
        }
        return teeth;
    }
}
