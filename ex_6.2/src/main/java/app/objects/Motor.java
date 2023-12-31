package app.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Motor extends Gear {
    private float rotationSpeed;

    public Motor(int positionX, int positionY, float radius) {
        super(positionX, positionY, radius);
    }
}
