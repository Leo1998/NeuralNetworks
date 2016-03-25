package com.nn.core.neuronbehavior;

public interface NeuronBehavior {

	public double computeActivation(double input);

	public NeuronBehavior getDedicatedInstance();

}
