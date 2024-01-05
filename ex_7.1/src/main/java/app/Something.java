package app;

import app.obj.Fluid;
import processing.core.PApplet;


public class Something extends PApplet {

    final AnimalManager animalManager = new AnimalManager();
    final FluidManager fluidManager = new FluidManager();

    @Override
    public void settings() {
        size(777, 444);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {
        background(50);
        fill(130);
        noStroke();
    }
}
