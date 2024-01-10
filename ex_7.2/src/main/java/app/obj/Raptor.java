package app.obj;


import app.PositionManager;
import processing.core.PImage;
import processing.core.PVector;

public class Raptor extends Animal {
    public Raptor(final PositionManager positionManager, final PImage image, final PVector position) {
        super(positionManager, image, position, 60, 30);
    }
}
