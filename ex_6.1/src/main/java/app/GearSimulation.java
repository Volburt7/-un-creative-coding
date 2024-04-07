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
import java.util.stream.Collectors;

public class GearSimulation extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(GearSimulation.class);
    private final boolean HIDDEN_MODE = false;
    private final List<Gear> gears = new ArrayList<>();

    private Gear gearInCreation = null;

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
                gearInCreation.updateRadius(mouseEvent.getX(), mouseEvent.getY());
            } else {
                gearInCreation.updateLocation(mouseEvent.getX(), mouseEvent.getY());
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && gearInCreation != null) {
            if (gearInCreation.getToothCount() >= 2 && GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
                continueGearCreation();
            }
        }
    }

    @Override
    public void mouseWheel(final MouseEvent mouseEvent) {
        if (gearInCreation != null) {
            switch (gearInCreation.getGearCreationState()) {
                case TYPE -> gearInCreation.updateType();
                case DIRECTION -> gearInCreation.updateDirection();
                case SPEED -> gearInCreation.updateSpeed(mouseEvent.getCount());
            }
        }
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT && gearInCreation != null) {
            if (!GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
                continueGearCreation();
            }
        }
        if (mouseEvent.getButton() == RIGHT && gearInCreation != null) {
            gearInCreation = null; // TODO: check this
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        LOG.debug("Pressed keycode {}", keyEvent.getKeyCode());
        if (gearInCreation != null) {
            switch (keyEvent.getKeyCode()) {
                case BACKSPACE -> gearInCreation = null;
                case ENTER, 32 -> continueGearCreation();
            }
        } else if (keyEvent.getKeyCode() == 116 || keyEvent.getKeyCode() == 82) { // Reload: F5 / R
            updateGearList();
        }
    }

    private void continueGearCreation() {
        gearInCreation.setToNextStage();
        if (GearCreationState.CREATED.equals(gearInCreation.getGearCreationState())) {
            gears.add(gearInCreation);
            gearInCreation = null;
            updateGearList();
        }
    }

    void updateGearList() {
        final List<Gear> motors = getMotors();
        final List<Gear> updatedEntries = new ArrayList<>(motors); // antiLoopList
        motors.forEach(motor -> translateGearRotation(motor, updatedEntries));
    }

    private List<Gear> getMotors() {
        return gears.stream().filter(Gear::isMotor).toList();
    }

    private void translateGearRotation(final Gear motor, final List<Gear> updatedEntries) {
        final List<Gear> connectedGears = getConnectedGears(motor);
        connectedGears.forEach(gear -> {
            if (!gear.isMotor() && !updatedEntries.contains(gear)) {
                applyTranslation(motor, gear);
                updatedEntries.add(gear);
                translateGearRotation(gear, updatedEntries);
            }
        });
    }

    private List<Gear> getConnectedGears(final Gear motor) {
        return gears.stream()
                .filter(gear -> !gear.isMotor())
                .filter(gear -> Gear.isConnected(motor, gear))
                .collect(Collectors.toList());
    }

    private void applyTranslation(final Gear motor, final Gear gear) {
        final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();
        gear.setRpm(motor.getRpm() * translationRatio);
        gear.setDirection(Direction.LEFT.equals(motor.getDirection()) ? Direction.RIGHT : Direction.LEFT);

        final float newOffset = motor.getRadiansOffset() * translationRatio;
//        final float newOffset = motor.getRadiansOffset() * translationRatio - (TWO_PI / motor.getToothCount() / 2);
        gear.setRadiansOffset(newOffset);
    }

    @Override
    public void setup() {
        frameRate(MyConsts.FPS);
        gears.clear();
        addTestGears();
    }

    private void addTestGears() {
        final Gear g1 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(11)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(80)
                .positionY(100)
                .radiansOffset(0)
                .toothCount(10)
                .build();
        g1.updateSize(100);
        gears.add(g1);

        final Gear g2 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(200)
                .positionY(100)
                .radiansOffset(0)
                .toothCount(30)
                .build();
        g2.updateSize(100);
        gears.add(g2);
    }

    @Override
    public void draw() {
        background(50);
        fill(130);
        noStroke();
        gears.forEach(this::drawGear);

        if (gearInCreation != null) {
            drawGear(gearInCreation);
        }
    }

    private void drawGear(final Gear gear) {
        pushMatrix();

        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
        if (!HIDDEN_MODE) {
            drawIntoInnerCircle(gear);
        }
        drawRedDot(gear);
        popMatrix();

        final float scaledRPM = (TWO_PI / MyConsts.FPS / 60) * gear.getRpm();
        if (Direction.LEFT.equals(gear.getDirection())) {
            gear.setRadiansOffset((gear.getRadiansOffset() + scaledRPM) % TWO_PI);
        } else if (Direction.RIGHT.equals(gear.getDirection())) {
            float newOffset = gear.getRadiansOffset() - scaledRPM;
            if (newOffset < 0) {
                gear.setRadiansOffset(TWO_PI - newOffset);
            } else {
                gear.setRadiansOffset(newOffset);
            }
        }
    }

    private void drawRedDot(Gear gear) {
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
        for (float rad = 0; rad <= TWO_PI; rad += deltaSinDots) {
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
