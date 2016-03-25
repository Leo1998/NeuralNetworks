package com.nn.core.neuronbehavior;

public class ThresholdBehavior implements NeuronBehavior {

	private double threshold;

	public ThresholdBehavior(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public double computeActivation(double input) {
		return input >= threshold ? 1D : 0D;
	}

	@Override
	public NeuronBehavior getDedicatedInstance() {
		return new ThresholdBehavior(getThreshold());
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}
