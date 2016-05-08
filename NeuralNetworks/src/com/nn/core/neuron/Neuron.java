package com.nn.core.neuron;

import java.util.ArrayList;
import java.util.List;

import com.nn.core.Connection;
import com.nn.core.Weight;
import com.nn.core.functional.input.InputFunction;
import com.nn.core.functional.input.WeightedSum;
import com.nn.core.functional.transfer.Linear;
import com.nn.core.functional.transfer.TransferFunction;

public abstract class Neuron {

	protected InputFunction inputFunction;
	protected TransferFunction transferFunction;

	protected List<Connection> inputConnections;
	protected List<Connection> outputConnections;

	protected double totalInput = 0D;
	protected double output = 0D;

	public Neuron() {
		this.inputFunction = new WeightedSum();
		this.transferFunction = new Linear();

		this.inputConnections = new ArrayList<>();
		this.outputConnections = new ArrayList<>();
	}

	public Neuron(InputFunction inputFunction, TransferFunction transferFunction) {
		this.inputFunction = inputFunction;
		this.transferFunction = transferFunction;
		this.inputConnections = new ArrayList<>();
		this.outputConnections = new ArrayList<>();
	}

	public void calculate() {
		this.totalInput = inputFunction.getOutput(inputConnections);
		this.output = transferFunction.getOutput(totalInput);
	}

	public void addInputConnection(Connection connection) {
		assert (connection.getOutNeuron() == this);

		inputConnections.add(connection);

		Neuron inNeuron = connection.getInNeuron();
		if (inNeuron.findOutputConnectionTo(this) == null) {
			inNeuron.addOutputConnection(connection);
		}
	}

	public void addOutputConnection(Connection connection) {
		assert (connection.getInNeuron() == this);

		outputConnections.add(connection);
	}

	public void addInputConnection(Neuron fromNeuron) {
		Connection connection = new Connection(fromNeuron, this);
		this.addInputConnection(connection);
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

	public Connection findInputConnectionTo(Neuron n) {
		for (Connection c : inputConnections) {
			if (c.getInNeuron() == n) {
				return c;
			}
		}

		return null;
	}

	public Connection findOutputConnectionTo(Neuron n) {
		for (Connection c : outputConnections) {
			if (c.getOutNeuron() == n) {
				return c;
			}
		}

		return null;
	}

	public double getOutput() {
		return output;
	}

	public double getInput() {
		return totalInput;
	}

	public void setInput(double input) {
		this.totalInput = input;
	}

	public InputFunction getInputFunction() {
		return inputFunction;
	}

	public void setInputFunction(InputFunction inputFunction) {
		this.inputFunction = inputFunction;
	}

	public TransferFunction getTransferFunction() {
		return transferFunction;
	}

	public void setTransferFunction(TransferFunction transferFunction) {
		this.transferFunction = transferFunction;
	}

	public Weight[] getWeights() {
		Weight[] weights = new Weight[inputConnections.size()];
		for (int i = 0; i < inputConnections.size(); i++) {
			weights[i] = inputConnections.get(i).getWeight();
		}
		return weights;
	}

}
