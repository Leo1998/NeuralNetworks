package com.nn.core.neuron;

import com.nn.core.functional.input.InputFunction;
import com.nn.core.functional.transfer.TransferFunction;

public class InputOutputNeuron extends Neuron {

	private boolean externalInputSet;

	private double bias = 0D;

	public InputOutputNeuron() {
		super();
	}

	public InputOutputNeuron(InputFunction inFunc, TransferFunction transFunc) {
		super(inFunc, transFunc);
	}

	@Override
	public void setInput(double input) {
		this.totalInput = input;
		this.externalInputSet = true;
	}

	@Override
	public void calculate() {
		if (!externalInputSet) {
			totalInput = inputFunction.getOutput(this.inputConnections);
		}

		this.output = transferFunction.getOutput(this.totalInput + bias);

		if (externalInputSet) {
			externalInputSet = false;
			totalInput = 0;
		}
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

}
