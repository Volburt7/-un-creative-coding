package app.obj;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
public class Blood extends Fluid {

    public Blood(PVector position) {
        super(position);
    }
}
