package app;

import processing.core.PApplet;
import processing.core.PImage;


public class Something extends PApplet {

    PImage background;
    AnimalManager animalManager;
    FluidManager fluidManager;

    @Override
    public void settings() {
        size(Consts.WIDTH, Consts.HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(60);
        background = loadImage("background.jpg");
        background.resize(Consts.WIDTH, Consts.HEIGHT);

        animalManager = new AnimalManager(this);
        fluidManager = new FluidManager();

        animalManager.initializeAnimals();
    }

    @Override
    public void draw() {
        background(background);
        animalManager.update();
    }
}
