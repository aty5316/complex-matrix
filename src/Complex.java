public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        if (!isFinite(real) || !isFinite(imaginary)) {
            throw new IllegalArgumentException(
                    "Число должно быть конечным. Возможно, произошло переполнение.");
        }
        this.real = real;
        this.imaginary = imaginary;
    }

    private static boolean isFinite(double value) {
        return value >= -Double.MAX_VALUE && value <= Double.MAX_VALUE;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex other) {
        double resultReal = real + other.real;
        double resultImaginary = imaginary + other.imaginary;
        return new Complex(resultReal, resultImaginary);
    }

    public Complex subtract(Complex other) {
        double resultReal = real - other.real;
        double resultImaginary = imaginary - other.imaginary;
        return new Complex(resultReal, resultImaginary);
    }

    public Complex multiply(Complex other) {
        double resultReal = real * other.real - imaginary * other.imaginary;
        double resultImaginary = real * other.imaginary + imaginary * other.real;
        return new Complex(resultReal, resultImaginary);
    }

    public Complex divide(Complex other) {
        if (other.isZero()) {
            throw new ArithmeticException("Деление комплексного числа на ноль.");
        }
        double denominator = other.real * other.real
                + other.imaginary * other.imaginary;
        if (denominator == 0.0) {
            throw new ArithmeticException("Делитель слишком мал для вычислений с double.");
        }
        if (!isFinite(denominator)) {
            throw new ArithmeticException("Делитель слишком велик для вычислений с double.");
        }
        double resultReal = (real * other.real + imaginary * other.imaginary)
                / denominator;
        double resultImaginary = (imaginary * other.real - real * other.imaginary)
                / denominator;
        return new Complex(resultReal, resultImaginary);
    }

    public boolean isZero() {
        return real == 0.0 && imaginary == 0.0;
    }

    public String toString() {
        if (imaginary == 0.0) {
            return "" + real;
        }
        if (real == 0.0) {
            return imaginary + "i";
        }
        if (imaginary < 0.0) {
            return real + " - " + (-imaginary) + "i";
        }
        return real + " + " + imaginary + "i";
    }
}
