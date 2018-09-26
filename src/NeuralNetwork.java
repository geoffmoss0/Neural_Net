import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NeuralNetwork {
	
	private Matrix hidden1;
	private Matrix hidden2;
	private Matrix output;
	private Matrix weights_ih1;
	private Matrix weights_h2o;
	private Matrix weights_h1h2;
	private Matrix bias_h1;
	private Matrix bias_h2;
	private Matrix bias_o;
	private double learningRate;
	
	
	public NeuralNetwork(int input, int hidden1, int hidden2, int output) {

		weights_ih1 = new Matrix(hidden1, input);
		weights_h1h2 = new Matrix(hidden2, hidden1);
		weights_h2o = new Matrix(output, hidden2);
		
		weights_ih1.randomize();
		weights_h1h2.randomize();
		weights_h2o.randomize();
		
		bias_h1 = new Matrix(hidden1, 1);
		bias_h2 = new Matrix(hidden2, 1);
		bias_h1.randomize();
		bias_h2.randomize();
		
		bias_o = new Matrix(output, 1);
		bias_o.randomize();
		learningRate = 0.01;
	}
	
	public void printValues() throws FileNotFoundException {
		File f = new File("C:\\Users\\gb_mo\\Desktop\\values.txt");	
		PrintWriter writer = new PrintWriter(f);
		for (int i = 0; i < weights_ih1.getData().length; i++) {
			for (int j = 0; j < weights_ih1.getData()[0].length; j++) {
				writer.print(weights_ih1.getData()[i][j]);
				writer.print(",");
			}
		}
		writer.println("");
		for (int i = 0; i < weights_h1h2.getData().length; i++) {
			for (int j = 0; j < weights_h1h2.getData()[0].length; j++) {
				writer.print(weights_h1h2.getData()[i][j]);
				writer.print(",");
			}
		}
		writer.println("");
		for (int i = 0; i < weights_h2o.getData().length; i++) {
			for (int j = 0; j < weights_h2o.getData()[0].length; j++) {
				writer.print(weights_h2o.getData()[i][j]);
				writer.print(",");
			}
		}
		writer.println("");
		for (int i = 0; i < bias_h1.getData().length; i++) {
			writer.print(bias_h1.getData()[i][0]);
			writer.print(",");
		}
		writer.println("");
		for (int i = 0; i < bias_h2.getData().length; i++) {
			writer.print(bias_h2.getData()[i][0]);
			writer.print(",");
		}
		writer.println("");
		for (int i = 0; i < bias_o.getData().length; i++) {
			writer.print(bias_o.getData()[i][0]);
			writer.print(",");
		}
		writer.println("");
		writer.close();
	}
	
	public Matrix getWeights_ih1() {
		return weights_ih1;
	}

	public void setWeights_ih1(Matrix weights_ih1) {
		this.weights_ih1 = weights_ih1;
	}

	public Matrix getWeights_h2o() {
		return weights_h2o;
	}

	public void setWeights_h2o(Matrix weights_h2o) {
		this.weights_h2o = weights_h2o;
	}

	public Matrix getWeights_h1h2() {
		return weights_h1h2;
	}

	public void setWeights_h1h2(Matrix weights_h1h2) {
		this.weights_h1h2 = weights_h1h2;
	}

	public Matrix getBias_h1() {
		return bias_h1;
	}

	public void setBias_h1(Matrix bias_h1) {
		this.bias_h1 = bias_h1;
	}

	public Matrix getBias_h2() {
		return bias_h2;
	}

	public void setBias_h2(Matrix bias_h2) {
		this.bias_h2 = bias_h2;
	}

	public Matrix getBias_o() {
		return bias_o;
	}

	public void setBias_o(Matrix bias_o) {
		this.bias_o = bias_o;
	}

	public Matrix feedForward(double[] in) throws Exception {
		Matrix input = Matrix.fromArray(in);

		hidden1 = weights_ih1.matrixProduct(input);
		hidden1.add(bias_h1);
		hidden1.activate();
		
		hidden2 = weights_h1h2.matrixProduct(hidden1);
		hidden2.add(bias_h2);
		hidden2.activate();
		
		output = weights_h2o.matrixProduct(hidden2);
		output.add(bias_o);
		output.activate();
			
		return output;
	}
	
	
	public void train(double[] in, Matrix answer) throws Exception {
		
		Matrix input = Matrix.fromArray(in);
		
		
		//gradient for output weights
		Matrix out = feedForward(in);
		Matrix error_o = Matrix.subtract(answer, out);
		out.derive();
		out.multiply(error_o);
		out.multiply(learningRate);
		bias_o.add(out);
		//deltas
		Matrix hidden2T = Matrix.transpose(hidden2);
		Matrix deltas_h2o = out.matrixProduct(hidden2T);
		weights_h2o.add(deltas_h2o);
		
		//gradient for hidden 2 weights
		Matrix w_h2oT = Matrix.transpose(weights_h2o);
		Matrix error_h2 = w_h2oT.matrixProduct(error_o);
		hidden2.derive();
		hidden2.multiply(error_h2);
		hidden2.multiply(learningRate);
		bias_h2.add(hidden2);
		//deltas
		Matrix hidden1T = Matrix.transpose(hidden1);
		Matrix deltas_h1h2 = hidden2.matrixProduct(hidden1T);
		weights_h1h2.add(deltas_h1h2);
		
		//gradient for hidden 1 weights
		Matrix w_h1h2T = Matrix.transpose(weights_h1h2);
		Matrix error_h1 = w_h1h2T.matrixProduct(error_h2);
		hidden1.derive();
		hidden1.multiply(error_h1);
		hidden1.multiply(learningRate);
		bias_h1.add(hidden1);
		//deltas
		Matrix inputT = Matrix.transpose(input);
		Matrix deltas_ih1 = hidden1.matrixProduct(inputT);
		weights_ih1.add(deltas_ih1);
		
	}
	
}
