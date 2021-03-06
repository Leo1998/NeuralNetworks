package com.nn.core.functions;

public abstract class TransferFunction {

	public abstract double getOutput(double totalInput);

	public double getDerivative(double totalInput) {
		return 1D;
	}

}
