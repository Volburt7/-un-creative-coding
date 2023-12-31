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

        final List<PShape> cogs = getTeeth(gear);
        final PShape outerCircle = getOuterCircle(gear.getRadius());

        gearShape.beginShape();
        cogs.forEach(gearShape::addChild);
        gearShape.addChild(outerCircle);
        gearShape.endShape(CLOSE);

        return gearShape;
    }

    private PShape getOuterCircle(final float outerRadius) {
        final PShape outerCircle = createShape();

        ellipseMode(RADIUS);
        fill(100);

        outerCircle.beginShape();
        ellipse(0, 0, outerRadius, outerRadius);
        outerCircle.beginContour();
        fill(g.backgroundColor);
        ellipse(0, 0, outerRadius/2, outerRadius/2);
        outerCircle.endContour();
        outerCircle.endShape(CLOSE);
        return outerCircle;
    }

    private List<PShape> getTeeth(final Gear gear) {
        final List<PShape> teeth = new ArrayList<>();
        final float degreePerTeeth = TWO_PI/gear.getToothCount()*2;
        System.out.println(gear.getToothCount());
        fill(100);
        rectMode(RADIUS);

        for (int i = 1; i <= gear.getToothCount(); i++) {
            final PShape tooth = createShape();
            tooth.beginShape();
            final float curRotation = degreePerTeeth * i + gear.getCurrentRotation();
            pushMatrix();
            translate(gear.getRadius() * cos(curRotation), gear.getRadius() * sin(curRotation));
            rotate(curRotation);
            rect(0, 0, Gear.TOOTH_SIZE - 2, Gear.TOOTH_SIZE - 2);
            popMatrix();
            tooth.endShape();
            teeth.add(tooth);
        }
        return teeth;
    }
}
