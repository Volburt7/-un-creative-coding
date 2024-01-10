import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;

public class Ignore extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(Ignore.class);

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
        background(255, 0, 0, 0.1f);
    }

    @Override
    public void draw() {

    }
}