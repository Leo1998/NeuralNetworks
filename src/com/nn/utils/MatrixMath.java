package com.nn.utils;

public class MatrixMath {

	private MatrixMath() {
	}

	public static Matrix add(final Matrix a, final Matrix b) {
		if (a.getRows() != b.getRows()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getRows() + " rows and matrix b has " + b.getRows() + " rows.");
		}

		if (a.getCols() != b.getCols()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getCols() + " cols and matrix b has " + b.getCols() + " cols.");
		}

		final double result[][] = new double[a.getRows()][a.getCols()];

		for (int resultRow = 0; resultRow < a.getRows(); resultRow++) {
			for (int resultCol = 0; resultCol < a.getCols(); resultCol++) {
				result[resultRow][resultCol] = a.get(resultRow, resultCol) + b.get(resultRow, resultCol);
			}
		}

		return new Matrix(result);
	}

	public static void copy(final Matrix source, final Matrix target) {
		for (int row = 0; row < source.getRows(); row++) {
			for (int col = 0; col < source.getCols(); col++) {
				target.set(row, col, source.get(row, col));
			}
		}

	}

	public static Matrix divide(final Matrix a, final double b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) / b;
			}
		}
		return new Matrix(result);
	}

	public static double dotProduct(final Matrix a, final Matrix b) {
		if (!a.isVector() || !b.isVector()) {
			throw new MatrixError("To take the dot product, both matrices must be vectors.");
		}

		final Double aArray[] = a.toPackedArray();
		final Double bArray[] = b.toPackedArray();

		if (aArray.length != bArray.length) {
			throw new MatrixError("To take the dot product, both matrices must be of the same length.");
		}

		double result = 0;
		final int length = aArray.length;

		for (int i = 0; i < length; i++) {
			result += aArray[i] * bArray[i];
		}

		return result;
	}

	public static Matrix identity(final int size) {
		if (size < 1) {
			throw new MatrixError("Identity matrix must be at least of size 1.");
		}

		final Matrix result = new Matrix(size, size);

		for (int i = 0; i < size; i++) {
			result.set(i, i, 1);
		}

		return result;
	}

	public static Matrix multiply(final Matrix a, final double b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) * b;
			}
		}
		return new Matrix(result);
	}

	public static Matrix multiply(final Matrix a, final Matrix b) {
		if (a.getCols() != b.getRows()) {
			throw new MatrixError(
					"To use ordinary matrix multiplication the number of columns on the first matrix must mat the number of rows on the second.");
		}

		final double result[][] = new double[a.getRows()][b.getCols()];

		for (int resultRow = 0; resultRow < a.getRows(); resultRow++) {
			for (int resultCol = 0; resultCol < b.getCols(); resultCol++) {
				double value = 0;

				for (int i = 0; i < a.getCols(); i++) {

					value += a.get(resultRow, i) * b.get(i, resultCol);
				}
				result[resultRow][resultCol] = value;
			}
		}

		return new Matrix(result);
	}

	public static Matrix subtract(final Matrix a, final Matrix b) {
		if (a.getRows() != b.getRows()) {
			throw new MatrixError(
					"To subtract the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getRows() + " rows and matrix b has " + b.getRows() + " rows.");
		}

		if (a.getCols() != b.getCols()) {
			throw new MatrixError(
					"To subtract the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getCols() + " cols and matrix b has " + b.getCols() + " cols.");
		}

		final double result[][] = new double[a.getRows()][a.getCols()];

		for (int resultRow = 0; resultRow < a.getRows(); resultRow++) {
			for (int resultCol = 0; resultCol < a.getCols(); resultCol++) {
				result[resultRow][resultCol] = a.get(resultRow, resultCol) - b.get(resultRow, resultCol);
			}
		}

		return new Matrix(result);
	}

	public static Matrix transpose(final Matrix input) {
		final double inverseMatrix[][] = new double[input.getCols()][input.getRows()];

		for (int r = 0; r < input.getRows(); r++) {
			for (int c = 0; c < input.getCols(); c++) {
				inverseMatrix[c][r] = input.get(r, c);
			}
		}

		return new Matrix(inverseMatrix);
	}

	/**
	 * Calculate the length of a vector.
	 * 
	 * @param v
	 *            vector
	 * @return Vector length.
	 */
	public static double vectorLength(final Matrix input) {
		if (!input.isVector()) {
			throw new MatrixError("Can only take the vector length of a vector.");
		}
		final Double v[] = input.toPackedArray();
		double rtn = 0.0;
		for (int i = 0; i < v.length; i++) {
			rtn += Math.pow(v[i], 2);
		}
		return Math.sqrt(rtn);
	}

}
