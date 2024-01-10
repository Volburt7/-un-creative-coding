package app;

import app.obj.Animal;
import processing.core.PVector;

import java.util.List;
import java.util.Random;

public class PositionManager {
    private final List<Animal> animals;

    public PositionManager(final List<Animal> animals) {
        this.animals = animals;
    }

    public static PVector getRandomPosition() {
        return new PVector(random(0, Consts.MAIN_WINDOW_WIDTH), random((int) Consts.BG_GRASS_Y_OFFSET, Consts.MAIN_WINDOW_HEIGHT));
    }

    private static int random(final int start, final int end) {
        return new Random().nextInt((start - end) + 1) + end;
    }

    public boolean hasNoCollision(final Animal animal) {
        for (Animal comp : animals) {
            if (!comp.equals(animal) &&
                    comp.getPosition().x < animal.getPosition().x + animal.getImage().width &&
                    comp.getPosition().x + comp.getImage().width > animal.getPosition().x &&
                    comp.getPosition().y < animal.getPosition().y + animal.getImage().height &&
                    comp.getPosition().y + comp.getImage().height > animal.getPosition().y
            ) {
                return false;
            }
        }
        return true;
    }
}
