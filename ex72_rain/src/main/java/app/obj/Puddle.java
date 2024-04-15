package app.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Puddle {
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
        this.intensity = this.intensity * 0.9f;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }

    public boolean shouldRipple() {
        return (
                this.initialRadius >= 4f &&
                        (
                                // Add ripple on 2/5 and 4/5 of lifespan
                                //  => 2 new ripples per ripple if radius fine
                                this.lifeSpan == (this.initialLifeSpan * (2 / 5)) ||
                                this.lifeSpan == (this.initialLifeSpan * (4 / 5))
                        )
        );
    }
}
