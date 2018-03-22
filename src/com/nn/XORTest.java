package com.nn;

import com.nn.core.NeuralNetwork;
import com.nn.core.functions.TransferFunctionType;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;
import com.nn.core.training.Trainer;

public class XORTest {

	public static void main(String[] args) {
		NeuralNetwork nn = new NeuralNetwork(new int[] { 2, 2, 1 }, TransferFunctionType.Sigmoid, true);
		nn.randomizeWeights(0.5);

		Lesson lesson = new Lesson(new Sample[] { //
				new Sample(new double[] { 0, 0 }, new double[] { 0 }), //
				new Sample(new double[] { 0, 1 }, new double[] { 1 }), //
				new Sample(new double[] { 1, 0 }, new double[] { 1 }), //
				new Sample(new double[] { 1, 1 }, new double[] { 0 }) //
		});//

		Trainer trainer = new Trainer(nn);
		trainer.train(lesson, 1000000, 0.1, 0.8);

		/*
		 * for(int i = 0; i < 50000; i++) { Sample s = samples[i %
		 * samples.length]; n.learnBackpropagation(s, 0.2);
		 * 
		 * System.out.println(Arrays.toString(s.getInput()) + "   " +
		 * Arrays.toString(n.compute(s.getInput())) + "   " +
		 * n.getInputMeanSquareError(s)); }
		 */
	}

}
