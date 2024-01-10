package app;

import app.obj.Chicken;
import app.obj.Cow;
import app.obj.Raptor;
import app.obj.Animal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class AnimalManager {
    private final static Logger LOG = LoggerFactory.getLogger(AnimalManager.class);

    private final List<Animal> animals = new ArrayList<>();
    private final DrawProcessor drawProcessor;

    private final PImage chickenImg;
    private final PImage cowImg;
    private final PImage raptorImg;

    public AnimalManager(final DrawProcessor drawProcessor) {
        this.drawProcessor = drawProcessor;
        this.chickenImg = drawProcessor.loadImage(Chicken.imagePath);
        this.cowImg = drawProcessor.loadImage(Cow.imagePath);
        this.raptorImg = drawProcessor.loadImage(Raptor.imagePath);
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
        if (animal != null && hasNoCollision(animal)) {
            animals.add(animal);
        }
    }

    private Animal createAnimal(final Class<?> animalClass) {
        if (Chicken.class.equals(animalClass)) {
            return new Chicken(this.chickenImg.copy(), getRandomPosition());
        } else if (Cow.class.equals(animalClass)) {
            return new Cow(this.cowImg.copy(), getRandomPosition());
        } else if (Raptor.class.equals(animalClass)) {
            return new Raptor(this.raptorImg.copy(), getRandomPosition());
        } else {
            LOG.warn("create animal got the wrong class {}", animalClass.getSimpleName());
            return null;
        }
    }

    private PVector getRandomPosition() {
        return new PVector(drawProcessor.random(0, drawProcessor.width), drawProcessor.random(0, drawProcessor.height));
    }

    private boolean hasNoCollision(final Animal animalToCheck) {
        for (Animal animal : animals) {
            if (!animal.equals(animalToCheck) &&
                animalToCheck.getPosition().x <) {
                return false;
            }
        }
        return true;
    }

    public void update() {
        animals.forEach(animal -> {
            drawProcessor.image(animal.getImage(), animal.getPosition().x, animal.getPosition().y);
        });
    }
}
