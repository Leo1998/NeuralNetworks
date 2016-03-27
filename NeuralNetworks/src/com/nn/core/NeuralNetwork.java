package com.nn.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nn.core.neuronbehavior.Identity;
import com.nn.core.neuronbehavior.NeuronBehavior;
import com.nn.core.neuronbehavior.TangensHyperbolicus;
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
		for (Sample sample : lesson) {
			trainBackpropagation(sample, learningRate);
		}
	}

	private void trainBackpropagation(Sample sample, double learningRate) {
		for (int i = 0; i < countLayers(); i++) {
			NeuronBehavior b = descriptor.getNeuronBehavior(i);
			assert(b instanceof TangensHyperbolicus || b instanceof Identity);
		}

		double[] input = sample.getInput();
		double[] desiredOutput = sample.getDesiredOutput();

		this.propagate(input);

		Layer lastLayer = outputLayer;
		double[] lastLayerErrors = new double[outputLayer.countNeurons()];

		// processing output layer
		for (int i = 0; i < outputLayer.countNeurons(); i++) {
			Neuron n = outputLayer.getNeuron(i);
			double out = n.getOutput();

			List<Connection> connections = n.getInputConnections();
			for (Connection c : connections) {
				double inNeuronOut = c.getInNeuron().getOutput();
				double desired = desiredOutput[i];

				double error = n.getBehavior().computeDerivative(out) * inNeuronOut * (desired - out);
				lastLayerErrors[i] = error;
				double deltaWeight = learningRate * error;

				double newWeight = c.getWeight() + deltaWeight;
				c.setWeight(newWeight);
			}
		}

		// processing hidden layers
		for (int i = countLayers() - 2; i > 0; i--) {
			Layer hiddenLayer = this.layers.get(i);

			for (int j = 0; j < hiddenLayer.countNeurons(); j++) {
				Neuron n = hiddenLayer.getNeuron(i);
				double out = n.getOutput();

				List<Connection> connections = n.getInputConnections();
				for (Connection c : connections) {
					double inNeuronOut = c.getInNeuron().getOutput();
					double sumOutputs = 0;

					for (int k = 0; k < lastLayer.countNeurons(); k++) {
						Neuron outNeuron = lastLayer.getNeuron(k);
						Connection outConn = outNeuron.findInputConnectionTo(n);
						double outConnWeight = outConn.getWeight();

						sumOutputs += outConnWeight * lastLayerErrors[k];
					}

					double error = n.getBehavior().computeDerivative(out) * inNeuronOut * sumOutputs;
					double deltaWeight = learningRate * error;

					double newWeight = c.getWeight() + deltaWeight;
					c.setWeight(newWeight);
				}
			}

			// update lastErrorMap
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
