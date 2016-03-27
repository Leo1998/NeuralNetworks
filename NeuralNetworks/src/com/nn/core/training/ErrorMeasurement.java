package com.nn.core.training;

import com.nn.core.NeuralNetwork;

public class ErrorMeasurement {

	private ErrorMeasurement() {
	}

	public static double getErrorSum(NeuralNetwork nn, Sample sample) {
		double[] input = sample.getInput();
		double[] desiredOutput = sample.getDesiredOutput();

		double error = 0.0D;

		double[] networkOutput = nn.propagate(input);
		for (int i = 0; i < networkOutput.length; i++) {
			double v = 0.0D;
			v = desiredOutput[i] - networkOutput[i];
			error += v;
		}

		return error;
	}

}
