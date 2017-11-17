package com.nn.core;

import com.nn.core.neuron.Neuron;

public class ConnectionCreator {

	public static void createConnection(Neuron inNeuron, Neuron outNeuron) {
		Connection connection = new Connection(inNeuron, outNeuron);
		outNeuron.addInputConnection(connection);
	}

	public static void createConnection(Neuron inNeuron, Neuron outNeuron, double weightVal) {
		Connection connection = new Connection(inNeuron, outNeuron, weightVal);
		outNeuron.addInputConnection(connection);
	}

	public static void fullConnect(Layer inLayer, Layer outLayer) {
		for (Neuron inNeuron : inLayer.getNeurons()) {
			for (Neuron outNeuron : outLayer.getNeurons()) {
				createConnection(inNeuron, outNeuron);
			}
		}
	}

}
