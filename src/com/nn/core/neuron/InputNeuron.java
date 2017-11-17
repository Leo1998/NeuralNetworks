package com.nn.core.neuron;

import com.nn.core.functional.input.WeightedSum;
import com.nn.core.functional.transfer.Linear;

public class InputNeuron extends Neuron {

	public InputNeuron() {
		super(new WeightedSum(), new Linear());
	}

	@Override
	public void calculate() {
		this.output = this.totalInput;
	}

}
