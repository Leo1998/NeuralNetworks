package com.nn.core;

import java.util.LinkedList;
import java.util.List;

import com.nn.core.neuronbehavior.NeuronBehavior;

public class Neuron {

	private NeuronBehavior behavior;

	private List<Connection> inputConnections;
	private List<Connection> outputConnections;

	private boolean fired = false;
	private double output = 0.0D;

	public Neuron(NeuronBehavior behavior) {
		this.behavior = behavior.getDedicatedInstance();

		this.inputConnections = new LinkedList<Connection>();
		this.outputConnections = new LinkedList<Connection>();
	}

	public void addConnection(Connection c) {
		if (c.getOutNeuron() == this) {
			inputConnections.add(c);
		} else if (c.getInNeuron() == this) {
			outputConnections.add(c);
		}
	}

	public void removeConnection(Connection c) {
		if (c.getOutNeuron() == this) {
			inputConnections.remove(c);
		} else if (c.getInNeuron() == this) {
			outputConnections.remove(c);
		}
	}

	public List<Connection> getInputConnections() {
		return inputConnections;
	}

	public List<Connection> getOutputConnections() {
		return outputConnections;
	}

	public int countInputs() {
		return inputConnections.size();
	}

	public int countOutputs() {
		return outputConnections.size();
	}

	public double fire() {
		if (countInputs() > 0 && !fired) {
			double sum = 0.0f;
			for (int i = 0; i < countInputs(); i++) {
				Connection c = inputConnections.get(i);
				Neuron inNeuron = c.getInNeuron();

				sum += c.getWeight() * inNeuron.fire();
			}

			this.output = behavior.computeActivation(sum);
		}

		fired = true;
		return this.output;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public boolean isFired() {
		return fired;
	}

	public void resetFiredFlag() {
		this.fired = false;
	}

}
