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

    public Fluid(final PVector position) {
        this.position = position;
    }
}
