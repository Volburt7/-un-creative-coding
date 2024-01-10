package app;

import app.obj.Animal;
import app.obj.Chicken;
import app.obj.Cow;
import app.obj.Raptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class AnimalManager {
    private static final Logger LOG = LoggerFactory.getLogger(AnimalManager.class);
    private final List<Animal> animals = new ArrayList<>();

    private final DrawProcessor drawProcessor;
    private final PositionManager positionManager;

    private final PImage chickenImg;
    private final PImage cowImg;
    private final PImage raptorImg;

    public AnimalManager(final DrawProcessor drawProcessor) {
        this.drawProcessor = drawProcessor;
        this.positionManager = new PositionManager(animals);
        this.chickenImg = loadImageWithSize("chicken.png", 30, 36);
        this.cowImg = loadImageWithSize("cow.png", 35, 21);
        this.raptorImg = loadImageWithSize("raptor.png", 45, 25);
    }

    private PImage loadImageWithSize(final String imageName, final int width, final int height) {
        final PImage raptorImg = drawProcessor.loadImage(imageName);
        raptorImg.resize(width, height);
        return raptorImg;
    }

    public void initializeAnimals() {
        for (int i = 0; i < Consts.INITIAL_CHICKEN_COUNT; i++) {
            addAnimalToList(createAnimal(Chicken.class));
        }

        for (int i = 0; i < Consts.INITIAL_COW_COUNT; i++) {
            addAnimalToList(createAnimal(Cow.class));
        }

        for (int i = 0; i < Consts.INITIAL_RAPTOR_COUNT; i++) {
            addAnimalToList(createAnimal(Raptor.class));
        }
    }

    private void addAnimalToList(final Animal animal) {
        if (animal != null && positionManager.hasNoCollision(animal)) {
            animals.add(animal);
        }
    }

    private Animal createAnimal(final Class<?> animalClass) {
        if (Chicken.class.equals(animalClass)) {
            return new Chicken(this.positionManager, this.chickenImg.copy(), PositionManager.getRandomPosition());
        } else if (Cow.class.equals(animalClass)) {
            return new Cow(this.positionManager, this.cowImg.copy(), PositionManager.getRandomPosition());
        } else if (Raptor.class.equals(animalClass)) {
            return new Raptor(this.positionManager, this.raptorImg.copy(), PositionManager.getRandomPosition());
        } else {
            LOG.warn("create animal got the wrong class {}", animalClass.getSimpleName());
            return null;
        }
    }

    public void update() {
        animals.forEach(animal -> {
            if (animal.getNextActionIn() <= 0) {
                animal.setNextActionIn(animal.getActionDelay());
                animal.move();
            } else {
                animal.setNextActionIn(animal.getNextActionIn() - 1);
            }
        });
    }

    public void draw() {
        animals.forEach(animal -> drawProcessor.image(animal.getImage(), animal.getPosition().x, animal.getPosition().y));
    }
}
