import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*; 
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Driver {
	
	private ArrayList<Digit> data;
	private NeuralNetwork nn;
	
	public Driver() {
		data = Driver.getTestData();
		nn = new NeuralNetwork(784, 256, 256, 10);
	}
	
	public static void main(String[] args) throws Exception{
		Driver d = new Driver();
		//int correct = 0;
		d.getWeightsFromFile();
		d.test(0);
		
		//for (int i = 55000; i < data.size(); i++) {
		//	if ( Matrix.getAnswer( nn.feedForward( Matrix.fromArray( data.get(i).getPixels() ) ) ) == data.get(i).getValue() ) {
		//		correct++;
		//	}
		//}
		//System.out.println("Amount correct: " + (((double) correct / 5000.0) * 100) + "%");
	}
	
	public void getWeightsFromTraining() throws Exception {
		for (int i = 0; i < 70000; i++) {
			int train = (int) (Math.random() * 55000);
			nn.train(data.get(train).getPixels(), Matrix.getAnswerMatrix(data.get(train).getValue()));
		}
	}
	
	public void getWeightsFromFile() throws FileNotFoundException {
		File f = new File("C:\\Users\\gb_mo\\Desktop\\values.txt");
		Scanner in = new Scanner(f);
		
		//input hidden1
		String[] nums = in.nextLine().split(",");
		Matrix ih1 = new Matrix(256, 784);
		int count = 0;
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 784; j++) {
				ih1.getData()[i][j] = Double.parseDouble(nums[count]);
				count++;
			}
		}
		
		//hidden1 hidden2
		nums = in.nextLine().split(",");
		Matrix h1h2 = new Matrix(256, 256);
		count = 0;
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 256; j++) {
				h1h2.getData()[i][j] = Double.parseDouble(nums[count]);
				count++;
			}
		}
		
		//hidden2 output
		nums = in.nextLine().split(",");
		Matrix h2o = new Matrix(10, 256);
		count = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 256; j++) {
				h2o.getData()[i][j] = Double.parseDouble(nums[count]);
				count++;
			}
		}
		
		//hidden1 bias
		nums = in.nextLine().split(",");
		count = 0;
		Matrix b1 = new Matrix(256, 1);
		for (int i = 0; i < 256; i++) {
			b1.getData()[i][0] = Double.parseDouble(nums[count]);
			count++;
		}
		
		//hidden2 bias
		nums = in.nextLine().split(",");
		count = 0;
		Matrix b2 = new Matrix(256, 1);
		for (int i = 0; i < 256; i++) {
			b2.getData()[i][0] = Double.parseDouble(nums[count]);
			count++;
		}
		
		nums = in.nextLine().split(",");
		count = 0;
		Matrix o = new Matrix(10, 1);
		for (int i = 0; i < 10; i++) {
			o.getData()[i][0] = Double.parseDouble(nums[count]);
			count++;
		}
		
		nn.setWeights_ih1(ih1);
		nn.setWeights_h1h2(h1h2);
		nn.setWeights_h2o(h2o);
		nn.setBias_h1(b1);
		nn.setBias_h2(b2);
		nn.setBias_o(o);
	}
	
	public void test() throws Exception {
		for (int n = 0; n < 10; n++) {
			File f = new File("C:\\Users\\gb_mo\\Desktop\\example.jpg");
			BufferedImage test = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
			test = ImageIO.read(f);	
			
			double[] testData = new double[28*28];
			int count = 0;
			for (int i = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++) {
					testData[count] = ((test.getRGB(j, i) >> 8 ) & 0xff);
					count++;
				}
			}
			
			System.out.print("Best guesses: ");
			for (int i : Matrix.getAnswers(nn.feedForward(Matrix.centerImage(testData)))) {
				System.out.print(i + " ");
			}
			
		}
	}
	
	public void testMNIST() throws Exception {
		int correct = 0;
		
		for (int i = 55000; i < data.size(); i++) {
			if ( Matrix.getAnswers( nn.feedForward( data.get(i).getPixels()))[0] == data.get(i).getValue() ) {
				correct++;
			}
		}
		System.out.println("Amount correct: " + (((double) correct / 5000.0) * 100) + "%");
	}
	
	public void test(int testing) throws Exception {
		File f = new File("C:\\Users\\gb_mo\\Desktop\\example.jpg");
		BufferedImage test = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
		test = ImageIO.read(f);	
		
		double[] testData = new double[28*28];
		int count = 0;
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
					testData[count] = ((test.getRGB(j, i) >> 8 ) & 0xff);
				count++;
			}
		}
		
		nn.feedForward(Matrix.centerImage(testData)).print();
		System.out.print("Best guesses: ");
		for (int i : Matrix.getAnswers(nn.feedForward(Matrix.centerImage(testData)))) {
			System.out.print(i + " ");
		}
	}
	
	public void printValues() throws FileNotFoundException {
		nn.printValues();
	}
	
	public static ArrayList<Digit> getTestData() {
		ArrayList<Digit> out = new ArrayList<Digit>();
		File input = new File("C:\\Users\\gb_mo\\Desktop\\mnist_train.csv");
		Scanner in = null;
		try {
			in = new Scanner(input);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file");
			e.printStackTrace();
		}
		
		while(in.hasNextLine()) {
			double[] nums = new double[28*28];
			String[] split = in.nextLine().split(",");
			
			for (int i = 1; i < split.length; i++) {
				double v = Double.parseDouble(split[i]);
				nums[i-1] = v/255.0;
			}
			out.add(new Digit(nums, Integer.parseInt(split[0])));
		}
		return out;
	}
}
