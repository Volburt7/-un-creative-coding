package app;

import app.obj.PuddleFluid;

import java.util.ArrayList;
import java.util.List;

public class FluidManager {
    private final List<PuddleFluid> fluids = new ArrayList<>();

    public void addToList(final PuddleFluid fluid) {
        fluids.add(fluid);
    }

    public List<PuddleFluid> getPuddles() {
        return fluids;
    }

    public void spawnRandomPuddle(final FluidParticleSystem particleSystem) {
        new PuddleFluid(this, particleSystem.random(0, particleSystem.width), particleSystem.random(0, particleSystem.height), (int) particleSystem.random(50, 200), particleSystem.random(10, 50))
        this.addToList();
    }
}
