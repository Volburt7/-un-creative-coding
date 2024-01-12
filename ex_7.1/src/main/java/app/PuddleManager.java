package app;

import app.obj.Puddle;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.map;

public class PuddleManager {
    private final static float MIN_STROKE_WEIGHT = 0.5f;
    private final PuddleSystem particleSystem;
    private final List<Puddle> puddles;
    private final boolean collisionEnabled;

    public PuddleManager(final PuddleSystem particleSystem) {
        this.particleSystem = particleSystem;
        this.puddles = new ArrayList<>();
        this.collisionEnabled = false;
    }

    protected List<Puddle> getPuddles() {
        return puddles;
    }

//    public List<Puddle> getPuddlesCopy() {
//        return new ArrayList<>(puddles);
//    }

    public void addToList(final Puddle fluid) {
        puddles.add(fluid);
    }

    public void spawnRandomPuddle(final int x, final int y) {
        puddles.add(createRandomPuddle(this.particleSystem, x, y));
    }

    public void spawnRandomPuddle(final PuddleSystem particleSystem) {
        final int x = (int) particleSystem.random(0, particleSystem.width);
        final int y = (int) particleSystem.random(0, particleSystem.height);
        puddles.add(createRandomPuddle(particleSystem, x, y));
    }

    private Puddle createRandomPuddle(final PuddleSystem particleSystem, final int x, final int y) {
        final int lifeSpan = (int) particleSystem.random(50, 400);
        final float radius = particleSystem.random(10, 50);
        final float maxIntensity = particleSystem.random(1, 3);
        final float velocity = particleSystem.random(1, 2);
        return Puddle.builder()
                .puddleManager(this)
                .x(x)
                .y(y)
                .initialLifeSpan(lifeSpan)
                .lifeSpan(lifeSpan)
                .initialRadius(radius)
                .radius(radius)
                .intensity(maxIntensity)
                .minIntensity(MIN_STROKE_WEIGHT)
                .maxIntensity(maxIntensity)
                .velocity(velocity)
                .build();
    }

    public Puddle createNextPuddle(final Puddle puddle) {
        final float lifespanFactor = this.particleSystem.random(8.5f, 9.5f) / 10;
        final float radiusFactor = this.particleSystem.random(8.5f, 11.5f) / 10;
        final float newVelocity = 4 * puddle.getVelocity() / 5;
        return Puddle.builder()
                .puddleManager(this)
                .x(puddle.getX())
                .y(puddle.getY())
                .initialLifeSpan((int) (puddle.getLifeSpan() * lifespanFactor))
                .lifeSpan((int) (puddle.getLifeSpan() * lifespanFactor))
                .initialRadius(puddle.getRadius() * (1 - radiusFactor))
                .radius(puddle.getRadius() * (1 - radiusFactor))
                .intensity(map(puddle.getIntensity(), puddle.getLifeSpan(), puddle.getLifeSpan() * lifespanFactor, puddle.getMinIntensity(), puddle.getMaxIntensity()))
                .minIntensity(puddle.getMinIntensity() / 2)
                .maxIntensity(puddle.getMaxIntensity())
                .velocity(newVelocity > 1 ? newVelocity : 1)
                .build();
    }
}
