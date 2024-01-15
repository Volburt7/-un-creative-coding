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
    private final boolean HIDDEN_MODE = false;
    private final List<Gear> gears = new ArrayList<>();

    private Gear gearInCreation = null;
    private boolean imageFlag = false;

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
        } else if (keyEvent.getKeyCode() == 116) { // F5 = Reload
            updateGearList();
        }
    }

    private void continueGearCreation() {
        gearInCreation.setToNextStage();
        if (GearCreationState.CREATED.equals(gearInCreation.getGearCreationState())) {
            gears.add(gearInCreation);
            updateGearList();
            gearInCreation = null;
        }
    }

    void updateGearList() {
        final List<Gear> updatedGears = new ArrayList<>();
        final List<List<Gear>> initialMotorChain = getInitialMotorChains();

        for (List<Gear> gearChain : initialMotorChain) {
            final Gear motor = gearChain.get(0);
            gearChain.remove(motor);
            updatedGears.add(motor);
            if (!gearChain.isEmpty()) {
                translateGearRotation(motor, gearChain, updatedGears);
            }
        }
    }

    private List<List<Gear>> getInitialMotorChains() {
        final List<List<Gear>> gearChains = new ArrayList<>();
        gears.forEach(gear -> {
            if (gear.isMotor()) {
                final List<Gear> gearChain = new ArrayList<>();
                gearChain.add(gear);
                gearChain.addAll(getSurroundingGears(gear));
                gearChains.add(gearChain);
            }
        });
        return gearChains;
    }

    private List<Gear> getSurroundingGears(final Gear gear) {
        final List<Gear> gearChain = new ArrayList<>();
        gears.forEach(g -> {
            if (!g.equals(gear) && isConnected(gear, g)) {
                gearChain.add(g);
            }
        });
        return gearChain;
    }

    private void translateGearRotation(final Gear motor, final List<Gear> rotateCandidates, final List<Gear> updatedGears) {
        rotateCandidates.stream().filter(gear -> !updatedGears.contains(gear)).forEach(gear -> {
            updatedGears.add(gear);
            final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();
            gear.setRpm(motor.getRpm() * translationRatio);
            gear.setDirection(Direction.LEFT.equals(motor.getDirection()) ? Direction.RIGHT : Direction.LEFT);
            gear.setRadiansOffset(calculateNewOffset(gear, motor));
            translateGearRotation(gear, getSurroundingGears(gear), updatedGears);
        });
        imageFlag = true;
    }

    private float calculateNewOffset(final Gear gear, final Gear motor) {
        final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();
        final float newOffset = motor.getRadiansOffset() * translationRatio - (TWO_PI / gear.getToothCount() / 2);
        return newOffset % TWO_PI;
    }

    private boolean isConnected(final Gear gear1, final Gear gear2) {
        final float dist = dist(gear1.getPositionX(), gear1.getPositionY(), gear2.getPositionX(), gear2.getPositionY());
        final float radiusSum = gear1.getRadius() + gear2.getRadius() + 2 * Gear.TOOTH_SIZE;
        return dist < radiusSum;
    }

    @Override
    public void setup() {
        frameRate(MyConsts.FPS);
        gears.clear();
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

        if (imageFlag) {
            saveFrame("./img/aahg_" + gears.size() + ".png");
            imageFlag = false;
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
        for (float rad = 0; rad < TWO_PI; rad += deltaSinDots) {
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
