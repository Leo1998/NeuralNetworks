package com.nn.core.neuronbehavior;

public class Threshold implements NeuronBehavior {

	private double threshold;

	public Threshold(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public double computeActivation(double input) {
		return input >= threshold ? 1D : 0D;
	}

	@Override
	public double computeDerivative(double input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public NeuronBehavior getDedicatedInstance() {
		return new Threshold(getThreshold());
	}

	@Override
	public boolean needsDedicatedInstance() {
		return true;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}
