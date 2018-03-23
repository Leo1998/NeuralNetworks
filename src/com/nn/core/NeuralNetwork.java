package com.nn.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nn.core.functions.TransferFunctionType;
import com.nn.utils.Matrix;
import com.nn.utils.MatrixMath;
import com.nn.utils.StreamUtils;

public class NeuralNetwork extends AbstractNeuralNetwork {

	private final Matrix[] neurons;
	private final Matrix[] weights;

	public NeuralNetwork(int[] shape, TransferFunctionType transferFunctionType) {
		super(shape, transferFunctionType);

		this.neurons = new Matrix[layerCount];
		for (int l = 0; l < layerCount; l++) {
			boolean bias = l < layerCount - 1;
			neurons[l] = new Matrix(bias ? shape[l] + 1 : shape[l], 1);

			if (bias)
				neurons[l].set(shape[l], 0, 1);
		}

		this.weights = new Matrix[layerCount - 1];
		for (int l = 0; l < layerCount - 1; l++) {
			weights[l] = new Matrix(countNeurons(l) + 1, countNeurons(l + 1));
		}
	}

	public void randomizeWeights(double r) {
		for (int i = 0; i < layerCount - 1; i++) {
			weights[i].randomize(-r, r);
		}
	}

	@Override
	public int countNeurons(int layer) {
		return shape[layer];
	}

	public double getNeuron(int l, int n) {
		return neurons[l].get(n, 0);
	}

	public double getBias(int l, int n) {
		return weights[l].get(countNeurons(l), n);
	}

	public void setBias(int l, int n, double bias) {
		weights[l].set(countNeurons(l), n, bias);
	}

	/**
	 * 
	 * @param l1
	 *            the layer from which the connection is coming
	 * @param n1
	 *            the neuron index from which the connection is coming
	 * @param n2
	 *            the neuron index in the next layer to which the connection is
	 *            going
	 * @return the weight
	 */
	public double getWeight(int l1, int n1, int n2) {
		return weights[l1].get(n1, n2);
	}

	/**
	 * 
	 * @param l1
	 *            the layer from which the connection is coming
	 * @param n1
	 *            the neuron index from which the connection is coming
	 * @param n2
	 *            the neuron index in the next layer to which the connection is
	 *            going
	 * @param the
	 *            weight
	 */
	public void setWeight(int l1, int n1, int n2, double weight) {
		weights[l1].set(n1, n2, weight);
	}

	public Matrix getWeights(int l) {
		return weights[l];
	}

	public void setWeights(int l, Matrix m) {
		weights[l] = m;
	}

	@Override
	public double[] compute(double[] input) {
		assert (input.length == getInputCount());

		for (int i = 0; i < getInputCount(); i++) {
			this.neurons[0].set(i, 0, input[i]);
		}

		for (int l = 0; l < layerCount - 1; l++) {
			Matrix in = this.neurons[l];

			for (int n = 0; n < countNeurons(l + 1); n++) {
				final Matrix col = this.weights[l].getCol(n);
				final double sum = MatrixMath.dotProduct(col, in);

				this.neurons[l + 1].set(n, 0, transferFunction.getOutput(sum));
			}
		}

		double[] output = new double[getOutputCount()];
		for (int i = 0; i < getOutputCount(); i++) {
			output[i] = this.neurons[getLayerCount() - 1].get(i, 0);
		}

		return output;
	}
	
	public void load(File file) {
		try {
			FileInputStream f = new FileInputStream(file);
			this.load(new DataInputStream(f));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void load(DataInputStream stream) {
		try {
			for (int l = 0; l < getLayerCount() - 1; l++) {
				Double[] packed = new Double[getWeights(l).size()];

				for (int i = 0; i < packed.length; i++) {
					packed[i] = stream.readDouble();
				}
				
				getWeights(l).fromPackedArray(packed, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtils.closeQuiet(stream);
		}
	}
	
	public void save(File file) {
		try {
			FileOutputStream f = new FileOutputStream(file);
			this.save(new DataOutputStream(f));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void save(DataOutputStream stream) {
		try {
			for (int l = 0; l < getLayerCount() - 1; l++) {
				Double[] packed = getWeights(l).toPackedArray();

				for (Double d : packed) {
					stream.writeDouble(d);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtils.closeQuiet(stream);
		}
	}

}
