package com.nn.test;

import com.nn.core.NeuralNetwork;
import com.nn.core.NeuralNetworkDescriptor;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;
import com.nn.debug.DebugWindow;

public class TrainingTest {

	public static void main(String[] args) {
		int[] neuronsPerLayer = new int[] { 2, 6, 2 };
		NeuralNetworkDescriptor desc = new NeuralNetworkDescriptor(neuronsPerLayer);
		desc.setFullyConnect(true);

		NeuralNetwork nn = new NeuralNetwork(desc);

		DebugWindow debugWindow = new DebugWindow(nn);
		debugWindow.waitForEvent();

		Sample[] samples = new Sample[10000];
		for (int j = 0; j < samples.length; j++) {
			samples[j] = new Sample(new double[] { 1.0, 5.0 }, new double[] { 0.8, 0.45 });
		}
		Lesson lesson = new Lesson(samples);

		nn.train(lesson, 0.01);
		calcOutput(nn, 1.0, 5.0);

		debugWindow.refresh();
	}

	private static void calcOutput(NeuralNetwork nn, double x, double y) {
		double[] input = new double[] { x, y };

		double[] output = nn.propagate(input);

		for (int i = 0; i < input.length; i++) {
			System.out.println("in[" + i + "]: " + input[i]);
		}

		for (int i = 0; i < output.length; i++) {
			System.out.println("out[" + i + "]: " + output[i]);
		}
	}

}
