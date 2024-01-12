import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Ignore extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(Ignore.class);

    private final List<FIsh> fIshList = new ArrayList<>();

    float rot = 0;

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
        for (int i = 0; i < 1; i++) {
            final float initialAngle = random(TWO_PI);
            fIshList.add(FIsh.builder()
                    .fishAgent(this)
                    .fishList(fIshList)
                    .position(new PVector(random(0, width), random(0, height)))
                    .acceleration(PVector.random2D())
                    .velocity(new PVector(cos(initialAngle), sin(initialAngle)))
                    .width(random(10, 20))
                    .length(random(40, 300))
                    .maxForce(random(0.02f, 0.10f))
                    .maxSpeed(random(1, 6))
                    .build());
        }
    }

    @Override
    public void draw() {
        background(0);
        strokeWeight(2f);

        final FIsh my_precioous_fish = fIshList.get(0);
        drawFish(my_precioous_fish);
        update(my_precioous_fish);
    }

    private void update(FIsh f) {
        rot += 0.005f;
        f.moveFin();
    }

    private void drawFish(FIsh f) {
        pushMatrix();
        translate(width / 2f, height / 2f);
        rotate(rot);
        shape(f.getBody());

        pushMatrix();
        translate(f.getLength() / 2, 0);
        rotate(radians(f.getFinRotation()));
        shape(f.getFin());
        popMatrix();

        popMatrix();
    }
}