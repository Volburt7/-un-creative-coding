package app.obj;

import lombok.AllArgsConstructor;
import processing.core.PVector;

@AllArgsConstructor
public class Fluid {

    private PVector position;
    private float rippleSize;
    private int ttl;

}
