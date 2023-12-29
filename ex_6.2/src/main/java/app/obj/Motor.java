package app.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Motor extends Gear {
    private float rotationSpeed;

    public Motor(int positionX, int positionY, int teethCount, float currentRotation) {
        super(positionX, positionY, teethCount, currentRotation);
    }
}
