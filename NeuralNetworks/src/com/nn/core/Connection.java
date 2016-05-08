package com.nn.core;

import com.nn.core.neuron.Neuron;

public class Connection {

	private final Neuron in, out;
	private Weight weight;

	public Connection(Neuron in, Neuron out) {
		this(in, out, new Weight());
	}

	public Connection(Neuron in, Neuron out, double weight) {
		this(in, out, new Weight(weight));
	}

	public Connection(Neuron in, Neuron out, Weight weight) {
		this.in = in;
		this.out = out;

		this.weight = weight;
	}

	public Neuron getInNeuron() {
		return in;
	}

	public Neuron getOutNeuron() {
		return out;
	}

	public Weight getWeight() {
		return weight;
	}

	public void setWeight(Weight weight) {
		this.weight = weight;
	}

	public double getInput() {
		return this.in.getOutput();
	}

	public double getWeightedInput() {
		return this.getInput() * weight.getValue();
	}

}
