package com.nn.core.training;

import com.nn.core.NeuralNetwork;
import com.nn.utils.Matrix;
import com.nn.utils.MatrixMath;

public class Trainer {

	private NeuralNetwork nn;

	private final Matrix[] errorMatrix;
	private final Matrix[] errorDeltaMatrix;
	private final Matrix[] accDeltaMatrix;
	private final Matrix[] previousDeltaMatrix;

	public Trainer(NeuralNetwork nn) {
		this.nn = nn;

		this.errorMatrix = new Matrix[nn.getLayerCount()];
		for (int l = 0; l < nn.getLayerCount(); l++) {
			this.errorMatrix[l] = new Matrix(nn.countNeurons(l), 1);
		}

		this.errorDeltaMatrix = new Matrix[nn.getLayerCount()];
		for (int l = 0; l < nn.getLayerCount(); l++) {
			this.errorDeltaMatrix[l] = new Matrix(nn.countNeurons(l), 1);
		}

		this.accDeltaMatrix = new Matrix[nn.getLayerCount() - 1];
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			this.accDeltaMatrix[l] = new Matrix(nn.countNeurons(l) + 1, nn.countNeurons(l + 1));
		}

		this.previousDeltaMatrix = new Matrix[nn.getLayerCount() - 1];
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			this.previousDeltaMatrix[l] = new Matrix(nn.countNeurons(l) + 1, nn.countNeurons(l + 1));
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

		learn(learningRate, momentum);
	}

	private void learn(double learningRate, double momentum) {
		for (int l = 0; l < nn.getLayerCount() - 1; l++) {
			final Matrix m1 = MatrixMath.multiply(this.accDeltaMatrix[l], learningRate);
			final Matrix m2 = MatrixMath.multiply(this.previousDeltaMatrix[l], momentum);
			this.previousDeltaMatrix[l] = MatrixMath.add(m1, m2);

			nn.setWeights(l, MatrixMath.add(nn.getWeights(l), this.previousDeltaMatrix[l]));

			this.accDeltaMatrix[l].clear();
		}
	}

	private void clearErrors() {
		for (int l = 0; l < nn.getLayerCount(); l++) {
			errorMatrix[l].clear();
			errorDeltaMatrix[l].clear();
		}
	}

	private void calcError(double[] output, double[] desiredOutput) {
		for (int l = nn.getLayerCount() - 1; l >= 0; l--) {
			int next = l + 1;
			for (int i = 0; i < nn.countNeurons(l); i++) {
				if (l == nn.getLayerCount() - 1) {
					errorMatrix[l].set(i, 0, desiredOutput[i] - output[i]);
				} else {
					for (int j = 0; j < nn.countNeurons(next); j++) {
						accumulateDelta(l, i, j, errorDeltaMatrix[next].get(j, 0) * nn.getNeuron(l, i));

						errorMatrix[l].add(i, 0, nn.getWeight(l, i, j) * errorDeltaMatrix[next].get(j, 0));
					}
				}
				errorDeltaMatrix[l].set(i, 0,
						errorMatrix[l].get(i, 0) * nn.getTransferFunction().getDerivative(nn.getNeuron(l, i)));
			}

			if (l < nn.getLayerCount() - 1) {
				for (int j = 0; j < nn.countNeurons(next); j++) {
					accumulateBiasDelta(l, j, errorDeltaMatrix[next].get(j, 0));
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
	 * @param
	 */
	private void accumulateDelta(int l1, int n1, int n2, double value) {
		accDeltaMatrix[l1].add(n1, n2, value);
	}

	private void accumulateBiasDelta(int l1, int n, double value) {
		accDeltaMatrix[l1].add(nn.countNeurons(l1), n, value);
	}

}
