package com.nn;

import java.util.Random;

public class Neuron {

	private static final Random r = new Random();

	private double[] weights;
	
	/** The Learning Constant **/
	private final double c = 0.01;

	public Neuron(int n) {
		this.weights = new double[n];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = r.nextFloat() * 2 - 1;
		}
	}

	public void train(Trainer trainer) {
		train(trainer.inputs, trainer.answer);
	}
	
	private void train(double[] inputs, int desired) {		
		int guess = feedForward(inputs);

		float error = desired - guess;

		for (int i = 0; i < weights.length; i++) {
			weights[i] += c * error * inputs[i];
		}
	}

	public int feedForward(double[] input) {
		double sum = 0;

		assert (weights.length == input.length);
		int length = weights.length;

		for (int i = 0; i < length; i++) {
			sum += weights[i] * input[i];
		}

		int output = activate(sum);

		return output;
	}

	private int activate(double sum) {
		if (sum > 0)
			return 1;
		else
			return -1;
	}

}
