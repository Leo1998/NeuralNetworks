package com.nn;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private BufferedImage img;
	private int[] pixels;
	private final int size = 200;

	public DrawPanel() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.setMinimumSize(new Dimension(size, size));
		this.setMaximumSize(new Dimension(size, size));
		this.setPreferredSize(new Dimension(size, size));

		this.img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}
	
	public void display(int[][] image) {
		int scale = size / image.length;
		
		for (int xGrid = 0; xGrid < image.length; xGrid++) {
			for (int yGrid = 0; yGrid < image[xGrid].length; yGrid++) {
				
				for (int x = xGrid * scale; x < xGrid * scale + scale; x++) {
					for (int y = yGrid * scale; y < yGrid * scale + scale; y++) {
						pixels[x + y * size] = image[xGrid][yGrid];
					}
				}
				
			}
		}
		
		repaint();
	}
	
	public double[] toGrid(int gridSize) {
		double[] grid = new double[gridSize * gridSize];
		
		int scale = size / gridSize;
		
		for (int xGrid = 0; xGrid < gridSize; xGrid++) {
			for (int yGrid = 0; yGrid < gridSize; yGrid++) {
				int count = 0;
				
				for (int x = xGrid * scale; x < xGrid * scale + scale; x++) {
					for (int y = yGrid * scale; y < yGrid * scale + scale; y++) {
						if (pixels[x + y * size] > 0) {
							count ++;
						}
					}
				}
				
				grid[xGrid + yGrid * gridSize] = count / (double) (scale * scale);
			}
		}
		
		return grid;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(img, null, 0, 0);
	}
	
	public void clear() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				pixels[x + y * size] = 0;
			}
		}
		
		this.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for (int x = e.getX() - 1; x < e.getX() + 10; x++) {
			for (int y = e.getY() - 1; y < e.getY() + 10; y++) {
				if (x > 0 && y > 0 && x < size && y < size) {
					pixels[x + y * size] = 0xFFFFFFFF;
				}
			}
		}

		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (int x = e.getX() - 1; x < e.getX() + 10; x++) {
			for (int y = e.getY() - 1; y < e.getY() + 10; y++) {
				if (x > 0 && y > 0 && x < size && y < size) {
					pixels[x + y * size] = 0xFFFFFFFF;
				}
			}
		}
		
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}