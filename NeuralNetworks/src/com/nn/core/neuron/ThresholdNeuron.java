package com.nn.core.neuron;

import com.nn.core.functional.input.InputFunction;
import com.nn.core.functional.transfer.TransferFunction;

public class ThresholdNeuron extends Neuron {

	protected double thresh = 0;

	public ThresholdNeuron(InputFunction inputFunction, TransferFunction transferFunction) {
		super(inputFunction, transferFunction);

		this.thresh = Math.random();
	}

	@Override
	public void calculate() {
		this.totalInput = this.inputFunction.getOutput(this.inputConnections);
		this.output = this.transferFunction.getOutput(this.totalInput - this.thresh);
	}

	public double getThresh() {
		return thresh;
	}

	public void setThresh(double thresh) {
		this.thresh = thresh;
	}
}
