package com.nn.core.neuronbehavior;

public class IdentityBehavior implements NeuronBehavior {

	@Override
	public double computeActivation(double input) {
		return input;
	}

	@Override
	public NeuronBehavior getDedicatedInstance() {
		return new IdentityBehavior();
	}

}
