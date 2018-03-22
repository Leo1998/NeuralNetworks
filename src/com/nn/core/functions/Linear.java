package com.nn.core.functions;

public class Linear extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		return totalInput;
	}

	@Override
	public double getDerivative(double totalInput) {
		return 1D;
	}

}
