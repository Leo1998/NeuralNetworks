package com.nn.core.training;

public class Sample {

	private final double[] input;
	private final double[] desiredOutput;

	public Sample(double[] input, double[] desiredOutput) {
		this.input = input;
		this.desiredOutput = desiredOutput;

		if (input == null) {
			throw new IllegalArgumentException("Inputs null.");
		}
		if (desiredOutput == null) {
			throw new IllegalArgumentException("Desired Outputs null.");
		}
		if (input == desiredOutput) {
			throw new IllegalArgumentException("Inputs and Desired Outputs are the same array.");
		}
		if (input.length < 1) {
			throw new IllegalArgumentException("0 Inputs given.");
		}
		if (desiredOutput.length < 1) {
			throw new IllegalArgumentException("0 Desired Outputs given.");
		}
	}

	public double[] getInput() {
		return input;
	}

	public double[] getDesiredOutput() {
		return desiredOutput;
	}

}
