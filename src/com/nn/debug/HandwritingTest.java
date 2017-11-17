package com.nn.debug;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import com.nn.core.NeuralNetwork;
import com.nn.core.functional.transfer.TransferFunctionType;
import com.nn.core.nnet.MultiLayerPerceptron;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;

public class HandwritingTest implements KeyListener {
	
	private static final int NEURON_GRID = 28;
	private static final char[] LETTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final double LEARNING_RATE = 0.2;

	private Object waitLock = new Object();

	private JFrame frame;
	private DebugPanel debugPanel;
	private DrawPanel drawPanel;
	private JSlider letterSlider;

	private NeuralNetwork neuralNetwork;

	public HandwritingTest() {
		List<Integer> neuronsInLayers = new ArrayList<>();
		neuronsInLayers.add(NEURON_GRID * NEURON_GRID);
		neuronsInLayers.add(200);
		neuronsInLayers.add(LETTERS.length);

		this.neuralNetwork = new MultiLayerPerceptron(neuronsInLayers, TransferFunctionType.Sigmoid, true);
		
		createView();
	}
	
	protected void detectCurrentLetter() {
		double[] grid = drawPanel.toGrid(NEURON_GRID);
		
		neuralNetwork.setInput(grid);
		neuralNetwork.calculate();
		
		drawPanel.clear();
		refresh();
	}

	protected void teachCurrentLetter() {
		double[] grid = drawPanel.toGrid(NEURON_GRID);
		
		double[] out = new double[LETTERS.length];
		
		int letterIndex = letterSlider.getValue();
		out[letterIndex] = 1.0f;
		
		Sample s = new Sample(grid, out);
		
		Lesson lesson = new Lesson(s);

		neuralNetwork.train(lesson, LEARNING_RATE);
		
		neuralNetwork.setInput(grid);
		neuralNetwork.calculate();
		
		drawPanel.clear();
		refresh();
	}

	public void waitForEvent() {
		synchronized (waitLock) {
			try {
				waitLock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void refresh() {
		debugPanel.repaint();
	}
	
	private void createView() {
		this.frame = new JFrame("Neural Network Debug Window");
		this.frame.setSize(800, 640);
		this.frame.setResizable(true);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.addKeyListener(this);
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		
		this.debugPanel = new DebugPanel(neuralNetwork);
		rootPanel.add(debugPanel, BorderLayout.CENTER);
		
		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(300, 640));
		rootPanel.add(sidePanel, BorderLayout.LINE_START);
		
		this.drawPanel = new DrawPanel();
		sidePanel.add(drawPanel);
		
		JButton detectLetterButton = new JButton("Detect Letter");
		detectLetterButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				detectCurrentLetter();
			}
		});
		sidePanel.add(detectLetterButton);
		
		JButton teachLetterButton = new JButton("Teach Letter");
		teachLetterButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				teachCurrentLetter();
			}
		});
		sidePanel.add(teachLetterButton);
		
		this.letterSlider = new JSlider(0, LETTERS.length);
		letterSlider.setPaintTicks(true);
		letterSlider.setSnapToTicks(true);
		letterSlider.setPaintLabels(true);
		letterSlider.setMajorTickSpacing(1);
		sidePanel.add(letterSlider);
		
		this.frame.add(rootPanel);
		this.frame.setVisible(true);		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			synchronized (waitLock) {
				waitLock.notifyAll();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	public static void main(String[] args) {
		new HandwritingTest();
	}

	

}
