package app.obj;


import processing.core.PImage;
import processing.core.PVector;

public class Raptor extends Animal {
    public final static String imagePath = "raptor.png";
    private final static PVector imageRatio = new PVector(5f, 2.75f);

    public Raptor(final PImage image, final PVector position) {
        super(image, imageRatio, 60, 30, position, 60, 30);
    }
}
