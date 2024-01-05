package app.objects.builders;

import app.enums.GearCreationState;
import app.objects.Gear;

public class GearBuilder {
    private float positionX;
    private float positionY;
    private GearCreationState gearCreationState;
    private float radius;
    private float radiansOffset;

    public GearBuilder withPositionX(final float positionX) {
        this.positionX = positionX;
        return this;
    }

    public GearBuilder withPositionY(final float positionY) {
        this.positionY = positionY;
        return this;
    }

    public GearBuilder withGearCreationState(final GearCreationState gearCreationState) {
        this.gearCreationState = gearCreationState;
        return this;
    }

    public GearBuilder withRadius(final float radius) {
        this.radius = radius;
        return this;
    }

    public GearBuilder withRadiansOffset(final float radiansOffset) {
        this.radiansOffset = radiansOffset;
        return this;
    }

    public Gear build() {
        return new Gear(this.positionX, this.positionY, this.radius, this.radiansOffset, this.gearCreationState);
    }
}
