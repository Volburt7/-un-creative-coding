package app;

import app.obj.Puddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;


public class RainSystem extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(RainSystem.class);
    private final RainManager rainManager;

    public RainSystem() {
        this.rainManager = new RainManager(this);
    }

    public static void makeItRain(final PApplet app, final int rainSpeed) {
        final RainManager pm = new RainManager(app);

//        if (app.frameCount % rainSpeed == 0) {
//            pm.addToList(getRandomPuddle(app.width / 2, app.height / 2));
//        }
//        updatePuddles(pm);
//        drawPuddles(pm);
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

        if (frameCount % 10 == 0) {
            this.rainManager.addToList(getRandomPuddle());
        }

        updatePuddles(this.rainManager);
        drawPuddles(this.rainManager);
    }

    private Puddle getRandomPuddle() {
        return rainManager.createRandomPuddle((int) random(0, width), (int) random(0, height));
    }

    private void updatePuddles(final RainManager pm) {
        pm.getPuddles().forEach(puddle -> {
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

    void drawPuddles(final RainManager pm) {
        noFill();
        pm.getPuddles().stream().filter(Puddle::isActive)
                .forEach(puddle -> {
                    stroke(0, 200, 250, puddle.getLifeSpan());
                    ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
                });
    }
}
