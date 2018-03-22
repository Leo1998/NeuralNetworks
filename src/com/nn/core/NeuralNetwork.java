package com.nn.core;

import java.util.Random;

import com.nn.core.functions.TransferFunctionType;

public class NeuralNetwork extends AbstractNeuralNetwork {

	private final double[][] neurons;
	private final double[][] weightMatrix;
	private final double[][] biases;

	public NeuralNetwork(int[] shape, TransferFunctionType transferFunctionType, boolean useBiases) {
		super(shape, transferFunctionType);

		this.neurons = new double[layerCount][];
		for (int l = 0; l < layerCount; l++) {
			neurons[l] = new double[shape[l]];
		}

		if (useBiases) {
			this.biases = new double[layerCount - 1][];
			for (int l = 1; l < layerCount; l++) {
				biases[l - 1] = new double[shape[l]];
			}
		} else {
			this.biases = null;
		}

		this.weightMatrix = new double[layerCount - 1][];
		for (int l = 0; l < layerCount - 1; l++) {
			weightMatrix[l] = new double[countNeurons(l) * countNeurons(l + 1)];
		}
	}

	public void randomizeWeights(double r) {
		Random random = new Random();

		for (int l = 0; l < layerCount - 1; l++) {
			for (int n = 0; n < countNeurons(l); n++) {
				for (int n1 = 0; n1 < countNeurons(l + 1); n1++) {
					setWeight(l, n, n1, random.nextDouble() * r * 2 - r);
				}

				if (l > 0 && biases != null) {
					biases[l - 1][n] = random.nextDouble() * r * 2 - r;
				}
			}
		}
	}

	@Override
	public int countNeurons(int layer) {
		return shape[layer];
	}

	public double getNeuron(int l, int n) {
		return neurons[l][n];
	}
	
	public boolean hasBiases() {
		return biases != null;
	}
	
	public double getBias(int l, int n) {
		if (biases == null) {
			throw new IllegalStateException("NeuralNetwork is not configured for biases!");
		}
		if (l == 0) {
			throw new IllegalStateException("Input layer has no biases!");
		}
		return biases[l - 1][n];
	}
	
	public void setBias(int l, int n, double bias) {
		if (biases == null) {
			throw new IllegalStateException("NeuralNetwork is not configured for biases!");
		}
		if (l == 0) {
			throw new IllegalStateException("Input layer has no biases!");
		}
		biases[l - 1][n] = bias;
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
		return weightMatrix[l1][n1 + n2 * countNeurons(l1)];
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
		weightMatrix[l1][n1 + n2 * countNeurons(l1)] = weight;
	}

	@Override
	public double[] compute(double[] input) {
		assert (input.length == getInputCount());

		System.arraycopy(input, 0, neurons[0], 0, getInputCount());

		for (int l = 1; l < layerCount; l++) {
			for (int n = 0; n < countNeurons(l); n++) {
				double weightedSum = 0;

				for (int n0 = 0; n0 < countNeurons(l - 1); n0++) {
					double weight = getWeight(l - 1, n0, n);

					if (weight != Double.NaN) {// check if connection exists
						weightedSum += getNeuron(l - 1, n0) * weight;
					}
				}
				
				if (biases != null) {
					weightedSum += getBias(l, n);
				}

				neurons[l][n] = transferFunction.getOutput(weightedSum);
			}
		}

		double[] output = new double[getOutputCount()];
		System.arraycopy(neurons[layerCount - 1], 0, output, 0, getOutputCount());

		return output;
	}
	
	
	/*
	 public void trainBackpropagation(Sample sample, double learningRate) {
		double[] input = sample.getInput();
		double[] desiredOutput = sample.getDesiredOutput();

		this.predict(input);

		// iterate back
		for (int l = getLayerCount() - 1; l > 0; l--) {
			for (int i = 0; i < countNeurons(l); i++) {
				double neuron = getNeuron(l, i);

				for (int j = 0; j < countNeurons(l - 1); j++) {
					double inNeuron = getNeuron(l - 1, j);
					double connWeight = getWeight(l - 1, j, i);

					double netInput = inNeuron * connWeight;

					double delta;
					if (l == getLayerCount() - 1) {
						delta = transferFunction.getDerivative(netInput) * (desiredOutput[i] - neuron);
					} else {
						double sum = 0;

						for (int k = 0; k < countNeurons(l + 1); k++) {
							double outConnWeight = getWeight(l, i, k);

							sum += outConnWeight * getError(l, i, k);
						}

						delta = transferFunction.getDerivative(netInput) * sum;
					}

					double deltaWeight = learningRate * delta * inNeuron;
					double newWeight = connWeight + deltaWeight;
					setWeight(l - 1, j, i, newWeight);

					setError(l - 1, j, i, delta);
				}
				
				if (biases != null) {
					double bias = getBias(l, i);
					
					double delta;
					if (l == getLayerCount() - 1) {
						delta = transferFunction.getDerivative(bias) * (desiredOutput[i] - neuron);
					} else {
						double sum = 0;

						for (int k = 0; k < countNeurons(l + 1); k++) {
							double outConnWeight = getWeight(l, i, k);

							sum += outConnWeight * getError(l, i, k);
						}

						delta = transferFunction.getDerivative(bias) * sum;
					}
					
					
					double deltaBias = learningRate * delta;
					double newBias = bias + deltaBias;
					setBias(l, i, newBias);
				}
			}
		}
	}
	 */

}
