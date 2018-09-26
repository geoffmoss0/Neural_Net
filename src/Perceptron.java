
public class Perceptron {
	double[] weights = new double[3];
	
	public Perceptron() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (Math.random() * 2) -1;
		}
	}
	
	public int guess(double[] input) {
		int out = 0;
		for (int i = 0; i < input.length; i++) {
			out += (input[i] * weights[i]);
		}
		return sign(out);
	}
	
	public int sign(double i) {
		if (i >= 0) return 1;
		return -1;
	}
	
	public int train(double[] input, int target, double lR) {
		int guess = guess(input);
		int error = target - guess; //2 or -2
		
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] + (error * input[i] * lR);
		}
		return guess;
	}
}
