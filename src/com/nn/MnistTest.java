package com.nn;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

	private Object waitLock = new Object();

	private JFrame frame;
	private DrawPanel drawPanel;
	private JLabel resultLabel;
	private JTextField nameField;
	private JLabel statusLabel;

	private NeuralNetwork nn;
	private Thread worker;

	public MnistTest() {
		createView();
	}

	private NeuralNetwork createNet() {
		int[] shape = { NEURON_GRID * NEURON_GRID, 600, 10 };

		NeuralNetwork nn = new NeuralNetwork(shape, TransferFunctionType.Sigmoid);
		nn.randomizeWeights(0.5);

		return nn;
	}

	protected void detectCurrentLetter() {
		if (nn == null) {
			System.out.println("No Network Loaded!");
			return;
		}

		double[] grid = drawPanel.toGrid(NEURON_GRID);

		double[] output = nn.compute(grid);

		resultLabel.setText("Result: " + OneHot.maxIndex(output));
		System.out.println("Result: " + Arrays.toString(output));

		drawPanel.clear();
	}

	protected void trainMnistDataset() {
		if (worker != null)
			return;

		this.worker = new Thread(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText("Loading Mnist Dataset...");
				try {
					MnistManager m = new MnistManager("train-images.idx3-ubyte", "train-labels.idx1-ubyte", 60000);

					statusLabel.setText("Starting Training");
					
					NeuralNetwork newNN = createNet();
					Trainer trainer = new Trainer(newNN);
					
					final int BATCH_SIZE = 50;
					for (int b = 0; b < 1000; b += BATCH_SIZE) {
						List<Sample> batch = new ArrayList<>();
						for (int i = b; i < b + BATCH_SIZE; i++) {
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

						trainer.train(lesson, 100, 0, 0.006, 0.9);
						statusLabel.setText(("Batch(" + b / BATCH_SIZE + ") trained!"));
					}

					File file = new File(nameField.getText());
					file.createNewFile();
					newNN.save(file);

					MnistTest.this.nn = newNN;

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

		JPanel rootPanel = new JPanel(new FlowLayout());

		this.drawPanel = new DrawPanel();
		rootPanel.add(drawPanel);

		this.resultLabel = new JLabel("Result");
		rootPanel.add(resultLabel);

		JButton detectLetterButton = new JButton("Detect Letter");
		detectLetterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				detectCurrentLetter();
			}
		});
		rootPanel.add(detectLetterButton);

		this.nameField = new JTextField(20);
		rootPanel.add(nameField);

		JButton loadButton = new JButton("Load Network");
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nn = createNet();

				File file = new File(nameField.getText());
				if (file.exists()) {
					nn.load(file);
				} else {
					System.out.println("File does not exist!!!!!!!!!!");
				}
			}
		});
		rootPanel.add(loadButton);

		JButton trainButton = new JButton("Train new Network");
		trainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				trainMnistDataset();
			}
		});
		rootPanel.add(trainButton);

		this.statusLabel = new JLabel("Train Status");
		rootPanel.add(statusLabel);

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
