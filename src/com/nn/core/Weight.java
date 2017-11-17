package com.nn.core;

public class Weight {

	private double value;

	public Weight() {
		this(Math.random());
	}

	public Weight(double value) {
		this.setValue(value);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
