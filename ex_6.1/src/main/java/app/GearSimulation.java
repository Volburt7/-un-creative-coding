package app;

import app.enums.Direction;
import app.enums.GearCreationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GearSimulation extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(GearSimulation.class);
    private static final boolean SHOW_GEAR_CREATION = true;
    private static final int DEFAULT_COLOR = 130;
    private final List<Gear> gears = new ArrayList<>();

    private int dragCount = 0;
    private PImage left;
    private PImage right;
    private Gear gearInCreation;

    @Override
    public void settings() {
        size(1600, 900);
    }

    @Override
    public void mouseDragged(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            dragCount++;
            if (gearInCreation != null) {
                dragCount = 0;
                if (GearCreationState.SIZE.equals(gearInCreation.getGearCreationState())) {
                    gearInCreation.updateSize(mouseEvent.getX(), mouseEvent.getY());
                } else {
                    gearInCreation.updateLocation(mouseEvent.getX(), mouseEvent.getY());
                }
            } else if (dragCount > 15) {
                gearInCreation = Gear.builder()
                        .positionX(mouseEvent.getX())
                        .positionY(mouseEvent.getY())
                        .gearCreationState(GearCreationState.SIZE)
                        .color(color(DEFAULT_COLOR))
                        .build();
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        dragCount = 0;
        if (mouseEvent.getButton() == LEFT &&
                gearInCreation != null &&
                gearInCreation.getToothCount() > 1 &&
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
                case COLOR -> updateColor();
                default -> LOG.info("Scrolling won't effect state '{}'", gearInCreation.getGearCreationState());
            }
        }
    }

    private void toggleType() {
        gearInCreation.setMotor(!gearInCreation.isMotor());
        if (gearInCreation.isMotor()) {
            gearInCreation.setDirection(Direction.LEFT);
            gearInCreation.setRpm(5);
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

    private void updateColor() {
        gearInCreation.setColor(color(random(255), random(255), random(255)));
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            if (gearInCreation != null && !GearCreationState.SIZE.equals(gearInCreation.getGearCreationState()))
                continueGearCreation();
            else {
                final Gear clickedGear = getClickedGear(mouseEvent.getX(), mouseEvent.getY());
                gears.forEach(gear -> gear.setSelected(false));
                if (clickedGear != null) {
                    clickedGear.setSelected(true);
                }
            }
        } else if (mouseEvent.getButton() == RIGHT) {
            gearInCreation = null;
            final Gear selectedGear = getSelectedGear();
            if (selectedGear != null) {
                if (selectedGear.equals(getClickedGear(mouseEvent.getX(), mouseEvent.getY()))) {
                    gears.remove(selectedGear);
                    gears.forEach(g -> g.setBlocked(false));
                    updateGearList();
                } else {
                    selectedGear.setSelected(false);
                }
            }
        }
    }

    private Gear getClickedGear(final int x, final int y) {
        for (Gear gear : gears) {
            float distance = dist(x, y, gear.getPositionX(), gear.getPositionY());
            if (distance <= gear.getRadius()) {
                return gear;
            }
        }
        return null;
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

    // TODO: I used the wrong data structure tbh but in the beginning I was too lazy to implement it as a linked list.
    private void updateGearList() {
        final List<Gear> motors = getMotors();
        final List<Gear> updatedEntries = new ArrayList<>(motors);
        // Quasi n doppelt/dreifach verschachtelter Loop, aber wir wollen ja mal nicht Auge machen
        motors.forEach(motor -> {
            final List<Gear> chainedList = getChainedList(motor);
            final long numMotors = chainedList.stream().filter(Gear::isMotor).count();
            if(numMotors == 1) {
                translateGearRotation(motor, updatedEntries);
            } else {
                chainedList.forEach(gear -> gear.setBlocked(true));
            }
        });
    }

    private List<Gear> getChainedList(final Gear motor) {
        final List<Gear> chainedList = new ArrayList<>();
        someRecursiveTrash(chainedList, motor);
        return chainedList;
    }

    private void someRecursiveTrash(final List<Gear> list, final Gear gear) {
        getAllConnectedGears(gear).forEach(g -> {
            if (!list.contains(g)) {
                list.add(g);
                someRecursiveTrash(list, g);
            }
        });
    }

    private List<Gear> getMotors() {
        return gears.stream().filter(Gear::isMotor).toList();
    }

    private void translateGearRotation(final Gear motor, final List<Gear> updatedEntries) {
        getAllConnectedGears(motor).forEach(gear -> {
            if (!gear.equals(motor) && !updatedEntries.contains(gear)) {
                // Updates the gear itself
                applyTranslation(motor, gear);
                gear.setColor(motor.getColor());
                updatedEntries.add(gear);

                // Recursive translation
                translateGearRotation(gear, updatedEntries);
            }
        });
    }

    private List<Gear> getAllConnectedGears(final Gear motor) {
        return gears.stream()
                .filter(Objects::nonNull)
                .filter(gear -> !gear.equals(motor))
                .filter(gear -> GearUtils.areConnected(motor, gear))
                .toList();
    }

    private void applyTranslation(final Gear motor, final Gear gear) {
        final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();
        gear.setRpm(motor.getRpm() * translationRatio);
        gear.setDirection(Direction.LEFT.equals(motor.getDirection()) ? Direction.RIGHT : Direction.LEFT);
        gear.setRadiansOffset(calculateNewRadiansOffset(motor, gear));
    }

    private float calculateNewRadiansOffset(final Gear motor, final Gear gear) {
        float motorToGearAngle = atan2(gear.getPositionY() - motor.getPositionY(), gear.getPositionX() - motor.getPositionX());
        if (motorToGearAngle < 0) {
            motorToGearAngle += TWO_PI;
        }
        final float gearToMotorAngle = (motorToGearAngle + PI) % TWO_PI;

        final float singleGearSin = TWO_PI / gear.getToothCount();

        final float translationRatio = (float) motor.getToothCount() / (float) gear.getToothCount();

        // the following line I got from https://geargenerator.com -> I gave up on wrapping my head around it....
        final float newRadOffset = PI - ((motor.getRadiansOffset() + gearToMotorAngle) * translationRatio) - gearToMotorAngle;

        if ((gear.getToothCount() + motor.getToothCount()) % 2 == 0) {
            // even - even or odd - odd
            return newRadOffset;
        } else {
            // even - odd | odd - even
            return newRadOffset + singleGearSin / 2;
        }
    }

    @Override
    public void setup() {
        frameRate(MyConsts.FPS);
        gears.clear();
        loadResources();
    }

    private void loadResources() {
        left = loadImage("left.png");
        right = loadImage("right.png");
    }

    @Override
    public void draw() {
        background(0);

        gears.forEach(this::drawGear);
        if (gearInCreation != null && gearInCreation.getToothCount() > 0) {
            drawGear(gearInCreation);
            updateGearRadians(gearInCreation);
        }

        gears.forEach(this::updateGearRadians);

        final Gear selectedGear = getSelectedGear();
        if (selectedGear != null) {
            displayGearInfo(selectedGear);
        }
    }

    private Gear getSelectedGear() {
        return gears.stream().filter(Gear::isSelected).findFirst().orElse(null);
    }

    private void displayGearInfo(final Gear gear) {
        pushMatrix();
        fill(255);
        textSize(14);
        textAlign(LEFT, TOP);
        final String info = "Position: (" + gear.getPositionX() + ", " + gear.getPositionY() + ")\n" +
                "Tooth Count: " + gear.getToothCount() + "\n" +
                "Radius: " + gear.getRadius() + "\n" +
                "Radians Offset: " + gear.getRadiansOffset() + "\n" +
                "Is Motor: " + gear.isMotor() + "\n" +
                "RPM: " + gear.getRpm() + "\n" +
                "Color: " + hex(gear.getColor());
        text(info, 10, 10);
        popMatrix();
    }

    private void drawGear(final Gear gear) {
        pushMatrix();

        translate(gear.getPositionX(), gear.getPositionY());
        shape(getGearShape(gear));
        if (SHOW_GEAR_CREATION) {
            drawIntoInnerCircle(gear);
        }
        drawRadiansOffset(gear);

        popMatrix();
    }

    private void updateGearRadians(final Gear gear) {
        if (gear.isBlocked()) return;
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
            gearShape.fill(determineColor(gear));
        } else {
            gearShape.beginShape(POINTS);
            gearShape.stroke(255);
            gearShape.strokeWeight(1.5f);
        }

        drawTeeth(gear, gearShape);
        gearShape.beginContour();
        removeInnerCircle(gear, gearShape);
        gearShape.endContour();

        gearShape.endShape(CLOSE);

        return gearShape;
    }

    private int determineColor(final Gear gear) {
        if (gear.isSelected()) {
            return color(0, 255, 0);
        } else if (gear.isBlocked()) {
            return color(255, 0, 0);
        } else {
            return gear.getColor();
        }
    }

    private void drawTeeth(final Gear gear, final PShape gearShape) {
        final float deltaSinDots = TWO_PI / (gear.getToothCount() * Gear.TOOTH_SIZE);
        final float completeCircle = TWO_PI + (deltaSinDots * 5); // 5 additional iterations due to graphic "bug"
        for (float rad = 0; rad <= completeCircle; rad += deltaSinDots) {
            // Following 2 lines from https://math.stackexchange.com/questions/225351/equation-of-sine
            float xPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * cos(rad);
            float yPos = (gear.getRadius() + Gear.TOOTH_SIZE * sin(gear.getToothCount() * (rad + gear.getRadiansOffset()))) * sin(rad);
            gearShape.vertex(xPos, yPos);
        }
    }

    private void removeInnerCircle(final Gear gear, final PShape gearShape) {
        final float deltaSinDots = TWO_PI / (gear.getToothCount() * Gear.TOOTH_SIZE);
        for (float rad = TWO_PI; rad >= 0; rad -= deltaSinDots) {
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
                final float size = gear.getRadius() / 2;
                pushMatrix();
                translate(-size / 2, -size / 2);
                if (Direction.LEFT.equals(gear.getDirection())) {
                    final PImage leftCpy = left.copy();
                    changeImageColor(leftCpy, color(DEFAULT_COLOR));
                    leftCpy.resize((int) size, (int) size);
                    image(leftCpy, 0, 0);
                } else {
                    final PImage rightCpy = right.copy();
                    changeImageColor(rightCpy, color(DEFAULT_COLOR));
                    rightCpy.resize((int) size, (int) size);
                    image(rightCpy, 0, 0);
                }
                popMatrix();
            }
            case SPEED, COLOR, CREATED -> {
                fill(gear.getColor());
                text(round(gear.getRpm()), 0, yTextOffset);
            }
            default -> LOG.info("Maybe something got wrong. Type was '{}'", gear.getGearCreationState());
        }
    }

    private void changeImageColor(final PImage img, final int color) {
        loadPixels();
        for (int i = 0; i < img.width; i++) {
            for (int j = 0; j < img.height; j++) {
                int pixelIndex = i + j * img.width;
                // only change non-transparent pixels
                if (alpha(img.pixels[pixelIndex]) > 0) {
                    img.pixels[pixelIndex] = color;
                }
            }
        }
        updatePixels();
    }
}
