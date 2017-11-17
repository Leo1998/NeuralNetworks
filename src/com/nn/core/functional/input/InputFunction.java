package com.nn.core.functional.input;

import java.util.List;

import com.nn.core.Connection;

public abstract class InputFunction {

	public abstract double getOutput(List<Connection> inputConnections);

}
