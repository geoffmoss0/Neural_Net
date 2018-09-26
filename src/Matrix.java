
public class Matrix {
	private int rows;
	private int cols;
	private double[][] matrix;

	public Matrix(int r, int c) {
		rows = r;
		cols = c;
		matrix = new double[r][c];
	}

	public void multiply(Matrix m) throws Exception {

		if (this.getRows() == m.getRows() && this.getCols() == m.getCols()) {
			for (int i = 0; i < this.getRows(); i++) {
				for (int j = 0; j < this.getCols(); j++) {
					matrix[i][j] *= m.getData()[i][j];
				}
			}
		} else {
			throw new RuntimeException("rows and cols do not match");
		}
	}

	public void subtract(Matrix s) throws Exception {

		if (this.getRows() == s.getRows() && this.getCols() == s.getCols()) {
			for (int i = 0; i < this.getRows(); i++) {
				for (int j = 0; j < this.getCols(); j++) {
					matrix[i][j] -= s.getData()[i][j];
				}
			}
		} else {
			throw new RuntimeException("rows and cols do not match");
		}
	}

	public static Matrix subtract(Matrix a, Matrix b) throws Exception {

		Matrix out = new Matrix(a.getRows(), a.getCols());

		if (a.getRows() == b.getRows() && a.getCols() == b.getCols()) {
			for (int i = 0; i < a.getRows(); i++) {
				for (int j = 0; j < a.getCols(); j++) {
					out.getData()[i][j] = a.getData()[i][j] - b.getData()[i][j];
				}
			}
		} else {
			throw new RuntimeException("rows and cols do not match");
		}

		return out;
	}

	public void add(Matrix a) throws Exception {

		if (this.getRows() == a.getRows() && this.getCols() == a.getCols()) {
			for (int i = 0; i < this.getRows(); i++) {
				for (int j = 0; j < this.getCols(); j++) {
					matrix[i][j] += a.getData()[i][j];
				}
			}
		} else {
			throw new RuntimeException("rows and cols do not match");
		}
	}

	public void add(double a) {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				matrix[i][j] += a;
			}
		}
	}

	public void multiply(double m) {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				matrix[i][j] *= m;
			}
		}
	}

	public void randomize() {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				matrix[i][j] = (Math.random() * 2.0) - 1;
			}
		}
	}

	public Matrix matrixProduct(Matrix b) throws Exception {
		if (this.getCols() == b.getRows()) {
			Matrix out = new Matrix(this.getRows(), b.getCols());
			for (int i = 0; i < out.getRows(); i++) {
				for (int j = 0; j < out.getCols(); j++) {
					double sum = 0.0;
					for (int k = 0; k < this.getCols(); k++) {
						sum += this.getData()[i][k] * b.getData()[k][j];
					}
					out.getData()[i][j] = sum;

				}
			}
			return out;
		} else {
			throw new RuntimeException("a's cols and b's rows do not match");
		}
	}

	public static Matrix transpose(Matrix m) {
		Matrix out = new Matrix(m.getCols(), m.getRows());
		for (int i = 0; i < m.getRows(); i++) {
			for (int j = 0; j < m.getCols(); j++) {
				out.getData()[j][i] = m.getData()[i][j];
			}
		}
		return out;
	}

	public double sigmoid(double x) {
		return (1 / (1 + Math.exp(-x)));
	}

	public void derive() {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				matrix[i][j] = matrix[i][j] * (1 - matrix[i][j]);
			}
		}
	}

	public void activate() {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				matrix[i][j] = sigmoid(matrix[i][j]);
			}
		}
	}

	public void print() {
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				System.out.print(this.getData()[i][j] + " ");
			}
			System.out.println("");
		}
	}

	public double[] toArray() {
		double[] out = new double[this.getRows() * this.getCols()];
		int count = 0;
		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j < this.getCols(); j++) {
				out[count] = matrix[i][j];
				count++;
			}
		}
		return out;
	}

	public static Matrix fromArray(double[] in) {
		Matrix out = new Matrix(in.length, 1);
		for (int i = 0; i < in.length; i++) {
			out.getData()[i][0] = in[i];
		}
		return out;
	}

	public static Matrix fromArray(double[] in, int rows, int cols) {
		Matrix out = new Matrix(rows, cols);
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				out.getData()[i][j] = in[count];
				count++;
			}
		}
		return out;
	}

	public static Matrix getAnswerMatrix(int answer) {
		Matrix out = new Matrix(10, 1);
		for (int i = 0; i < 10; i++) {
			if (i == answer) {
				out.getData()[i][0] = 1.0;
			} else {
				out.getData()[i][0] = 0.0;
			}
		}
		return out;
	}

	public static int[] getAnswers(Matrix m) {
		double max = 0.0;
		int[] ans = new int[] { -1, -1 };
		for (int i = 0; i < m.getData().length; i++) {
			if (m.getData()[i][0] > max) {
				max = m.getData()[i][0];
				ans[0] = i;
			}
		}
		max = 0.0;
		for (int i = 0; i < m.getData().length; i++) {
			if ((m.getData()[i][0] > max) && i != ans[0]) {
				max = m.getData()[i][0];
				ans[1] = i;
			}
		}
		return ans;
	}

	public static double[] centerImage(double[] input) {
		int leftBound = 0;
		outerloop: for (int j = 0; j < 28; j++) {
			for (int i = 0; i < 28; i++) {
				int index = (i * 28) + j;
				if (input[index] != 0.0) {
					leftBound = j;
					break outerloop;
				}
			}
		}
		int rightBound = 0;
		outerloop2: for (int j = 27; j >= 0; j--) {
			for (int i = 0; i < 28; i++) {
				int index = (i * 28) + j;
				if (input[index] != 0.0) {
					rightBound = j;
					break outerloop2;
				}
			}
		}
		int LRdif = (leftBound + (28 - rightBound)) / 2;
		boolean right = false;
		if (leftBound < (28 - rightBound)) {
			right = true;
		}
		if (right) {
			rightShift(input, LRdif - leftBound);
		} else {
			leftShift(input, LRdif - (28 - rightBound));
		}

		int topBound = 0;
		outerloop3: for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				int index = (i * 28) + j;
				if (input[index] != 0.0) {
					topBound = i;
					break outerloop3;
				}
			}
		}

		int bottomBound = 0;
		outerloop4: for (int i = 27; i >= 0; i--) {
			for (int j = 0; j < 28; j++) {
				int index = (i * 28) + j;
				if (input[index] != 0.0) {
					bottomBound = i;
					break outerloop4;
				}
			}
		}
		int UPdif = (topBound + (28 - bottomBound)) / 2;
		boolean down = false;
		if (topBound < (28 - bottomBound)) {
			down = true;
		}
		if (down) {
			downShift(input, UPdif - topBound);
		} else {
			upShift(input, UPdif - (28 - bottomBound));
		}

		return input;
	}

	public static void leftShift(double[] t, int cols) {
		for (int i = 0; i < 28; i++) {
			for (int j = cols; j < 28; j++) {
				int index = (i * 28) + j;
				double temp = t[index];
				t[index] = 0.0;
				t[index - cols] = temp;
			}
		}
	}

	public static void rightShift(double[] t, int cols) {
		for (int i = 0; i < 28; i++) {
			for (int j = 27 - cols; j >= 0; j--) {
				int index = (i * 28) + j;
				double temp = t[index];
				t[index] = 0.0;
				t[index + cols] = temp;
			}
		}
	}

	public static void upShift(double[] t, int rows) {
		for (int i = rows; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				int index = (i * 28) + j;
				double temp = t[index];
				t[index] = 0.0;
				int updex = ((i - rows) * 28) + j;
				t[updex] = temp;
			}
		}
	}

	public static void downShift(double[] t, int rows) {
		for (int i = 27 - rows; i >= 0; i--) {
			for (int j = 0; j < 28; j++) {
				int index = (i * 28) + j;
				double temp = t[index];
				t[index] = 0.0;
				int downdex = ((i + rows) * 28) + j;
				t[downdex] = temp;
			}
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public double[][] getData() {
		return matrix;
	}
}
