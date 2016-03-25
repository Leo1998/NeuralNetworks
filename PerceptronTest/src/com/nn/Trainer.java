package com.nn;

public class Trainer {

	double[] inputs;
	int answer;

	public Trainer(double x, double y, int a) {
		inputs = new double[2];
		inputs[0] = x;
		inputs[1] = y;
		answer = a;
	}

}
