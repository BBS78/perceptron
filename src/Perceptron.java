import java.util.Random;

public class Perceptron {

    private double[] weights;
    private double bias;
    private double a;

    public Perceptron(int i, double a) {
        this.a = a;
        this.weights = new double[i];
        Random rand = new Random();
        this.bias = rand.nextDouble();;

        for (int j = 0; j < i; j++) {
            weights[j] = rand.nextDouble();
        }
    }

    public int predict(double[] x) {
        double sum = 0.0;

        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * x[i];
        }

        sum -= bias;

        if (sum>=0){
            return 1;
        }else{
            return 0;
        }
    }

    public void train(double[] x, int d) {
        int y = predict(x);

        for (int i = 0; i < weights.length; i++) {
            weights[i] += a * (d - y) * x[i];
        }

        bias -= a * (d - y);
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }
    public double getA() {
        return a;
    }
    public void setA(double a){
        this.a = a;
    }
}
