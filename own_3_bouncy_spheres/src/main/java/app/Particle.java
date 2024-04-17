package app;

import processing.core.PApplet;
import processing.core.PVector;

class Particle {
    private final PApplet app;

    private PVector acc;
    private PVector vel;
    private PVector loc;
    private float lifespan;
    private final int color;

    public Particle(final PApplet app, final PVector l, final int color) {
        this.app = app;
        this.acc = new PVector(0, 0);
        float vx = this.app.randomGaussian() * 0.3f;
        float vy = this.app.randomGaussian() * 0.3f - 1.0f;
        this.vel = new PVector(vx, vy);
        this.loc = l.copy();
        this.lifespan = 100.0f;
        this.color = color;
    }

    public boolean isDead() {
        return lifespan <= 0.0f;
    }

    public void applyForce(final PVector f) {
        acc.add(f);
    }

    public void update() {
        vel.add(acc);
        loc.add(vel);
        lifespan -= 2.5f;
        acc.mult(0);
    }

    public void render() {
        app.fill(color, lifespan);
        app.noStroke();
        app.ellipse(loc.x, loc.y, 16, 16);
    }
}