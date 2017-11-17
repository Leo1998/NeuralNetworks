package com.nn.test;

import java.util.ArrayList;
import java.util.List;

import com.nn.core.NeuralNetwork;
import com.nn.core.functional.transfer.TransferFunctionType;
import com.nn.core.nnet.MultiLayerPerceptron;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;
import com.nn.debug.DebugWindow;

public class TrainingTest {

	public static void main(String[] args) {
		List<Integer> neuronsInLayers = new ArrayList<>();
		neuronsInLayers.add(3);
		neuronsInLayers.add(10);
		neuronsInLayers.add(1);

		NeuralNetwork nn = new MultiLayerPerceptron(neuronsInLayers, TransferFunctionType.Sigmoid, true);

		DebugWindow debugWindow = new DebugWindow(nn);

		calcOutput(nn, 1.0, 1.0, 1.0);
		calcOutput(nn, 2.0, 1.0, 2.0);

		debugWindow.waitForEvent();

		Sample[] samples = new Sample[25000];
		for (int j = 0; j < samples.length; j++) {
			if (Math.random() > 0.5) {
				samples[j] = new Sample(new double[] { 1.0, 1.0, 1.0 }, new double[] { 0.5454545454 });
			} else {
				samples[j] = new Sample(new double[] { 2.0, 1.0, 2.0 }, new double[] { 0.6666666666 });
			}
		}
		Lesson lesson = new Lesson(samples);

		nn.train(lesson, 0.1);

		calcOutput(nn, 1.0, 1.0, 1.0);
		calcOutput(nn, 2.0, 1.0, 2.0);

		debugWindow.refresh();
	}

	private static void calcOutput(NeuralNetwork nn, double... input) {
		nn.setInput(input);
		nn.calculate();
		double[] output = nn.getOutput();

		System.out.printf("Inputs: ");
		for (int i = 0; i < input.length; i++) {
			System.out.printf(input[i] + "   ");
		}
		System.out.println();

		System.out.printf("Outputs: ");
		for (int i = 0; i < output.length; i++) {
			System.out.printf(output[i] + "   ");
		}
		System.out.println();
	}

}
