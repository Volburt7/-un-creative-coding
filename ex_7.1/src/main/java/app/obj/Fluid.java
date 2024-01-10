package app.obj;

import app.FluidManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fluid {
    private final FluidManager fluidManager;
    private final float x, y;

    private int lifeSpan;

    public Fluid(final FluidManager fluidManager, final float x, final float y, final int lifeSpan) {
        this.fluidManager = fluidManager;
        this.x = x;
        this.y = y;
        this.lifeSpan = lifeSpan;
    }

    public void update() {
        this.lifeSpan--;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }
}
