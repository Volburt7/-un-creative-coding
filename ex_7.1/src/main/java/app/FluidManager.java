package app;

import app.obj.Fluid;

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
}
