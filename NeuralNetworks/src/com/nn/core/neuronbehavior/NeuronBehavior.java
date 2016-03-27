package com.nn.core.neuronbehavior;

public interface NeuronBehavior {

	public double computeActivation(double input);

	public double computeDerivative(double input);

	public NeuronBehavior getDedicatedInstance();

	public boolean needsDedicatedInstance();

}
