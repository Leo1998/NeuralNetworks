package com.nn.core.nnet;

import com.nn.core.ConnectionCreator;
import com.nn.core.Layer;
import com.nn.core.NeuralNetwork;
import com.nn.core.functional.input.WeightedSum;
import com.nn.core.neuron.InputNeuron;
import com.nn.core.neuron.InputOutputNeuron;
import com.nn.core.neuron.Neuron;
import com.nn2.core.functions.TransferFunctionType;

public class Perceptron extends NeuralNetwork {

	public Perceptron(int inputNeuronsCount, int outputNeuronsCount) {
		this(inputNeuronsCount, outputNeuronsCount, TransferFunctionType.Step);
	}

	public Perceptron(int inputNeuronsCount, int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		super();

		this.createNetwork(inputNeuronsCount, outputNeuronsCount, transferFunctionType);
	}

	private void createNetwork(int inputNeuronsCount, int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		Layer inputLayer = new Layer();
		for (int i = 0; i < inputNeuronsCount; i++) {
			Neuron neuron = new InputNeuron();

			inputLayer.addNeuron(neuron);
		}
		addLayer(inputLayer, 0);

		Layer outputLayer = new Layer();
		for (int i = 0; i < outputNeuronsCount; i++) {
			Neuron neuron = new InputOutputNeuron(new WeightedSum(), transferFunctionType.instance());

			outputLayer.addNeuron(neuron);
		}
		addLayer(outputLayer, 1);

		ConnectionCreator.fullConnect(inputLayer, outputLayer);

		setDefaultIO();
	}

}
