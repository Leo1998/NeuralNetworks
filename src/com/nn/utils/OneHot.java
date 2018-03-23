package com.nn.utils;

public class OneHot {

	private OneHot() {}
	
	public static double[] as(int index, int length) {
		double[] a = new double[length];
		a[index] = 1.0D;
		
		return a;
	}
	
	public static int maxIndex(double[] out) {
		double max = out[0];
		int maxIdx = 0;
		for (int i = 1; i < out.length; i++) {
			if (out[i] >= max) {
				max = out[i];
				maxIdx = i;
			}
		}
		
		return maxIdx;
	}
	
}
