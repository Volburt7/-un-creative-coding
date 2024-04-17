package app;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

class ParticleSystem {
    private final List<Particle> particles = new ArrayList<>();
    private final PApplet app;
    private final PVector origin;
    private final int color;

    public ParticleSystem(final PApplet app, final PVector origin, final int color) {
        this.app = app;
        this.origin = origin.copy();
        this.color = color;
    }

    public void run() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            p.render();
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }

    public void applyForceToOuter() {
        final PVector dir = origin.copy().sub(app.width * 0.5f, app.height * 0.5f);
        dir.setMag(app.random(0.1f, 2f));
        particles.forEach(p -> p.applyForce(dir));
    }

    public void addParticle() {
        particles.add(new Particle(app, origin, color));
    }
}