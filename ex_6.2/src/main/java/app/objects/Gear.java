package app.objects;

import app.MyConsts;
import app.enums.Direction;
import app.enums.GearCreationState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

@Getter
@Setter
public class Gear {
    @Getter(AccessLevel.NONE)
    public final static int TOOTH_SIZE = 15;

    private float positionX;
    private float positionY;
    private GearCreationState gearCreationState;
    private int toothCount;
    private float radius;
    private float radiansOffset;

    private boolean isMotor;
    private Direction direction;
    private int rpm;

    public Gear(final float positionX, final float positionY, final float radius, final float radiansOffset, final GearCreationState gearCreationState) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.gearCreationState = gearCreationState;
        this.radiansOffset = radiansOffset;
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }

    private int calculateToothCount(final float radius) {
        final float circumcise = TWO_PI * radius;
        return Math.round(circumcise / TOOTH_SIZE / 4);
    }

    public void updateSize(final float radius) {
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }
}
