package com.nn.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.nn.core.neuron.Neuron;

public class Layer implements Iterable<Neuron> {

	private List<Neuron> neurons;

	public Layer() {
		this.neurons = new ArrayList<>();
	}

	public Layer(List<Neuron> neurons) {
		this.neurons = neurons;
	}

	public void calculate() {
		for (Neuron neuron : this.neurons) {
			neuron.calculate();
		}
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

	public int indexOf(Neuron neuron) {
		return neurons.indexOf(neuron);
	}

	public int getNeuronCount() {
		return this.neurons.size();
	}

	public void addNeuron(Neuron neuron) {
		neurons.add(neuron);
	}

	public final void removeNeuron(Neuron neuron) {
		neurons.remove(neuron);
	}

	@Override
	public Iterator<Neuron> iterator() {
		return new LayerIterator(this);
	}

}
