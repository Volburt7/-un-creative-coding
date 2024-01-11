package app.obj;

import app.FluidManager;
import lombok.Getter;
import lombok.Setter;

import static processing.core.PApplet.map;

@Getter
@Setter
public class Fluid {
    private final FluidManager fluidManager;
    private final float x, y;
    private final int initialLifeSpan;

    private int lifeSpan;

    public Fluid(final FluidManager fluidManager, final float x, final float y, final int lifeSpan) {
        this.fluidManager = fluidManager;
        this.x = x;
        this.y = y;
        this.initialLifeSpan = lifeSpan;
        this.lifeSpan = lifeSpan;
    }

    public void update() {
        this.lifeSpan--;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }

    public int getLifeSpan() {
        return (int) map(this.lifeSpan, 0, this.initialLifeSpan, 0, 255);
    }
}
