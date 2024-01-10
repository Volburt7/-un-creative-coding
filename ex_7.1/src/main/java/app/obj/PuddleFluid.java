package app.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuddleFluid extends Fluid {
    private float size;

    public PuddleFluid(float x, float y, int lifeSpan, float size) {
        super(x, y, lifeSpan);
        this.size = size;
    }

    @Override
    public void update() {
        super.update();
    }
}
