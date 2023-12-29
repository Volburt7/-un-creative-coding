package app.fourier;

public class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex divide(double divisor) {
        return new Complex(this.re / divisor, this.im / divisor);
    }

    public Complex exp() {
        double expRe = Math.exp(this.re) * Math.cos(this.im);
        double expIm = Math.exp(this.re) * Math.sin(this.im);
        return new Complex(expRe, expIm);
    }

    public Complex multiply(Complex other) {
        double mulRe = this.re * other.re - this.im * other.im;
        double mulIm = this.re * other.im + this.im * other.re;
        return new Complex(mulRe, mulIm);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    /*
       downscale(divisor) {
        return new Complex(this.re / divisor, this.im / divisor);
    };
    * */

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}