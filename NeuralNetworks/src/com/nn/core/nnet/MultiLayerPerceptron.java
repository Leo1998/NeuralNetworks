package com.nn.core.nnet;

import java.util.List;

import com.nn.core.ConnectionCreator;
import com.nn.core.Layer;
import com.nn.core.NeuralNetwork;
import com.nn.core.functional.input.WeightedSum;
import com.nn.core.functional.transfer.TransferFunctionType;
import com.nn.core.neuron.BiasNeuron;
import com.nn.core.neuron.InputNeuron;
import com.nn.core.neuron.InputOutputNeuron;
import com.nn.core.neuron.Neuron;

public class MultiLayerPerceptron extends NeuralNetwork {

	public MultiLayerPerceptron(List<Integer> neuronsInLayers, TransferFunctionType transferFunction, boolean useBias) {
		super();

		this.createNetwork(neuronsInLayers, transferFunction, useBias);
	}

	private void createNetwork(List<Integer> neuronsInLayers, TransferFunctionType transferFunction, boolean useBias) {
		int layerCount = neuronsInLayers.size();

		for (int i = 0; i < layerCount; i++) {
			int count = neuronsInLayers.get(i);

			Layer layer = new Layer();
			for (int j = 0; j < count; j++) {
				Neuron neuron = null;
				if (i == 0) {
					neuron = new InputNeuron();
				} else {
					neuron = new InputOutputNeuron(new WeightedSum(), transferFunction.instance());
				}

				layer.addNeuron(neuron);
			}

			if (useBias && (i < (neuronsInLayers.size() - 1))) {
				layer.addNeuron(new BiasNeuron());
			}

			this.addLayer(layer, i);
		}
		assert (layers.size() == layerCount);

		for (int i = 0; i < layers.size() - 1; i++) {
			Layer current = layers.get(i);
			Layer next = layers.get(i + 1);

			ConnectionCreator.fullConnect(current, next);
		}

		setDefaultIO();
	}
}
