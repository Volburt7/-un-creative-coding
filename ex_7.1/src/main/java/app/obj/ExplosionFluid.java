package app.obj;

import app.FluidManager;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ExplosionFluid extends Fluid {
    private float speed;

    public ExplosionFluid(final FluidManager fluidManager, final float x, final float y, final int lifeSpan, final float speed) {
        super(fluidManager, x, y, lifeSpan);
        this.speed = speed;
    }

    @Override
    public void update() {
        super.update();
    }
}
