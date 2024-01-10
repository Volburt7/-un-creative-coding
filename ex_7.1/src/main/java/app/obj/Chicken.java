package app.obj;

import processing.core.PImage;
import processing.core.PVector;

import java.util.List;


public class Chicken extends Animal {
    public final static String imagePath = "chicken.png";
    private final static PVector imageRatio = new PVector(4f, 5f);

    public Chicken(final PImage image, final PVector position) {
        super(image, imageRatio, 60, 60, position, 60, 60);
    }
}
