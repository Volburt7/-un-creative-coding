package app.objects;

import app.enums.Direction;
import app.enums.GearCreationState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static app.enums.GearCreationState.DIRECTION;
import static app.enums.GearCreationState.TYPE;
import static processing.core.PApplet.dist;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

@Getter
@Setter
@Builder
public class Gear {
    @Getter(AccessLevel.NONE)
    public final static int TOOTH_SIZE = 20;

    private float positionX;
    private float positionY;
    private GearCreationState gearCreationState;
    private int toothCount;
    private float radius;
    private float radiansOffset;

    private boolean isMotor;
    private Direction direction;
    private float rpm;

    private int calculateToothCount(final float radius) {
        final float circumcise = TWO_PI * radius;
        return Math.round(circumcise / TOOTH_SIZE / 4);
    }

    public void updateRadius(final float xDirection, final float yDirection) {
        final float radius = dist(this.getPositionX(), this.getPositionY(), xDirection, yDirection);
        this.updateSize(radius);
    }

    public void updateLocation(final float xDirection, final float yDirection) {
        this.setPositionX(xDirection);
        this.setPositionY(yDirection);
    }

    public void updateType() {
        this.setMotor(!this.isMotor());
        if (this.isMotor()) {
            this.setDirection(Direction.LEFT);
            this.setRpm(30);
        } else {
            this.setRpm(0);
        }
    }

    public void updateDirection() {
        if (Direction.LEFT.equals(this.getDirection())) {
            this.setDirection(Direction.RIGHT);
        } else {
            this.setDirection(Direction.LEFT);
        }
    }

    public void updateSpeed(final int speedChange) {
        final float newRPM = this.getRpm() + speedChange;
        if (newRPM >= 1) {
            this.setRpm(newRPM);
        }
    }

    public void setToNextStage() {
        switch (this.getGearCreationState()) {
            case SIZE -> this.setGearCreationState(TYPE);
            case TYPE -> {
                if (this.isMotor()) {
                    this.setGearCreationState(DIRECTION);
                } else {
                    this.setGearCreationState(GearCreationState.CREATED);
                }
            }
            case DIRECTION -> this.setGearCreationState(GearCreationState.SPEED);
            case SPEED -> this.setGearCreationState(GearCreationState.CREATED);
        }
    }

    public void updateSize(final float radius) {
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }
}
