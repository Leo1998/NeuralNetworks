package com.nn.core;

import com.nn.core.neuronbehavior.Identity;
import com.nn.core.neuronbehavior.NeuronBehavior;
import com.nn.core.neuronbehavior.TangensHyperbolicus;

public class NeuralNetworkDescriptor {

	private int[] neuronsPerLayer;
	private NeuronBehavior inputBehavior = new Identity();
	private NeuronBehavior hiddenBehavior = new TangensHyperbolicus();
	private NeuronBehavior outputBehavior = new TangensHyperbolicus();

	private boolean fullyConnect = true;

	public NeuralNetworkDescriptor(int[] neuronsPerLayer) {
		this.setNeuronsPerLayer(neuronsPerLayer);
	}

	public int[] getNeuronsPerLayer() {
		return (int[]) this.neuronsPerLayer.clone();
	}

	private void setNeuronsPerLayer(int[] a) {
		if (a.length < 2) {
			throw new IllegalArgumentException("There must be at least 2 layers.");
		}
		if (a[0] < 1) {
			throw new IllegalArgumentException("There must be at least 1 input neuron.");
		}
		if (a[(a.length - 1)] < 1) {
			throw new IllegalArgumentException("There must be at least 1 output neuron.");
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] < -1) {
				throw new IllegalArgumentException("Illegal neuron number definition in layer " + i + ".");
			}
		}
		this.neuronsPerLayer = ((int[]) a.clone());
	}

	public int countLayers() {
		return this.neuronsPerLayer.length;
	}

	public int getNeuronsPerLayer(int layerIndex) {
		return neuronsPerLayer[layerIndex];
	}

	public boolean isFullyConnect() {
		return fullyConnect;
	}

	public void setFullyConnect(boolean fullyConnect) {
		this.fullyConnect = fullyConnect;
	}

	public NeuronBehavior getInputBehavior() {
		return inputBehavior;
	}

	public void setInputBehavior(NeuronBehavior inputBehavior) {
		this.inputBehavior = inputBehavior;
	}

	public NeuronBehavior getHiddenBehavior() {
		return hiddenBehavior;
	}

	public void setHiddenBehavior(NeuronBehavior hiddenBehavior) {
		this.hiddenBehavior = hiddenBehavior;
	}

	public NeuronBehavior getOutputBehavior() {
		return outputBehavior;
	}

	public void setOutputBehavior(NeuronBehavior outputBehavior) {
		this.outputBehavior = outputBehavior;
	}

}
