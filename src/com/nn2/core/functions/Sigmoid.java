package com.nn2.core.functions;

public class Sigmoid extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		if (totalInput > 100) {
			return 1.0;
		} else if (totalInput < -100) {
			return 0.0;
		}

		double den = 1d + Math.exp(-totalInput);
		return (1d / den);
	}

	@Override
	public double getDerivative(double totalInput) {
		double output = getOutput(totalInput);

		double derivative = output * (1d - output) + 0.1;
		return derivative;
	}

}
