package com.nn2.core;

import java.util.Random;

import com.nn2.core.functions.TransferFunctionType;

public class NeuralNetwork extends AbstractNeuralNetwork {

	private final double[][] neurons;
	private final double[][] weightMatrix;

	public NeuralNetwork(int[] shape, TransferFunctionType transferFunctionType) {
		super(shape, transferFunctionType);

		this.neurons = new double[layerCount][];
		for (int i = 0; i < layerCount; i++) {
			neurons[i] = new double[shape[i]];
		}

		this.weightMatrix = new double[layerCount - 1][];
		for (int i = 0; i < layerCount - 1; i++) {
			weightMatrix[i] = new double[countNeurons(i) * countNeurons(i + 1)];
		}
	}

	public void randomizeWeights() {
		Random random = new Random();

		for (int l = 0; l < layerCount - 1; l++) {
			for (int n = 0; n < countNeurons(l); n++) {
				for (int n1 = 0; n1 < countNeurons(l + 1); n1++) {
					setWeight(l, n, n1, random.nextDouble());
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

	public double getWeight(int l1, int n1, int n2) {
		return weightMatrix[l1][n1 + n2 * countNeurons(l1)];
	}

	public void setWeight(int l1, int n1, int n2, double weight) {
		weightMatrix[l1][n1 + n2 * countNeurons(l1)] = weight;
	}

	@Override
	public double[] predict(double[] input) {
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

				neurons[l][n] = transferFunction.getOutput(weightedSum);
			}
		}

		double[] output = new double[getOutputCount()];
		System.arraycopy(neurons[layerCount - 1], 0, output, 0, getOutputCount());

		return output;
	}
	
	

}
