package app.obj;

import processing.core.PImage;
import processing.core.PVector;

public class Cow extends Animal {
    public final static String imagePath = "cow.png";
    private final static PVector imageRatio = new PVector(5f, 3f);

    public Cow(final PImage image, final PVector position) {
        super(image, imageRatio, 180, 90, position, 180, 90);
    }
}
