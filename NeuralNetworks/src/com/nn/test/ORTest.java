package com.nn.test;

import com.nn.core.NeuralNetwork;
import com.nn.core.NeuralNetworkDescriptor;
import com.nn.core.neuronbehavior.IdentityBehavior;
import com.nn.core.neuronbehavior.NeuronBehavior;
import com.nn.core.neuronbehavior.ThresholdBehavior;

public class ORTest {

	public static void main(String[] args) {
		int[] neuronsPerLayer = new int[] { 2, 1 };
		NeuronBehavior[] behaviors = new NeuronBehavior[] { new IdentityBehavior(), new ThresholdBehavior(1.0) };
		NeuralNetworkDescriptor desc = new NeuralNetworkDescriptor(neuronsPerLayer, behaviors);
		desc.setFullyConnect(true);

		NeuralNetwork nn = new NeuralNetwork(desc);

		calcOR(nn, 0, 0);
		calcOR(nn, 1, 0);
		calcOR(nn, 0, 1);
		calcOR(nn, 1, 1);
	}

	private static void calcOR(NeuralNetwork nn, double x, double y) {
		double[] input = new double[] { x, y };

		double[] output = nn.computeOutput(input);

		System.out.println("x: " + x + "  y: " + y + "  output: " + output[0]);
	}

}
