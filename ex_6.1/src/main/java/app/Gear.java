package app;

import app.enums.Direction;
import app.enums.GearCreationState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static processing.core.PApplet.dist;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

@Getter
@Setter
@Builder
public class Gear {
    @Getter(AccessLevel.NONE)
    public static final float TOOTH_SIZE = 20f;

    private float positionX;
    private float positionY;
    private GearCreationState gearCreationState;
    private int toothCount;
    private float radius;
    private float radiansOffset;

    private boolean isMotor;
    private Direction direction;
    private float rpm;

    public void updateSize(final float xTo, final float yTo) {
        final float r = dist(this.getPositionX(), this.getPositionY(), xTo, yTo);
        final float circumcise = TWO_PI * r;
        final int closestTeethCount = Math.round(circumcise / (2 * TOOTH_SIZE));

        this.toothCount = closestTeethCount;
        // Due to the rounding we need to calculate the radius for that exact teeth count
        this.radius = (closestTeethCount * TOOTH_SIZE) / PI;
    }

    public void updateLocation(final float xDirection, final float yDirection) {
        this.setPositionX(xDirection);
        this.setPositionY(yDirection);
    }
}
