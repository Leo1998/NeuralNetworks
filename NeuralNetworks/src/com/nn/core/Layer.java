package com.nn.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.nn.core.neuronbehavior.NeuronBehavior;

public class Layer implements Iterable<Neuron> {

	private List<Neuron> neurons;

	public Layer(List<Neuron> neurons) {
		this.neurons = neurons;
	}

	public Layer(int requiredNeurons, NeuronBehavior behavior) {
		this.neurons = new LinkedList<Neuron>();
		for (int i = 0; i < requiredNeurons; i++) {
			neurons.add(new Neuron(behavior));
		}
	}

	public void fullyConnectTo(Layer layer) {
		if (layer != null) {
			for (Neuron n1 : this.neurons) {
				for (Neuron n2 : layer.neurons) {
					Connection c = new Connection(n1, n2);
					n1.addConnection(c);
					n2.addConnection(c);
				}
			}
		}
	}

	public void setNeuronOutputs(double[] outputs) {
		assert (outputs.length == countNeurons());

		for (int i = 0; i < outputs.length; i++) {
			Neuron n = neurons.get(i);

			n.setOutput(outputs[i]);
		}
	}

	public double[] fireAll() {
		double[] values = new double[countNeurons()];

		for (int i = 0; i < countNeurons(); i++) {
			Neuron n = neurons.get(i);

			values[i] = n.fire();
		}

		return values;
	}

	public List<Neuron> getNeurons() {
		return Collections.unmodifiableList(this.neurons);
	}

	public Neuron getNeuron(int i) {
		return this.neurons.get(i);
	}

	public boolean contains(Neuron n) {
		return this.neurons.contains(n);
	}

	public int countNeurons() {
		return this.neurons.size();
	}

	@Override
	public Iterator<Neuron> iterator() {
		return new LayerIterator(this);
	}

}
