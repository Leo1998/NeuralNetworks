package com.nn.core.neuronbehavior;

public class Identity implements NeuronBehavior {

	@Override
	public double computeActivation(double input) {
		return input;
	}

	@Override
	public double computeDerivative(double input) {
		return 1.0D;
	}

	@Override
	public NeuronBehavior getDedicatedInstance() {
		return new Identity();
	}

	@Override
	public boolean needsDedicatedInstance() {
		return false;
	}

}
