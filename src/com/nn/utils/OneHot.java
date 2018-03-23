package com.nn.utils;

public class OneHot {

	private OneHot() {}
	
	public static double[] as(int index, int length) {
		double[] a = new double[length];
		a[index] = 1.0D;
		
		return a;
	}
	
}
