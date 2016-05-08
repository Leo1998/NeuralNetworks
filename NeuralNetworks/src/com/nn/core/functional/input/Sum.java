package com.nn.core.functional.input;

import java.util.List;

import com.nn.core.Connection;

public class Sum extends InputFunction {

	@Override
	public double getOutput(List<Connection> inputConnections) {
		double output = 0d;

		for (Connection connection : inputConnections) {
			output += connection.getInput();
		}

		return output;
	}

}
