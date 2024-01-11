package app;

import app.obj.Fluid;
import app.obj.PuddleFluid;

import java.util.ArrayList;
import java.util.List;

public class FluidManager {
    private final List<Fluid> fluids = new ArrayList<>();

    public void addToList(final Fluid fluid) {
        fluids.add(fluid);
    }

    public List<Fluid> getFluids() {
        return fluids;
    }

    public void spawnRandomPuddle(final FluidParticleSystem particleSystem) {
        this.addToList(new PuddleFluid(this, particleSystem.random(0, particleSystem.width), particleSystem.random(0, particleSystem.height), (int) particleSystem.random(50, 200), particleSystem.random(10, 50)));
    }
}
