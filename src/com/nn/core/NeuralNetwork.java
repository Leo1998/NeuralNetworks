package com.nn.core;

import com.nn.core.functions.TransferFunctionType;
import com.nn.utils.Matrix;
import com.nn.utils.MatrixMath;

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

	/*
	 * public void trainBackpropagation(Sample sample, double learningRate) {
	 * double[] input = sample.getInput(); double[] desiredOutput =
	 * sample.getDesiredOutput();
	 * 
	 * this.predict(input);
	 * 
	 * // iterate back for (int l = getLayerCount() - 1; l > 0; l--) { for (int
	 * i = 0; i < countNeurons(l); i++) { double neuron = getNeuron(l, i);
	 * 
	 * for (int j = 0; j < countNeurons(l - 1); j++) { double inNeuron =
	 * getNeuron(l - 1, j); double connWeight = getWeight(l - 1, j, i);
	 * 
	 * double netInput = inNeuron * connWeight;
	 * 
	 * double delta; if (l == getLayerCount() - 1) { delta =
	 * transferFunction.getDerivative(netInput) * (desiredOutput[i] - neuron); }
	 * else { double sum = 0;
	 * 
	 * for (int k = 0; k < countNeurons(l + 1); k++) { double outConnWeight =
	 * getWeight(l, i, k);
	 * 
	 * sum += outConnWeight * getError(l, i, k); }
	 * 
	 * delta = transferFunction.getDerivative(netInput) * sum; }
	 * 
	 * double deltaWeight = learningRate * delta * inNeuron; double newWeight =
	 * connWeight + deltaWeight; setWeight(l - 1, j, i, newWeight);
	 * 
	 * setError(l - 1, j, i, delta); }
	 * 
	 * if (biases != null) { double bias = getBias(l, i);
	 * 
	 * double delta; if (l == getLayerCount() - 1) { delta =
	 * transferFunction.getDerivative(bias) * (desiredOutput[i] - neuron); }
	 * else { double sum = 0;
	 * 
	 * for (int k = 0; k < countNeurons(l + 1); k++) { double outConnWeight =
	 * getWeight(l, i, k);
	 * 
	 * sum += outConnWeight * getError(l, i, k); }
	 * 
	 * delta = transferFunction.getDerivative(bias) * sum; }
	 * 
	 * 
	 * double deltaBias = learningRate * delta; double newBias = bias +
	 * deltaBias; setBias(l, i, newBias); } } } }
	 */

}
