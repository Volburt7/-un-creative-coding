package app.builders;

import app.objects.Gear;

public class GearBuilder {
    private int positionX;
    private int positionY;
    private float radius;
    private float currentRotation;

    public GearBuilder setPositionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public GearBuilder setPositionY(int positionY) {
        this.positionY = positionY;
        return this;
    }

    public GearBuilder setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public GearBuilder setCurrentRotation(float currentRotation) {
        this.currentRotation = currentRotation;
        return this;
    }

    public Gear build() {
        return new Gear(this.positionX, this.positionY, this.radius, this.currentRotation);
    }
}
