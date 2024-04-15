package app;

import app.obj.Puddle;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static app.Consts.*;
import static processing.core.PApplet.map;

public class RainManager {
    private final PApplet app;
    private final List<Puddle> puddles;

    public RainManager(final PApplet app) {
        this.app = app;
        this.puddles = new ArrayList<>();
    }

    protected List<Puddle> getPuddlesCopy() {
        return new ArrayList<>(puddles);
    }

    public void addToList(final Puddle fluid) {
        puddles.add(fluid);
    }

    public Puddle createRandomPuddle(final int x, final int y) {
        final int lifeSpan = (int) this.app.random(MIN_LIFE, MAX_LIFE);
        final float radius = this.app.random(MIN_RADIUS, MAX_RADIUS);
        final float maxIntensity = this.app.random(MIN_INTENSITY, MAX_INTENSITY);
        final float velocity = this.app.random(MIN_VELOCITY, MAX_VELOCITY);
        return Puddle.builder()
                .x(x)
                .y(y)
                .initialLifeSpan(lifeSpan)
                .lifeSpan(lifeSpan)
                .initialRadius(radius)
                .radius(radius)
                .intensity(maxIntensity)
                .minIntensity(MIN_INTENSITY)
                .maxIntensity(maxIntensity)
                .velocity(velocity)
                .build();
    }

    public Puddle createNextPuddle(final Puddle puddle) {

        final float newVelocity = puddle.getVelocity() / 2;
        return Puddle.builder()
                .x(puddle.getX())
                .y(puddle.getY())
                .initialLifeSpan(puddle.getLifeSpan())
                .lifeSpan(puddle.getLifeSpan())
                .initialRadius(puddle.getRadius())
                .radius(puddle.getRadius())
                .intensity(puddle.getIntensity())
                .minIntensity(puddle.getMinIntensity())
                .maxIntensity(puddle.getMaxIntensity())
                .velocity(newVelocity)
                .build();
    }

    public void updatePuddle(final Puddle puddle) {
        puddle.update();
    }

    public void remove(final Puddle puddle) {
        puddles.remove(puddle);
    }
}
