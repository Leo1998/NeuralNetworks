package com.nn.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.nn.core.neuron.Neuron;

public class LayerIterator implements Iterator<Neuron> {

	private int currentIndex = 0;
	private Layer layer;

	public LayerIterator(Layer layer) {
		this.layer = layer;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < layer.getNeuronCount();
	}

	@Override
	public Neuron next() {
		if (!hasNext())
			throw new NoSuchElementException();

		Neuron s = layer.getNeuron(currentIndex);
		currentIndex++;

		return s;
	}

}
