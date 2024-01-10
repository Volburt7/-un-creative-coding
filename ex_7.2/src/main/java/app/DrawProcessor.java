package app;

import processing.core.PApplet;
import processing.core.PImage;


public class DrawProcessor extends PApplet {
    PImage background;
    AnimalManager animalManager;
    FluidManager fluidManager;

    @Override
    public void settings() {
        size(Consts.MAIN_WINDOW_WIDTH, Consts.MAIN_WINDOW_HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(60);
        background = loadImage("background.jpg");
        background.resize(Consts.MAIN_WINDOW_WIDTH, Consts.MAIN_WINDOW_HEIGHT);

        animalManager = new AnimalManager(this);
        fluidManager = new FluidManager();

        animalManager.initializeAnimals();
    }

    @Override
    public void draw() {
        background(background);
        animalManager.update();
        animalManager.draw();
    }
}
