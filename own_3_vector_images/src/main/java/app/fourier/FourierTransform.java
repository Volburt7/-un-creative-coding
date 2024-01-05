package app.fourier;

import app.objs.ChainedVector;

import java.util.ArrayList;
import java.util.List;

public class FourierTransform {
    private List<Complex> polyline;
    private List<FourierCircle> transform;
    private double period;

    public FourierTransform(double[][] path, double period) {
        this.polyline = new ArrayList<>();
        for (double[] doubles : path) {
            this.polyline.add(new Complex(doubles[0], doubles[1]));
        }
        this.period = period * path.length;
        this.transform = getTransform(this.polyline);
    }

    public static List<ChainedVector> generateVectorsFromPoints(double[][] points, double[][] motionData) {
        List<ChainedVector> vectors = new ArrayList<>();

        for (int n = 0; n < points.length; n++) {
            double sumX = 0;
            double sumY = 0;

            for (int t = 0; t < points.length; t++) {
                double angle = 2 * Math.PI * n * t / points.length;
                sumX += points[t][0] * Math.cos(angle);
                sumY += points[t][1] * Math.sin(angle);
            }

            double magnitude = Math.sqrt(sumX * sumX + sumY * sumY);
            double phase = Math.atan2(sumY, sumX);

            // Calculate velocity based on motion data
            double dx = motionData[n][0];
            double dy = motionData[n][1];
            double velocity = Math.sqrt(dx * dx + dy * dy);

            vectors.add(new ChainedVector((float) magnitude, (float) phase, (float) velocity));
        }

        // Connect vectors to form a chain
        for (int i = 1; i < vectors.size(); i++) {
            vectors.get(i).setParent(vectors.get(i - 1));
        }

        return vectors;
    }

    private List<FourierCircle> getTransform(List<Complex> polyline) {
        int N = polyline.size();
        final List<FourierCircle> transform = new ArrayList<>();

        for (int k = 0; k < N; k++) {
            Complex current = new Complex(0, 0);
            for (int n = 0; n < N; n++) {
                Complex coef = new Complex(0, -2 * Math.PI * k * n / N);
                current = current.add(coef.exp().multiply(polyline.get(n)));
            }
            transform.add(new FourierCircle(new Complex(0, 0), current.divide(N), k));
        }

        return transform;
    }

    public ChainedVector getLastTransformers() {
        final Complex sample = transform.get(transform.size() - 1).getSample();
        return new ChainedVector((float) sample.getRe(), (float) sample.getIm());
    }

    public List<ChainedVector> calculateVectors(final int n) {
        final List<ChainedVector> vectors = new ArrayList<>();

        final int N = this.transform.size();
        final int nyquist = (int) Math.floor((double) this.transform.size() / 2);
        Complex acc = new Complex(0, 0);

        for (FourierCircle fourierCircle : this.transform) {
            int k = fourierCircle.getK();
            if (k > nyquist) {
                k -= N;
            }
            final Complex oldAcc = acc;
            final Complex angle = new Complex(0, 2 * Math.PI * k * n / this.period);

            acc = acc.add(angle.exp().multiply(fourierCircle.getSample()));
            vectors.add(new ChainedVector(oldAcc.getRe(), oldAcc.getIm(), acc.getRe(), acc.getIm()));
        }

        return vectors;
    }
}