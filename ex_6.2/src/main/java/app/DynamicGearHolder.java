package app;

import app.builders.GearBuilder;
import app.enums.Direction;
import app.enums.GearCreationState;
import app.objects.Gear;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static processing.core.PApplet.dist;

public class DynamicGearHolder {
    private final static Logger LOG = LoggerFactory.getLogger(DynamicGearHolder.class);

    @Getter
    private Gear gear;

    public void initializeDynamicGear(final int xPosition, final int yPosition) {
        gear = new GearBuilder()
                .withPositionX(xPosition)
                .withPositionY(yPosition)
                .withGearCreationState(GearCreationState.SIZE)
                .build();
    }

    public void updateRadius(final float xDirection, final float yDirection) {
        final float radius = dist(gear.getPositionX(), gear.getPositionY(), xDirection, yDirection);
        gear.updateSize(radius);
    }

    public void updateLocation(final float xDirection, final float yDirection) {
        gear.setPositionX(xDirection);
        gear.setPositionY(yDirection);
    }

    public void updateType() {
        gear.setMotor(!gear.isMotor());
        if (gear.isMotor()) {
            this.gear.setDirection(Direction.LEFT);
            this.gear.setRpm(30);
        } else {
            this.gear.setRpm(0);
        }
    }

    public void updateDirection() {
        if (Direction.LEFT.equals(gear.getDirection())) {
            gear.setDirection(Direction.RIGHT);
        } else {
            gear.setDirection(Direction.LEFT);
        }
    }

    public void updateSpeed(final int speedChange) {
        final int newRPM = gear.getRpm() + speedChange;
        if(newRPM >= 1) {
            gear.setRpm(newRPM);
        }
    }

    public void setToNextStage() {
        switch (this.gear.getGearCreationState()) {
            case SIZE -> this.gear.setGearCreationState(GearCreationState.TYPE);
            case TYPE -> {
                if (gear.isMotor()) {
                    this.gear.setGearCreationState(GearCreationState.DIRECTION);
                } else {
                    this.gear.setGearCreationState(GearCreationState.CREATED);
                }
            }
            case DIRECTION -> this.gear.setGearCreationState(GearCreationState.SPEED);
            case SPEED -> this.gear.setGearCreationState(GearCreationState.CREATED);
            default -> LOG.info("Next stage got called for {}.", gear.getGearCreationState());
        }
    }

}
