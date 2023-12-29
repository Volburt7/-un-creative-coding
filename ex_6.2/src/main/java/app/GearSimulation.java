package app;
import app.obj.Gear;
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

    final List<Gear> gears = new ArrayList<>();

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            LOG.info("Pressed left on {} {}", mouseEvent.getX(), mouseEvent.getY());
        }
        if (mouseEvent.getButton() == CENTER) {
            // TODO: Delete this
            LOG.info("PRESSED Center... not sure how to use this");
        }
    }

    @Override
    public void mouseDragged(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            LOG.info("Moved left on {} {}", mouseEvent.getX(), mouseEvent.getY());
            // TODO: Show preview here
        }
    }

    @Override
    public void mouseWheel(final MouseEvent mouseEvent) {
        if (true) { // TODO: If init process of new gear
            LOG.info("Scrolled {} times.", mouseEvent.getCount());
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            LOG.info("Released left on {} {}", mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        LOG.info("Pressed keycode {}", keyEvent.getKeyCode());
    }

    public void setup() {
        frameRate(60);
        exampleGearForTesting();
    }

    private void exampleGearForTesting() {
        gears.add(new Gear(100, 100, 2, 0));
        gears.add(new Gear(300, 100, 3, HALF_PI));
        gears.add(new Gear(100, 200, 4, PI));
        gears.add(new Gear(200, 300, 5, 3/TWO_PI));
        gears.add(new Gear(500, 350, 11, 3/TWO_PI));
    }

    public void draw() {
        background(50);
        fill(100);
        stroke(255);
        gears.forEach(this::drawGear);
    }


    private void drawGear(final Gear gear) {
        pushMatrix();
        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
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

    private PShape getOuterCircle(float outerRadius) {
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
        final float degreePerTeeth = TWO_PI / gear.getTeethCount();

        fill(100);
        rectMode(RADIUS);

        for (int i = 1; i <= gear.getTeethCount(); i++) {
            final PShape tooth = createShape();
            tooth.beginShape();
            final float curRotation = degreePerTeeth * i + gear.getCurrentRotation();
            pushMatrix();
            translate(gear.getRadius() * cos(curRotation), gear.getRadius() * sin(curRotation));
            rotate(curRotation);
            rect(0, 0, gear.getToothSize(), gear.getToothSize() - 3);
            popMatrix();
            tooth.endShape();
            teeth.add(tooth);
        }
        return teeth;
    }
}
