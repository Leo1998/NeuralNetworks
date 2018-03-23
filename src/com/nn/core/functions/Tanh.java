package com.nn.core.functions;

public class Tanh extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		return Math.tanh(totalInput);
	}

	@Override
	public double getDerivative(double totalInput) {
		double d = Math.tanh(totalInput);
		return 1.0D - Math.pow(d, 2);
	}

}
