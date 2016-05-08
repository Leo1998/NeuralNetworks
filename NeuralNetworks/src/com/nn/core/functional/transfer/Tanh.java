package com.nn.core.functional.transfer;

public class Tanh extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		return Math.tanh(totalInput);
	}

	@Override
	public double getDerivative(double totalInput) {
		double d = Math.tanh(totalInput);
		return 1.0D - d * d;
	}

}
