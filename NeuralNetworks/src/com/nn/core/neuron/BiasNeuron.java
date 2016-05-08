package com.nn.core.neuron;

import com.nn.core.Connection;

public class BiasNeuron extends Neuron {

	public BiasNeuron() {
		super();
	}

	@Override
	public double getOutput() {
		return 1;
	}

	@Override
	public void addInputConnection(Connection connection) {
	}

	@Override
	public void addInputConnection(Neuron fromNeuron) {
	}

}
