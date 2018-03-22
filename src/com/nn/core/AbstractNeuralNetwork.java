package com.nn.core;

import com.nn.core.functions.TransferFunction;
import com.nn.core.functions.TransferFunctionType;

public abstract class AbstractNeuralNetwork {

	final int[] shape;
	final int layerCount;
	final int inputCount;
	final int outputCount;

	final TransferFunction transferFunction;

	public AbstractNeuralNetwork(int[] shape, TransferFunctionType transferFunctionType) {
		this.shape = shape;
		this.layerCount = shape.length;

		this.transferFunction = transferFunctionType.instance();

		this.inputCount = countNeurons(0);
		this.outputCount = countNeurons(shape.length - 1);
	}

	public abstract int countNeurons(int layer);

	public abstract double[] compute(double[] input);

	public TransferFunction getTransferFunction() {
		return transferFunction;
	}

	public int getLayerCount() {
		return layerCount;
	}

	public int getInputCount() {
		return inputCount;
	}

	public int getOutputCount() {
		return outputCount;
	}

	public int[] getSizes() {
		return shape;
	}
}
