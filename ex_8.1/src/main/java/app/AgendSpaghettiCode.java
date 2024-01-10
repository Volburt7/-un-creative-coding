package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;

public class AgendSpaghettiCode extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(AgendSpaghettiCode.class);

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {

    }
}
