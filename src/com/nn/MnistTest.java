package com.nn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.nn.core.NeuralNetwork;
import com.nn.core.functions.TransferFunctionType;
import com.nn.core.training.Lesson;
import com.nn.core.training.Sample;
import com.nn.core.training.Trainer;
import com.nn.mnist.MnistManager;
import com.nn.utils.OneHot;

public class MnistTest implements KeyListener {

	private static final int NEURON_GRID = 28;
	private static final char[] LETTERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private Object waitLock = new Object();

	private JFrame frame;
	private DrawPanel drawPanel;
	private JLabel resultLabel;

	private NeuralNetwork nn;
	private Thread worker;

	public MnistTest() {
		int[] shape = { NEURON_GRID * NEURON_GRID, 600, 10 };

		this.nn = new NeuralNetwork(shape, TransferFunctionType.Sigmoid);
		this.nn.randomizeWeights(0.5);

		createView();
	}

	protected void detectCurrentLetter() {
		double[] grid = drawPanel.toGrid(NEURON_GRID);

		double[] output = nn.compute(grid);

		resultLabel.setText(Arrays.toString(output));

		drawPanel.clear();
	}

	protected void trainMnistDataset() {
		if (worker != null)
			return;

		this.worker = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Loading Mnist Dataset...");
				try {
					MnistManager m = new MnistManager("train-images.idx3-ubyte", "train-labels.idx1-ubyte", 60000);

					Trainer trainer = new Trainer(nn);

					List<Sample> batch = new ArrayList<>();
					for (int i = 0; i < 50; i++) {
						m.setCurrent(i);

						int[][] image = m.readImage();
						int label = m.readLabel();

						double[] in = new double[NEURON_GRID * NEURON_GRID];
						for (int x = 0; x < NEURON_GRID; x++) {
							for (int y = 0; y < NEURON_GRID; y++) {
								in[x + y * NEURON_GRID] = (double) image[x][y] / 255D;
							}
						}
						double[] out = OneHot.as(label, 10);

						batch.add(new Sample(in, out));
					}
					Lesson lesson = new Lesson(batch);
					System.out.println("Batch loaded!");
					
					trainer.train(lesson, 150, 0, 0.006, 0.9);

					m.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				worker = null;
			}
		});

		worker.start();
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

	private void createView() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		this.frame = new JFrame("Neural Network Debug Window");
		this.frame.setSize(800, 640);
		this.frame.setResizable(true);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.addKeyListener(this);

		JPanel rootPanel = new JPanel(new BorderLayout());

		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(300, 640));
		rootPanel.add(sidePanel, BorderLayout.LINE_START);

		this.drawPanel = new DrawPanel();
		sidePanel.add(drawPanel);

		this.resultLabel = new JLabel("Result");
		sidePanel.add(resultLabel);

		JButton detectLetterButton = new JButton("Detect Letter");
		detectLetterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				detectCurrentLetter();
			}
		});
		sidePanel.add(detectLetterButton);

		JButton trainButton = new JButton("Train Mnist");
		trainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trainMnistDataset();
			}
		});
		sidePanel.add(trainButton);

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
		new MnistTest();
	}

}
