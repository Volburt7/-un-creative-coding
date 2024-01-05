package app.obj;

import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
public class Eatable {
    private PVector position;
    private int size;

    public Eatable(final PVector position) {
        this.position = position;
    }
}
