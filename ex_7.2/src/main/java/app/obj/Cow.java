package app.obj;

import app.PositionManager;
import processing.core.PImage;
import processing.core.PVector;

public class Cow extends Animal {
    public Cow(final PositionManager positionManager, final PImage image, final PVector position) {
        super(positionManager, image, position, 180, 90);
    }
}
