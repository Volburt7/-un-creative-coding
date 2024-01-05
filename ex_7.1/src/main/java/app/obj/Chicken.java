package app.obj;

import lombok.Getter;
import lombok.Setter;
import processing.core.PImage;
import processing.core.PVector;

@Getter
@Setter
public class Chicken extends Eatable {
    final int width = 4;
    final int height = 5;

    public Chicken(final PImage img, final PVector position, final int ttl) {
        super(img, position, ttl);
    }
}
