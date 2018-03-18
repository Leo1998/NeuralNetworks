package com.nn.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nn.core.neuron.BiasNeuron;
import com.nn.core.neuron.Neuron;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;

public abstract class NeuralNetwork {

	protected List<Layer> layers;

	protected List<Neuron> inputNeurons;
	protected List<Neuron> outputNeurons;

	public NeuralNetwork() {
		this.layers = new ArrayList<>();
	}

	protected void setDefaultIO() {
		this.inputNeurons = new ArrayList<>();

		Layer firstLayer = getLayerAt(0);
		for (Neuron neuron : firstLayer.getNeurons()) {
			if (!(neuron instanceof BiasNeuron)) {
				inputNeurons.add(neuron);
			}
		}

		outputNeurons = ((Layer) getLayerAt(getLayerCount() - 1)).getNeurons();
	}

	public void setInput(double... inputVector) {
		if (inputVector.length != inputNeurons.size()) {
			throw new IllegalArgumentException("Input vector size does not match network input dimension!");
		}

		int i = 0;
		for (Neuron neuron : this.inputNeurons) {
			neuron.setInput(inputVector[i]);
			i++;
		}
	}

	public double[] calculateOutput() {
		for (Layer layer : this.layers) {
			layer.calculate();
		}
		
		double[] outputBuffer = new double[outputNeurons.size()];

		int i = 0;
		for (Neuron c : outputNeurons) {
			outputBuffer[i] = c.getOutput();
			i++;
		}

		return outputBuffer;
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

		this.setInput(input);
		this.calculateOutput();

		Map<Connection, Double> errorSignalMap = new HashMap<Connection, Double>();

		for (int l = getLayerCount() - 1; l > 0; l--) {
			Layer layer = layers.get(l);
			Map<Connection, Double> lastErrorSignalMap = new HashMap<Connection, Double>();
			lastErrorSignalMap.putAll(errorSignalMap);
			errorSignalMap.clear();

			for (int i = 0; i < layer.getNeuronCount(); i++) {
				Neuron n = layer.getNeuron(i);
				double out = n.getOutput();

				for (Connection inConn : n.getInputConnections()) {
					double inNeuronOut = inConn.getInNeuron().getOutput();
					double netInput = inNeuronOut * inConn.getWeight().getValue();

					double errorSignal;
					if (layer == getLayerAt(getLayerCount() - 1)) {
						errorSignal = n.getTransferFunction().getDerivative(netInput) * (desiredOutput[i] - out);
					} else {
						double sum = 0;

						for (Connection outConn : n.getOutputConnections()) {
							double outConnWeight = outConn.getWeight().getValue();

							sum += outConnWeight * lastErrorSignalMap.get(outConn);
						}

						errorSignal = n.getTransferFunction().getDerivative(netInput) * sum;
					}

					double deltaWeight = learningRate * errorSignal * inNeuronOut;
					double newWeight = inConn.getWeight().getValue() + deltaWeight;
					inConn.getWeight().setValue(newWeight);

					errorSignalMap.put(inConn, errorSignal);
				}
			}
		}
	}

	public int getLayerCount() {
		return this.layers.size();
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
	}

	public void addLayer(Layer layer, int i) {
		layers.add(i, layer);
	}

	public List<Layer> getLayers() {
		return Collections.unmodifiableList(layers);
	}

	public Layer getLayerAt(int i) {
		return layers.get(i);
	}

	public List<Neuron> getInputNeurons() {
		return inputNeurons;
	}

	public List<Neuron> getOutputNeurons() {
		return outputNeurons;
	}

}
