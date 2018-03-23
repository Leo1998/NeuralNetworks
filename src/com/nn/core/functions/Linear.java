package com.nn.core.functions;

public class Linear extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		return totalInput;
	}

	@Override
	public double getDerivative(double totalInput) {
		throw new IllegalStateException("Can't use the linear activation function where a derivative is required.");
	}

}
