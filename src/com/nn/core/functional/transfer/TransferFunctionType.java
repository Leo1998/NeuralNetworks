package com.nn.core.functional.transfer;

public enum TransferFunctionType {

	Linear("com.nn.core.functional.transfer.Linear"), Tanh("com.nn.core.functional.transfer.Tanh"), Sigmoid("com.nn.core.functional.transfer.Sigmoid"), Step("com.nn.core.functional.transfer.Step");

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
