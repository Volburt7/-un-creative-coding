package app;

import app.obj.Chicken;
import app.obj.Eatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class AnimalManager {
    private final static Logger LOG = LoggerFactory.getLogger(AnimalManager.class);

    private final List<Eatable> animals = new ArrayList<>();

    private final Something something;

    private final PImage chicken;
    private final PImage cow;
    private final PImage raptor;

    public AnimalManager(final Something something) {
        this.something = something;
        this.chicken = something.loadImage("chicken.png");
        this.cow = something.loadImage("cow.png");
        this.raptor = something.loadImage("raptor.png");
    }

    public void initializeAnimals() {
        for (int i = 0; i < 10; i++) {
            animals.add(new Chicken(this.chicken, new PVector(something.random(0, something.width), something.random(0, something.height)), 30));
        }
    }

    public void update() {
        animals.forEach(animal -> {
            something.image(animal.getImage(), animal.getPosition().x, animal.getPosition().y);
        });
    }
}
