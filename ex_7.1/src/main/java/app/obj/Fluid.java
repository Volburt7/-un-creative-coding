package app.obj;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
public class Fluid {

    private PVector position;
    private float rippleSize;
    private int ttl;

    public Fluid(PVector position, float rippleSize, int ttl) {
        this.position = position;
        this.rippleSize = rippleSize;
        this.ttl = ttl;
    }
}
