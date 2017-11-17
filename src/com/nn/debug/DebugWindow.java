package com.nn.debug;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.nn.core.Connection;
import com.nn.core.Layer;
import com.nn.core.NeuralNetwork;
import com.nn.core.neuron.Neuron;
import com.nn.core.util.MathUtil;

public class DebugWindow implements KeyListener {

	private Object waitLock = new Object();

	private JFrame frame;
	private DebugPanel panel;

	private NeuralNetwork nn;

	public DebugWindow(NeuralNetwork nn) {
		this.nn = nn;

		this.frame = new JFrame("Neural Network Debug Window");
		this.frame.setSize(800, 640);
		this.frame.setMinimumSize(new Dimension(800, 640));
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.addKeyListener(this);

		this.panel = new DebugPanel();
		this.frame.add(panel);

		this.frame.setVisible(true);
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
		panel.repaint();
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

	class DebugPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public DebugPanel() {
			this.setBackground(Color.DARK_GRAY);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;

			Map<Neuron, Point> neuronPositions = new HashMap<Neuron, Point>();

			final int xStart = 100;
			final int xEnd = getWidth() - 100;
			final int yStart = 100;
			final int yEnd = getHeight() - 100;

			final int xStep = (xEnd - xStart) / nn.getLayerCount();

			int xPos = xStart + (xStep / 2);
			for (Layer layer : nn.getLayers()) {
				final int yStep = (yEnd - yStart) / layer.getNeuronCount();
				int yPos = yStart + (yStep / 2);

				for (Neuron n : layer.getNeurons()) {
					neuronPositions.put(n, new Point(xPos, yPos));

					yPos += yStep;
				}

				xPos += xStep;
			}

			// draw connections
			for (Layer layer : nn.getLayers()) {
				for (Neuron n : layer.getNeurons()) {
					for (Connection c : n.getOutputConnections()) {
						Point p1 = neuronPositions.get(n);
						Point p2 = neuronPositions.get(c.getOutNeuron());
						Point center = new Point(p1.x + (p2.x - p1.x) / 2, p1.y + (p2.y - p1.y) / 2);

						g2d.setColor(Color.BLACK);
						g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
						g2d.setColor(Color.WHITE);
						g2d.drawString("" + MathUtil.round(c.getWeight().getValue(), 2), center.x, center.y);
					}
				}
			}

			// draw neurons
			for (Neuron neuron : neuronPositions.keySet()) {
				Point p = neuronPositions.get(neuron);
				drawNeuron(g2d, p.x, p.y, neuron);
			}
		}

		private void drawNeuron(Graphics2D g2d, int xPos, int yPos, Neuron n) {
			final int size = 60;

			g2d.setColor(Color.BLUE);
			g2d.fillOval(xPos - (size / 2), yPos - (size / 2), size, size);

			g2d.setColor(Color.WHITE);
			g2d.drawString("" + MathUtil.round(n.getOutput(), 2), xPos, yPos);
		}

	}

}
