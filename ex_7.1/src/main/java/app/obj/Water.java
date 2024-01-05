package app.obj;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
public class Water extends Fluid {
    public Water(PVector position) {
        super(position);
    }
}
