package com.nn.core.training;

import com.nn.core.NeuralNetwork;

public class Trainer {

	private NeuralNetwork nn;

	private final double[][] errorMatrix;
	private final double[][] errorDeltaMatrix;
	private final double[][] accDeltaMatrix;
	private final double[][] accBiasDeltaMatrix;

	public Trainer(NeuralNetwork nn) {
		this.nn = nn;

		this.errorMatrix = new double[nn.getLayerCount()][];
		for (int l = 0; l < nn.getLayerCount(); l++) {
			errorMatrix[l] = new double[nn.countNeurons(l)];
		}

		this.errorDeltaMatrix = new double[nn.getLayerCount()][];
		for (int l = 0; l < nn.getLayerCount(); l++) {
			errorDeltaMatrix[l] = new double[nn.countNeurons(l)];
		}

		this.accDeltaMatrix = new double[nn.getLayerCount() - 1][];
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			accDeltaMatrix[l] = new double[nn.countNeurons(l) * nn.countNeurons(l + 1)];
		}

		if (nn.hasBiases()) {
			this.accBiasDeltaMatrix = new double[nn.getLayerCount() - 1][];
			for (int l = 1; l < nn.getLayerCount(); l++) {
				accBiasDeltaMatrix[l - 1] = new double[nn.countNeurons(l)];
			}
		} else {
			this.accBiasDeltaMatrix = null;
		}
	}

	public double calcError(Lesson lesson) {
		double totalError = 0.0;

		for (Sample sample : lesson) {
			double[] output = nn.compute(sample.getInput());
			for (int i = 0; i < output.length; i++) {
				double delta = output[i] - sample.getDesiredOutput()[i];
				totalError += (delta * delta);
			}
		}

		return Math.sqrt(totalError / (lesson.countSamples() * lesson.getSample(0).getDesiredOutput().length));
	}

	public void train(Lesson lesson, int epochs, double learningRate, double momentum) {
		double errorBefore = calcError(lesson);
		System.out.println("Starting Training... (Error before: " + errorBefore + ")");
		
		for (int i = 0; i < epochs; i++) {
			trainEpoch(lesson, learningRate, momentum);
		}
		
		double errorAfter = calcError(lesson);
		System.out.println("Training finished!!! (Error after: " + errorAfter + ")");
	}
	
	private void trainEpoch(Lesson lesson, double learningRate, double momentum) {
		clearErrors();

		for (Sample sample : lesson) {
			double[] output = nn.compute(sample.getInput());
			calcError(output, sample.getDesiredOutput());
		}

		learn(learningRate);
	}

	private void learn(double learningRate) {
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			for (int j = 0; j < nn.countNeurons(l + 1); j++) {
				for (int i = 0; i < nn.countNeurons(l); i++) {
					double delta = getAccumulatedDelta(l, i, j);

					nn.setWeight(l, i, j, nn.getWeight(l, i, j) + (learningRate * delta));
				}

				if (nn.hasBiases()) {
					double delta = getAccumulatedBiasDelta(l + 1, j);
					nn.setBias(l + 1, j, nn.getBias(l + 1, j) + (learningRate * delta));
				}
			}
		}

		clearAccumulatedDeltas();
	}

	private void clearErrors() {
		for (int l = 0; l < nn.getLayerCount(); l++) {
			for (int n = 0; n < nn.countNeurons(l); n++) {
				errorMatrix[l][n] = 0.0;
				errorDeltaMatrix[l][n] = 0.0;
			}
		}
	}

	private void calcError(double[] output, double[] desiredOutput) {
		for (int l = nn.getLayerCount() - 1; l >= 0; l--) {
			int next = l + 1;
			for (int i = 0; i < nn.countNeurons(l); i++) {
				if (l == nn.getLayerCount() - 1) {
					errorMatrix[l][i] = desiredOutput[i] - output[i];
				} else {
					for (int j = 0; j < nn.countNeurons(next); j++) {
						accumulateDelta(l, i, j, errorDeltaMatrix[next][j] * nn.getNeuron(l, i));

						errorMatrix[l][i] = errorMatrix[l][i] + nn.getWeight(l, i, j) * errorDeltaMatrix[next][j];
					}
				}
				errorDeltaMatrix[l][i] = errorMatrix[l][i] * nn.getTransferFunction().getDerivative(nn.getNeuron(l, i));
			}

			if (nn.hasBiases() && l < nn.getLayerCount() - 1) {
				for (int j = 0; j < nn.countNeurons(next); j++) {
					accumulateBiasDelta(next, j, errorDeltaMatrix[next][j]);
				}
			}
		}
	}

	private void clearAccumulatedDeltas() {
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			for (int n = 0; n < accDeltaMatrix[l].length; n++) {
				accDeltaMatrix[l][n] = 0.0;
			}
			if (nn.hasBiases()) {
				for (int n = 0; n < accBiasDeltaMatrix[l].length; n++) {
					accBiasDeltaMatrix[l][n] = 0.0;
				}
			}
		}
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
	 * @return
	 */
	private double getAccumulatedDelta(int l1, int n1, int n2) {
		return accDeltaMatrix[l1][n1 + n2 * nn.countNeurons(l1)];
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
	 * @param
	 */
	private void accumulateDelta(int l1, int n1, int n2, double value) {
		accDeltaMatrix[l1][n1 + n2 * nn.countNeurons(l1)] += value;
	}

	private double getAccumulatedBiasDelta(int l, int n) {
		return accBiasDeltaMatrix[l - 1][n];
	}

	private void accumulateBiasDelta(int l, int n, double value) {
		accBiasDeltaMatrix[l - 1][n] += value;
	}

}
