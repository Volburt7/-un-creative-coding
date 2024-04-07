package app;

import app.enums.Direction;
import app.enums.GearCreationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class GearSimulation extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(GearSimulation.class);
    private final boolean HIDDEN_MODE = true;
    private final List<Gear> gears = new ArrayList<>();

    private Gear gearInCreation;

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && gearInCreation == null) {
            gearInCreation = Gear.builder()
                    .positionX(mouseEvent.getX())
                    .positionY(mouseEvent.getY())
                    .gearCreationState(GearCreationState.SIZE)
                    .build();
        }
    }

    @Override
    public void mouseDragged(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && gearInCreation != null) {
            if (GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
                gearInCreation.updateSize(mouseEvent.getX(), mouseEvent.getY());
            } else {
                gearInCreation.updateLocation(mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT &&
                gearInCreation != null &&
                gearInCreation.getToothCount() >= 3 &&
                GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
            continueGearCreation();
        }
    }

    @Override
    public void mouseWheel(final MouseEvent mouseEvent) {
        if (gearInCreation != null) {
            switch (gearInCreation.getGearCreationState()) {
                case TYPE -> toggleType();
                case DIRECTION -> toggleDirection();
                case SPEED -> updateSpeed(mouseEvent.getCount());
                default -> LOG.info("Scrolling won't effect state '{}'", gearInCreation.getGearCreationState());
            }
        }
    }

    private void toggleType() {
        gearInCreation.setMotor(!gearInCreation.isMotor());
        if (gearInCreation.isMotor()) {
            gearInCreation.setDirection(Direction.LEFT);
            gearInCreation.setRpm(30);
        } else {
            gearInCreation.setDirection(null);
            gearInCreation.setRpm(0);
        }
    }

    private void toggleDirection() {
        if (Direction.LEFT.equals(gearInCreation.getDirection())) {
            gearInCreation.setDirection(Direction.RIGHT);
        } else {
            gearInCreation.setDirection(Direction.LEFT);
        }
    }

    private void updateSpeed(final int speedChange) {
        final float newRPM = gearInCreation.getRpm() + speedChange;
        if (newRPM >= 1) {
            gearInCreation.setRpm(newRPM);
        }
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            if (gearInCreation != null && !GearCreationState.SIZE.equals(gearInCreation.getGearCreationState()))
                continueGearCreation();
        } else if (mouseEvent.getButton() == RIGHT) {
            gearInCreation = null;
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case BACKSPACE -> gearInCreation = null;
            case ENTER, 32 -> { // 32 = SPACEBAR
                if (gearInCreation != null) {
                    continueGearCreation();
                }
            }
            case 82, 116 -> updateGearList(); // Update on "R" and "F5"
            default -> LOG.info("Button {} got no use.", keyEvent.getKeyCode());
        }
    }

    private void continueGearCreation() {
        final GearCreationState nextStage = GearUtils.determineNextStage(gearInCreation);
        gearInCreation.setGearCreationState(nextStage);
        if (GearCreationState.CREATED.equals(nextStage)) {
            gears.add(gearInCreation);
            gearInCreation = null;
            updateGearList();
        }
    }

    private void updateGearList() {
        final List<Gear> motors = getMotors();
        motors.forEach(motor -> motor.setRadiansOffset(0f)); // Set motor to initial position
        final List<Gear> updatedEntries = new ArrayList<>(motors);
        motors.forEach(motor -> translateGearRotation(motor, updatedEntries));
    }

    private List<Gear> getMotors() {
        return gears.stream().filter(Gear::isMotor).toList();
    }

    private void translateGearRotation(final Gear motor, final List<Gear> updatedEntries) {
        getConnectedGears(motor).forEach(gear -> {
            if (!updatedEntries.contains(gear)) {
                // Updates the gear itself
                applyTranslation(motor, gear);
                updatedEntries.add(gear);

                // Recursive translation
                translateGearRotation(gear, updatedEntries);
            }
        });
    }

    private List<Gear> getConnectedGears(final Gear motor) {
        return gears.stream()
                .filter(gear -> !gear.isMotor())
                .filter(gear -> GearUtils.areConnected(motor, gear))
                .toList();
    }

    private void applyTranslation(final Gear motor, final Gear gear) {
        final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();
        gear.setRpm(motor.getRpm() * translationRatio);
        gear.setDirection(Direction.LEFT.equals(motor.getDirection()) ? Direction.RIGHT : Direction.LEFT);
        // TODO: Set init pos here?
    }

    @Override
    public void setup() {
        frameRate(MyConsts.FPS);
        gears.clear();
        addTestGears();
        updateGearList();
    }

    private void addTestGears() {
        final Gear g1 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(10)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(100)
                .positionY(100)
                .radiansOffset(0f)
                .initialRadiansOffset(0f)
                .build();
        g1.updateSize(250, 250);
        gears.add(g1);

        final Gear g2 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(300)
                .positionY(300)
                .radiansOffset(1.5f)
                .initialRadiansOffset(1.5f)
                .build();
        g2.updateSize(340, 340);
        gears.add(g2);

        final Gear g3 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(380)
                .positionY(100)
                .radiansOffset(1)
                .initialRadiansOffset(1)
                .radiansOffset(1)
                .build();
        g3.updateSize(440, 100);
        gears.add(g3);
    }

    @Override
    public void draw() {
        background(50);
        fill(130);
        noStroke();

        gears.forEach(this::drawGear);
        if (gearInCreation != null && gearInCreation.getToothCount() > 0) {
            drawGear(gearInCreation);
        }

        gears.forEach(this::updateGearRadians);
    }

    private void drawGear(final Gear gear) {
        pushMatrix();

        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
        if (!HIDDEN_MODE) {
            drawIntoInnerCircle(gear);
        }
        drawRadiansOffset(gear);
        popMatrix();
    }

    private void updateGearRadians(final Gear gear) {
//        final float scaledRPM = TWO_PI / MyConsts.FPS / 60 * gear.getRpm();
//        if (Direction.LEFT.equals(gear.getDirection())) {
//            gear.setRadiansOffset((gear.getRadiansOffset() + scaledRPM) % TWO_PI);
//        } else if (Direction.RIGHT.equals(gear.getDirection())) {
//            float newOffset = gear.getRadiansOffset() - scaledRPM;
//            if (newOffset < 0) {
//                gear.setRadiansOffset(TWO_PI - newOffset);
//            } else {
//                gear.setRadiansOffset(newOffset);
//            }
//        }

        // TODO: maybe undo what I do now
        //  I think the % operation here adds some minimal offset change with each rotation...
//// APPROACH 1 fail
//        final float scaledRPM = (TWO_PI / MyConsts.FPS / 60) * gear.getRpm();
//        if (Direction.LEFT.equals(gear.getDirection())) {
//            final float newOffset = gear.getRadiansOffset() + scaledRPM;
//            if (newOffset > TWO_PI) {
//                gear.setRadiansOffset(gear.getInitialRadiansOffset());
//            } else {
//                gear.setRadiansOffset(newOffset);
//            }
//        } else if (Direction.RIGHT.equals(gear.getDirection())) {
//            final float rad = gear.getRadiansOffset() - scaledRPM;
//            if (abs(rad) > TWO_PI) {
//                gear.setRadiansOffset(gear.getInitialRadiansOffset());
//            } else {
//                gear.setRadiansOffset(rad);
//            }
//        }

// APPROACH 2
        final float scaledRPM = TWO_PI / MyConsts.FPS / 60 * gear.getRpm();
        if (Direction.LEFT.equals(gear.getDirection())) {
            gear.setRadiansOffset(gear.getRadiansOffset() + scaledRPM);
        } else if (Direction.RIGHT.equals(gear.getDirection())) {
            gear.setRadiansOffset(gear.getRadiansOffset() - scaledRPM);
        }

    }

    private void drawRadiansOffset(Gear gear) {
        stroke(255, 0, 0);
        strokeWeight(3L);
        final float rad = gear.getRadiansOffset();
        float xPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * cos(rad);
        float yPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * sin(rad);
        point(xPos, yPos);
        noStroke();
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
        final float completeCircle = TWO_PI + (10 * deltaSinDots); // 10 additional iterations due to graphic "bug"
        for (float rad = 0; rad <= completeCircle; rad += deltaSinDots) {
            float xPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * cos(rad);
            float yPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * sin(rad);
            gearShape.vertex(xPos, yPos);
        }
    }

    private void removeInnerCircle(final Gear gear, final PShape gearShape) {
        final float deltaSinDots = TWO_PI / (gear.getToothCount() * Gear.TOOTH_SIZE);
        for (float rad = TWO_PI - deltaSinDots; rad >= 0; rad -= deltaSinDots) {
            float xPos = (gear.getRadius() / 2f) * cos(rad);
            float yPos = (gear.getRadius() / 2f) * sin(rad);
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
