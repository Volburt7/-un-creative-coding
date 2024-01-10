package app.obj;

import app.PositionManager;
import processing.core.PImage;
import processing.core.PVector;


public class Chicken extends Animal {
    public Chicken(final PositionManager positionManager, final PImage image, final PVector position) {
        super(positionManager, image, position, 60, 60);
    }
}
