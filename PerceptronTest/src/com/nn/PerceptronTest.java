package com.nn;

import java.util.Random;

public class PerceptronTest {

	private static final Random r = new Random();

	public static void main(String[] args) {
		System.out.println("Neural Network Test Running: ");

		Neuron perceptron = new Neuron(2);

		double[] point = { 0, 2 };

		int result = perceptron.feedForward(point);
		System.out.println("un-trained result: " + result);

		System.out.println("Generating trainers...");
		Trainer[] trainers = new Trainer[2000];
		for (int i = 0; i < trainers.length; i++) {
			trainers[i] = trainer();
		}

		System.out.println("starting training...");
		for (int i = 0; i < trainers.length; i++) {
			perceptron.train(trainers[i]);
		}

		int trainedResult = perceptron.feedForward(point);
		System.out.println("trained result: " + trainedResult);
	}

	private static int width = 640, height = 360;

	private static Trainer trainer() {
		double x = r.nextDouble() * width - (width / 2);
		double y = r.nextDouble() * height - (height / 2);

		int answer;
		double yline = 2 * x + 1;
		if (y < yline) {
			answer = -1;
		} else {
			answer = 1;
		}

		return new Trainer(x, y, answer);
	}

}
