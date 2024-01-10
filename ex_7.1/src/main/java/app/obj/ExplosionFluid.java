package app.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExplosionFluid extends Fluid {
    private float radius;

    public ExplosionFluid(float x, float y, int lifeSpan, float radius) {
        super(x, y, lifeSpan);
        this.radius = radius;
    }

    @Override
    public void update() {
        super.update();
    }
}
