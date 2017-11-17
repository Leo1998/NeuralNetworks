package com.nn.core.functional.transfer;

public class Step extends TransferFunction {

	@Override
	public double getOutput(double totalInput) {
		if (totalInput > 0D)
			return 1D;
		else
			return 0D;
	}

}
