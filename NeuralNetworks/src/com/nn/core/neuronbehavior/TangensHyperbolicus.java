package com.nn.core.neuronbehavior;

public class TangensHyperbolicus implements NeuronBehavior {

	@Override
	public double computeActivation(double input) {
		return Math.tanh(input);
	}

	@Override
	public double computeDerivative(double input) {
		double d = Math.tanh(input);
		return 1.0D - d * d;
	}

	@Override
	public NeuronBehavior getDedicatedInstance() {
		return new TangensHyperbolicus();
	}

	@Override
	public boolean needsDedicatedInstance() {
		return false;
	}

}
