package com.nn.core;

import com.nn.core.neuronbehavior.NeuronBehavior;

public class NeuralNetworkDescriptor {

	private int[] neuronsPerLayer;
	private NeuronBehavior[] layerBehaviors;

	private boolean fullyConnect = true;

	public NeuralNetworkDescriptor(int[] neuronsPerLayer, NeuronBehavior[] layerBehaviors) {
		this.setNeuronsPerLayer(neuronsPerLayer);
		this.layerBehaviors = layerBehaviors;
		assert (neuronsPerLayer.length == layerBehaviors.length);
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

	public NeuronBehavior getNeuronBehavior(int layerIndex) {
		return layerBehaviors[layerIndex];
	}

	public boolean isFullyConnect() {
		return fullyConnect;
	}

	public void setFullyConnect(boolean fullyConnect) {
		this.fullyConnect = fullyConnect;
	}

}
