package com.nn.core;

public class Connection {

	private final Neuron in, out;
	private double weight;

	public Connection(Neuron in, Neuron out) {
		this(in, out, Math.random());
	}

	public Connection(Neuron in, Neuron out, double initialWeight) {
		this.in = in;
		this.out = out;

		this.weight = initialWeight;
	}

	public Neuron getInNeuron() {
		return in;
	}

	public Neuron getOutNeuron() {
		return out;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
