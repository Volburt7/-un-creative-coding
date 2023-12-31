package app;

import app.builders.GearBuilder;
import app.objects.Gear;

import static processing.core.PApplet.dist;

public class DynamicGearHolder {
    private Gear gear;

    public void initializeDynamicGear(final int xPosition, final int yPosition) {
        gear = new GearBuilder()
                .setPositionX(xPosition)
                .setPositionY(yPosition)
                .setRadius(0)
                .build();
    }

    public void update(final float xDirection, final float yDirection) {
        final float radius = dist(gear.getPositionX(), gear.getPositionY(), xDirection, yDirection);
        gear.updateSize(radius);
    }

    public Gear getSnapshotGear() {
        return new Gear(gear);
    }

    public Gear getGear(final float xDirection, final float yDirection) {
        this.update(xDirection, yDirection);
        return gear;
    }
}
