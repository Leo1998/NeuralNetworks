package com.nn.core.functions;

public enum TransferFunctionType {

	Linear("com.nn.core.functions.Linear"), Tanh("com.nn.core.functions.Tanh"), Sigmoid("com.nn.core.functions.Sigmoid"), Step("com.nn.core.functions.Step");

	public final String className;

	TransferFunctionType(String className) {
		this.className = className;
	}

	public TransferFunction instance() {
		try {
			return (TransferFunction) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

}
