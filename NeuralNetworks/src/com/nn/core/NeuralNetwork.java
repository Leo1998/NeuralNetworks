package com.nn.core;

import java.util.LinkedList;
import java.util.List;

import com.nn.core.neuronbehavior.NeuronBehavior;

public class NeuralNetwork {

	private final NeuralNetworkDescriptor descriptor;

	private double learningRate = 0.01;

	private List<Layer> layers;
	private Layer inputLayer;
	private Layer outputLayer;

	public NeuralNetwork(NeuralNetworkDescriptor descriptor) {
		this.descriptor = descriptor;
		this.layers = new LinkedList<Layer>();

		initialize();
	}

	public NeuralNetworkDescriptor getDescriptor() {
		return descriptor;
	}

	private void initialize() {
		int layerCount = descriptor.countLayers();

		for (int i = 0; i < layerCount; i++) {
			int c = descriptor.getNeuronsPerLayer(i);
			NeuronBehavior behavior = descriptor.getNeuronBehavior(i);

			Layer layer = new Layer(c, behavior);
			layers.add(layer);

			if (i == 0) {
				inputLayer = layer;
			} else if (i == layerCount - 1) {
				outputLayer = layer;
			}
		}
		assert (layers.size() == layerCount);

		if (descriptor.isFullyConnect()) {
			for (int i = 0; i < layers.size() - 1; i++) {
				Layer current = layers.get(i);
				Layer next = layers.get(i + 1);

				current.fullyConnectTo(next);
			}
		}
	}

	public double[] computeOutput(double[] input) {
		assert (input.length == inputLayer.countNeurons());

		inputLayer.setNeuronOutputs(input);

		double[] result = outputLayer.fireAll();

		for (Layer layer : layers) {
			for (Neuron n : layer.getNeurons()) {
				n.resetFiredFlag();
			}
		}

		return result;
	}

	public int countLayers() {
		return this.layers.size();
	}

	public int countInputNeurons() {
		return this.inputLayer.countNeurons();
	}

	public int countOutputNeurons() {
		return this.outputLayer.countNeurons();
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public List<Layer> getLayers() {
		return layers;
	}

	public Layer getInputLayer() {
		return inputLayer;
	}

	public Layer getOutputLayer() {
		return outputLayer;
	}

}
