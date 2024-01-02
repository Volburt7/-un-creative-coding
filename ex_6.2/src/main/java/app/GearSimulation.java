package app;

import app.enums.Direction;
import app.enums.GearCreationState;
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

    final List<Gear> gears = new ArrayList<>();
    final DynamicGearHolder dynamicGearHolder = new DynamicGearHolder();

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
            if (GearCreationState.SIZE.equals(dynamicGearHolder.getGear().getGearCreationState())) {
                dynamicGearHolder.updateRadius(mouseEvent.getX(), mouseEvent.getY());
            } else {
                dynamicGearHolder.updateLocation(mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && creationInProgress) {
            final Gear gearInCreation = dynamicGearHolder.getGear();
            if (gearInCreation.getToothCount() >= 2 && GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
                continueGearCreation();
            }
        }
    }

    @Override
    public void mouseWheel(final MouseEvent mouseEvent) {
        if (creationInProgress) {
            final Gear gearInCreation = dynamicGearHolder.getGear();
            if (gearInCreation != null) {
                switch (gearInCreation.getGearCreationState()) {
                    case TYPE -> dynamicGearHolder.updateType();
                    case DIRECTION -> dynamicGearHolder.updateDirection();
                    case SPEED -> dynamicGearHolder.updateSpeed(mouseEvent.getCount());
                }
            }
        }
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && creationInProgress) {
            if(!GearCreationState.SIZE.equals(dynamicGearHolder.getGear().getGearCreationState())) {
                continueGearCreation();
            }
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        LOG.info("Pressed keycode {}", keyEvent.getKeyCode());
        if (creationInProgress) {
            switch (keyEvent.getKeyCode()) {
                case BACKSPACE -> creationInProgress = false;
                case ENTER -> continueGearCreation();
            }
        }
    }

    private void continueGearCreation() {
        dynamicGearHolder.setToNextStage();

        final Gear gearInCreation = dynamicGearHolder.getGear();
        if(GearCreationState.CREATED.equals(gearInCreation.getGearCreationState())) {
            creationInProgress = false;
            gears.add(gearInCreation);
            // TODO: Implement List logic here to make it linked or somewhat
        }
    }


    @Override
    public void setup() {
        frameRate(MyConsts.FPS);
        gears.clear();
        gears.add(new Gear(400, 300, 200, 0, GearCreationState.CREATED));
    }

    @Override
    public void draw() {
        background(50);
        fill(100);
        noStroke();

        gears.forEach(this::drawGear);

        if (creationInProgress) {
            drawGear(dynamicGearHolder.getGear());
        }
    }

    private void drawGear(final Gear gear) {
        pushMatrix();
        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
        drawIntoInnerCircle(gear);
        popMatrix();
        if(gear.isMotor()) {
            final float scaledRPM = (TWO_PI / MyConsts.FPS / 60) * gear.getRpm();
            if (Direction.LEFT.equals(gear.getDirection())) {
                gear.setRadiansOffset((gear.getRadiansOffset() + scaledRPM) % TWO_PI);
            } else {
                gear.setRadiansOffset((gear.getRadiansOffset() - scaledRPM) % TWO_PI);
            }
        }

    }

    private PShape getGearShape(final Gear gear) {
        final PShape gearShape = createShape();

        if (GearCreationState.CREATED.equals(gear.getGearCreationState())) {
            gearShape.beginShape();
            gearShape.noStroke();
        } else {
            gearShape.beginShape(POINTS);
            gearShape.stroke(255);
        }

        drawTeeth(gear, gearShape);
        gearShape.beginContour();
        removeInnerCircle(gear, gearShape);
        gearShape.endContour();

        gearShape.endShape(CLOSE);

        return gearShape;
    }

    private void drawTeeth(final Gear gear, final PShape gearShape) {
        // Logic from https://math.stackexchange.com/questions/225351/equation-of-sine
        final float deltaSinDots = TWO_PI / (gear.getToothCount() * Gear.TOOTH_SIZE);
        for (float rad = 0.0f; rad < TWO_PI; rad += deltaSinDots) {
            float xPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * cos(rad);
            float yPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * sin(rad);
            gearShape.vertex(xPos, yPos);
        }
    }

    private void removeInnerCircle(final Gear gear, final PShape gearShape) {
        for (float rad = TWO_PI; rad >= 0; rad -= TWO_PI / 60) {
            float xPos = cos(rad) * gear.getRadius() / 2;
            float yPos = sin(rad) * gear.getRadius() / 2;
            gearShape.vertex(xPos, yPos);
        }
    }

    private void drawIntoInnerCircle(final Gear gear) {
        final float fontSize = gear.getRadius() / 3;
        if (fontSize < 10) {
            textSize(10);
        } else {
            textSize(fontSize);
        }

        textAlign(CENTER, CENTER);

        final float yTextOffset = -gear.getRadius() / 20;
        switch (gear.getGearCreationState()) {
            case SIZE -> text(gear.getToothCount(), 0, yTextOffset);
            case TYPE -> {
                if (gear.isMotor()) {
                    text("M", 0, yTextOffset);
                } else {
                    text("G", 0, yTextOffset);
                }
            }
            case DIRECTION -> {
                // TODO: img
                if (Direction.LEFT.equals(gear.getDirection())) {
                    text("L", 0, 0);
                } else {
                    text("R", 0, 0);
                }
            }
            case SPEED, CREATED -> text(round(gear.getRpm()), 0, yTextOffset);
            default -> LOG.info("Maybe something got wrong. Type was '{}'", gear.getGearCreationState());
        }
    }
}
