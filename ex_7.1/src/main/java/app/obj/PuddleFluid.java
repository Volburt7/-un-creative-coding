package app.obj;

import app.FluidManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuddleFluid extends Fluid {
    private float radius;

    public PuddleFluid(final FluidManager fluidManager, final float x, final float y, final int lifeSpan, final float radius) {
        super(fluidManager, x, y, lifeSpan);
        this.radius = radius;
    }

    @Override
    public void update() {
        super.update();
    }
}
