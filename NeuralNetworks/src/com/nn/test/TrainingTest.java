package com.nn.test;

import com.nn.core.NeuralNetwork;
import com.nn.core.NeuralNetworkDescriptor;
import com.nn.core.neuronbehavior.Identity;
import com.nn.core.neuronbehavior.NeuronBehavior;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;
import com.nn.debug.DebugWindow;

public class TrainingTest {

	public static void main(String[] args) {
		int[] neuronsPerLayer = new int[] { 1, 2, 1 };
		NeuronBehavior[] behaviors = new NeuronBehavior[] { new Identity(), new Identity(), new Identity() };
		NeuralNetworkDescriptor desc = new NeuralNetworkDescriptor(neuronsPerLayer, behaviors);
		desc.setFullyConnect(true);

		NeuralNetwork nn = new NeuralNetwork(desc);

		DebugWindow debugWindow = new DebugWindow(nn);

		calcOutput(nn, 1.0);

		for (int i = 0; i < 100; i++) {
			debugWindow.waitForEvent();

			Sample[] samples = new Sample[1];
			for (int j = 0; j < samples.length; j++) {
				double num = Math.random() * 10;
				double res = num;

				samples[j] = new Sample(new double[] { num }, new double[] { res });
			}
			Lesson lesson = new Lesson(samples);

			nn.train(lesson, 0.1);
			calcOutput(nn, 1.0);

			debugWindow.refresh();
		}
	}

	private static void calcOutput(NeuralNetwork nn, double in) {
		double[] input = new double[] { in };

		double[] output = nn.propagate(input);

		System.out.println("in: " + in + "  output: " + output[0]);
	}

}
