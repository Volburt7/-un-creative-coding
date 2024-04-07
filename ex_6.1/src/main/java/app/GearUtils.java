package app;

import app.enums.GearCreationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
