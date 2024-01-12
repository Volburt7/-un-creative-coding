package app.obj;

import app.FluidManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static processing.core.PApplet.map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PuddleFluid {
    private final FluidManager fluidManager;
    private final float x, y;
    private final int initialLifeSpan;
    private final float initialRadius;
    private final float minIntesity;
    private final float maxIntesity;

    private float radius;
    private float intensity;
    private int lifeSpan;

    public void update() {
        this.lifeSpan--;
        this.radius++;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }

    public int getLifeSpan() {
        return (int) map(this.lifeSpan, 0, this.initialLifeSpan, 0, 255);
    }

    public boolean shouldRipple() {
        return (this.lifeSpan == (3 * this.initialLifeSpan / 4) && this.initialRadius >= 5);
    }
}
