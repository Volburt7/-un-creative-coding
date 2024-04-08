package app.obj;

import app.RainManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static processing.core.PApplet.map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Puddle {
    private final RainManager rainManager;
    private final float x, y;
    private final int initialLifeSpan;
    private final float initialRadius;
    private final float minIntensity;
    private final float maxIntensity;
    private final float velocity;

    private float radius;
    private float intensity;
    private int lifeSpan;

    public void update() {
        this.lifeSpan--;
        this.radius += this.velocity;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }

    public int getLifeSpan() {
        return (int) map(this.lifeSpan, 0, this.initialLifeSpan, 0, 255);
    }

    public boolean shouldRipple() {
        return (
                this.initialRadius >= 5f &&
                        (
                                this.lifeSpan == (2 * this.initialLifeSpan / 5) ||
                                        this.lifeSpan == (4 * this.initialLifeSpan / 5)
                        )
        );
    }
}
