package app.obj;

import lombok.Getter;

import static processing.core.PConstants.PI;


@Getter
public class Gear {
    final int toothSize = 15;

    private int positionX;
    private int positionY;
    private int teethCount;
    private float radius;
    private float currentRotation;

    public Gear(int positionX, int positionY, int teethCount, float currentRotation) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.teethCount = teethCount;
        this.currentRotation = currentRotation;
        this.radius = 2 * teethCount * toothSize / PI;
    }
}
