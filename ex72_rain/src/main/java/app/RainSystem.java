package app;

import app.obj.Puddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;

import static app.Consts.MAX_LIFE;
import static app.Consts.MIN_LIFE;


public class RainSystem extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(RainSystem.class);

    private final RainManager rainManager;

    public RainSystem() {
        this.rainManager = new RainManager(this);
    }

    public RainSystem(final PApplet app) {
        this.rainManager = new RainManager(app);
    }

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);

        update(this, 1);
    }

    public void update(final PApplet app, final int speed) {
        if (app.frameCount % speed == 0) {
            final Puddle newPuddle = rainManager.createRandomPuddle((int) random(0, app.width), (int) random(0, app.height));
            this.rainManager.addToList(newPuddle);
        }

        updatePuddles(this.rainManager);
        drawPuddles(app, this.rainManager);
    }

    private void updatePuddles(final RainManager pm) {
        pm.getPuddlesCopy().forEach(puddle -> {
            if (puddle.isActive()) {
                pm.updatePuddle(puddle);

                if (puddle.shouldRipple()) {
                    pm.addToList(pm.createNextPuddle(puddle));
                }
            } else {
                pm.remove(puddle);
            }
        });
    }

    private void drawPuddles(final PApplet app, final RainManager pm) {
        app.noFill();
        pm.getPuddlesCopy().stream().filter(Puddle::isActive)
                .forEach(puddle -> {
                    final float transparency = map(puddle.getLifeSpan(), MIN_LIFE, MAX_LIFE, 0, 255);
                    app.stroke(0, 200, 250, transparency);
                    app.ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
                });
    }
}
