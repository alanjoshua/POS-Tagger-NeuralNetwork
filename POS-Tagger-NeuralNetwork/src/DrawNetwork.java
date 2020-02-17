

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class DrawNetwork extends Canvas {

	private int[] neuronNumbers;
	private List<List<Neuron>> neuronList;
	private String[] id;

	public DrawNetwork(int[] neuronNumbers, String[] id) {
		this.neuronNumbers = neuronNumbers;
		this.id = id;
		neuronList = new ArrayList<List<Neuron>>();
		initNeurons();
	}

	public void initNeurons() {

		int i = 0;
		for (int x : neuronNumbers) {
			List<Neuron> list = new ArrayList<Neuron>();
			for (int j = 0; j < x; j++) {
				list.add(new Neuron(id[i]));
			}
			i++;
			neuronList.add(list);
		}
	}

	public void tick(int xOff, int yOff, List<List<Double>> vals, Color c,boolean shouldFill) {
		
		int hOff = 0;
		int h = 0;
		int wOff = (int) (getWidth() * 0.05);
		int w = (int) ((getWidth() - (neuronNumbers.length * wOff)) / neuronNumbers.length);
		
		for(int i = 0;i < neuronList.size();i++) {
			System.out.println(" i is :" + i);
			h = (int) ((getHeight() - (neuronNumbers[i] * hOff)) / neuronNumbers[i]);
			for(int j = 0;j < neuronNumbers[i];j++) {
				System.out.println(" j is :" + j);
				neuronList.get(i).get(j).tick((i * w) + wOff, (j * h) + hOff, w, h, xOff, yOff, Double.toString(vals.get(i)
						.get(j)), c, shouldFill);
			}
			System.out.println(i);
		}
		System.out.println("tick finished");
	}

	public void tick(List<List<Integer>> xOff, List<List<Integer>> yOff, List<List<Double>> vals,
			List<List<Color>> colors,List<List<Boolean>> shouldLoop) {
		
		int h = 0;
		int hOff = 0;
		int wOff = (int) (getWidth() * 0.05);
		int w = (int) ((getWidth() - (neuronNumbers.length * wOff)) / neuronNumbers.length);

		for(int i = 0;i < neuronList.size();i++) {
			h = (int) ((getHeight() - (neuronNumbers[i] * hOff)) / neuronNumbers[i]);
			for(int j = 0;j < neuronList.get(i).size();j++) {
				neuronList.get(i).get(j).tick((i * w) + wOff, (j * h) + hOff, w, h, xOff.get(i).get(j), yOff.get(i).get(j),
						Double.toString(vals.get(i).get(j)), colors.get(i).get(j),shouldLoop.get(i).get(j));
			}
		}
	}

	public void render() {
		System.out.println("draw rendered");
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		for (List<Neuron> x : neuronList) {
			for (Neuron y : x) {
				y.render(g);
			}
		}
	}

	public class Neuron {

		int x;
		int y;
		int xOff;
		int yOff;
		int height;
		int width;
		Color c;
		String text;
		String val;
		boolean shouldFill;
		String id;

		public Neuron(String id) {
			this.id = id;
			x = 0;
			y = 0;
			height = 0;
			width = 0;
			xOff = 0;
			yOff = 0;
			c = new Color(100, 10, 50);
		}

		public void tick(int x, int y, int width, int height, int xOff, int yOff, String val, Color c,
				boolean shouldFill) {
			this.x = x;
			this.y = y;
			this.xOff = xOff;
			this.yOff = yOff;
			this.width = width;
			this.height = height;
			this.shouldFill = shouldFill;
			this.c = c;
			this.val = val;
		}

		public void render(Graphics g) {
			g.setColor(c);

			if (shouldFill) {
				g.fillOval(x, y, width, height);
			} else {
				g.drawOval(x, y, width, height);
			}

			g.drawString(val, x + xOff, y + yOff);
		}
	}

}
