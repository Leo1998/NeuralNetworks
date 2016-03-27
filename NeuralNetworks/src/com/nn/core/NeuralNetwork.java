package com.nn.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.nn.core.neuronbehavior.NeuronBehavior;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;

public class NeuralNetwork {

	private final NeuralNetworkDescriptor descriptor;

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
			NeuronBehavior behavior = descriptor.getHiddenBehavior();
			if (i == 0) {
				behavior = descriptor.getInputBehavior();
			} else if (i == layerCount - 1) {
				behavior = descriptor.getOutputBehavior();
			}

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

	public double[] propagate(double[] input) {
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

	public void train(Lesson lesson, double learningRate) {
		long startTime = System.currentTimeMillis();

		for (Sample sample : lesson) {
			trainBackpropagation(sample, learningRate);
		}
		
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("NeuralNetwork: Training took " + time + " ms");
	}

	private void trainBackpropagation(Sample sample, double learningRate) {
		double[] input = sample.getInput();
		double[] desiredOutput = sample.getDesiredOutput();

		this.propagate(input);

		Map<Connection, Double> errorSignalMap = new HashMap<Connection, Double>();

		for (int l = countLayers() - 1; l > 0; l--) {
			Layer layer = layers.get(l);
			Map<Connection, Double> lastErrorSignalMap = new HashMap<Connection, Double>();
			lastErrorSignalMap.putAll(errorSignalMap);
			errorSignalMap.clear();

			for (int i = 0; i < layer.countNeurons(); i++) {
				Neuron n = layer.getNeuron(i);
				double out = n.getOutput();

				for (Connection inConn : n.getInputConnections()) {
					double inNeuronOut = inConn.getInNeuron().getOutput();
					double netInput = inNeuronOut * inConn.getWeight();

					double errorSignal;
					if (layer == outputLayer) {
						errorSignal = n.getBehavior().computeDerivative(netInput) * (desiredOutput[i] - out);
					} else {
						double sum = 0;

						for (Connection outConn : n.getOutputConnections()) {
							double outConnWeight = outConn.getWeight();

							sum += outConnWeight * lastErrorSignalMap.get(outConn);
						}

						errorSignal = n.getBehavior().computeDerivative(netInput) * sum;
					}

					double deltaWeight = learningRate * errorSignal * inNeuronOut;
					double newWeight = inConn.getWeight() + deltaWeight;
					inConn.setWeight(newWeight);

					errorSignalMap.put(inConn, errorSignal);
				}
			}
		}
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

	public List<Layer> getLayers() {
		return Collections.unmodifiableList(layers);
	}

	public Layer getInputLayer() {
		return inputLayer;
	}

	public Layer getOutputLayer() {
		return outputLayer;
	}

	public List<Neuron> getAllNeurons() {
		List<Neuron> all = new LinkedList<Neuron>();

		for (Layer layer : layers) {
			all.addAll(layer.getNeurons());
		}

		return all;
	}

}
