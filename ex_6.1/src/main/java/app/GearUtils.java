package app;

import app.enums.Direction;
import app.enums.GearCreationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static app.enums.GearCreationState.*;
import static processing.core.PApplet.dist;

public class GearUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GearUtils.class);

    private GearUtils() {
    }

    public static boolean areConnected(final Gear gear1, final Gear gear2) {
        final float dist = dist(gear1.getPositionX(), gear1.getPositionY(), gear2.getPositionX(), gear2.getPositionY());
        final float radiusSum = gear1.getRadius() + gear2.getRadius() + 2 * Gear.TOOTH_SIZE;
        return dist < radiusSum;
    }

    public static GearCreationState determineNextStage(final Gear gear) {
        switch (gear.getGearCreationState()) {
            case SIZE -> {
                return TYPE;
            }
            case TYPE -> {
                if (gear.isMotor()) {
                    return DIRECTION;
                } else {
                    return CREATED;
                }
            }
            case DIRECTION -> {
                return SPEED;
            }
            case SPEED -> {
                return CREATED;
            }
            default -> throw new GearException("Unknown Gear state.");
        }
    }



    public static void addTestGearsWithCorrectOffset(final List<Gear> gears) {
        final Gear g1 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(10)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(100)
                .positionY(100)
                .radiansOffset(0f)
                .build();
        g1.updateSize(250, 250);
        gears.add(g1);

        final Gear g2 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(300)
                .positionY(300)
                .radiansOffset(1.5f)
                .build();
        g2.updateSize(340, 340);
        gears.add(g2);

        final Gear g3 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(380)
                .positionY(100)
                .radiansOffset(1)
                .radiansOffset(1)
                .build();
        g3.updateSize(440, 100);
        gears.add(g3);
    }

    public static void addTestGearsNoOffset(final List<Gear> gears) {
        final Gear g1 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(10)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(100)
                .positionY(100)
                .radiansOffset(1.4f)
                .build();
        g1.updateSize(250, 250);
        gears.add(g1);

        final Gear g2 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(300)
                .positionY(300)
                .radiansOffset(0f)
                .build();
        g2.updateSize(340, 340);
        gears.add(g2);

        final Gear g3 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(380)
                .positionY(100)
                .radiansOffset(0f)
                .build();
        g3.updateSize(440, 100);
        gears.add(g3);
    }

    public static void addTestGears(final List<Gear> gears) {
        // 8 gears
        final Gear g1 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(10)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(100)
                .positionY(100)
                .radiansOffset(2f)
                .build();
        g1.updateSize(150, 100);
        gears.add(g1);
        // 20 gears
        final Gear g2 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(10)
                .isMotor(false)
                .direction(Direction.LEFT)
                .positionX(300)
                .positionY(100)
                .radiansOffset(0f)
                .build();
        g2.updateSize(425, 100);
        gears.add(g2);


        // 8 gears
        final Gear g3 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .rpm(2)
                .isMotor(true)
                .direction(Direction.LEFT)
                .positionX(100)
                .positionY(300)
                .radiansOffset(2f)
                .build();
        g3.updateSize(150, 300);
        gears.add(g3);


        // 8 gears
        final Gear g4 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(200)
                .positionY(300)
                .radiansOffset(2f)
                .build();
        g4.updateSize(200, 330);
        gears.add(g4);

        // 8 gears
        final Gear g5 = Gear.builder()
                .gearCreationState(GearCreationState.CREATED)
                .isMotor(false)
                .positionX(290)
                .positionY(320)
                .radiansOffset(2f)
                .build();
        g5.updateSize(330, 320);
        gears.add(g5);
    }

//    public static void addTestGears(final List<Gear> gears) {
//        // 8 gears
//        final Gear g1 = Gear.builder()
//                .gearCreationState(GearCreationState.CREATED)
//                .rpm(10)
//                .isMotor(true)
//                .direction(Direction.LEFT)
//                .positionX(100)
//                .positionY(100)
//                .radiansOffset(2f)
//                .build();
//        g1.updateSize(150, 100);
//        gears.add(g1);
//
//        // 20 gears
//        final Gear g2 = Gear.builder()
//                .gearCreationState(GearCreationState.CREATED)
//                .rpm(10)
//                .isMotor(true)
//                .direction(Direction.LEFT)
//                .positionX(300)
//                .positionY(100)
//                .radiansOffset(2f)
//                .build();
//        g2.updateSize(350, 100);
//        gears.add(g2);
//    }
}
