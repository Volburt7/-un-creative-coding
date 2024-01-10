package app.obj;

import app.PositionManager;
import lombok.Getter;
import lombok.Setter;
import processing.core.PImage;
import processing.core.PVector;

@Getter
@Setter
public class Animal {
    private final PositionManager positionManager;
    private final PImage image;
    private final int actionDelay;

    private PVector position;
    private int remainingLifeSpan;
    private int nextActionIn;

    public Animal(final PositionManager positionManager, final PImage image, final PVector position, final int lifeSpan, final int actionDelay) {
        this.positionManager = positionManager;
        this.image = image;
        this.actionDelay = actionDelay;
        this.remainingLifeSpan = lifeSpan;
        this.nextActionIn = actionDelay;
        this.position = position;
    }

    public void move() {
//        updatePosition(getNewPosition());
    }
}
