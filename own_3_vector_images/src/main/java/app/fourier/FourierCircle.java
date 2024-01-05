package app.fourier;

class FourierCircle {
    private final Complex position;
    private final Complex sample;
    private final double radius;
    private final int k;

    public FourierCircle(Complex position, Complex sample, int k) {
        this.position = position;
        this.sample = sample;
        this.radius = Math.sqrt(sample.getIm() * sample.getIm() + sample.getRe() * sample.getRe());
        this.k = k;
    }

    public void update(Complex end) {
        // Update the position and endpoints here (e.g., for animation)
    }

    public Complex getPosition() {
        return position;
    }

    public Complex getSample() {
        return sample;
    }

    public double getRadius() {
        return radius;
    }

    public int getK() {
        return k;
    }
}


