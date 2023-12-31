package app.objects;

import lombok.AccessLevel;
import lombok.Getter;

import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

@Getter
public class Gear {
    @Getter(AccessLevel.NONE)
    public final static int TOOTH_SIZE = 15;

    private final int positionX;
    private final int positionY;
    private int toothCount;
    private float radius;
    private float currentRotation;

    public Gear(final Gear gear) {
        this.positionX = gear.getPositionX();
        this.positionY = gear.getPositionY();
        this.currentRotation = gear.getCurrentRotation();
        this.toothCount = gear.getToothCount();
        this.radius = gear.getRadius();
    }

    public Gear(int positionX, int positionY, final float radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentRotation = 0;
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }

    public Gear(int positionX, int positionY, final float radius, float currentRotation) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentRotation = currentRotation;
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }

    private int calculateToothCount(final float radius) {
        final float circumcise = TWO_PI * radius;
        return Math.round(circumcise / TOOTH_SIZE / 2);
    }

    public void updateSize(final float radius) {
        this.toothCount = calculateToothCount(radius);
        this.radius = this.toothCount * TOOTH_SIZE / PI;
    }

}
