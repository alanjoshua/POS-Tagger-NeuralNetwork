

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jblas.DoubleMatrix;

public class Draw extends Canvas {
	private List<Neuron> inputNeuron;
	private List<Neuron> h1Neuron;
	private List<Neuron> h2Neuron;
	private List<Neuron> outputNeuron;
	private List<Synapse> synapses;
	private Neuron input;
	private Neuron ans;

	private int width;
	private int height;
	
	BufferedImage img;

	public Draw(int width,int height) {

		inputNeuron = new ArrayList<Neuron>();
		h1Neuron = new ArrayList<Neuron>();
		h2Neuron = new ArrayList<Neuron>();
		outputNeuron = new ArrayList<Neuron>();
		synapses = new ArrayList<Synapse>();
		
		try {
			img = ImageIO.read(IOUtils.getFileFromResourcesFolder("cool-dark-background-wallpapersafari-19.jpeg"));		} catch (IOException e1) {
		
			e1.printStackTrace();
		}

		
		this.width = width;
		this.height = height;

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(img, 0,0, null);

		g.setColor(Color.WHITE);

		g.drawString("input Layer", (int)(this.width * 0.05), (int)(this.height * 0.02));
		g.drawString("hiddenLayer 1", (int)(this.width * 0.25), (int)(this.height * 0.02));
		g.drawString("hiddenLayer 2", (int)(this.width * 0.5), (int)(this.height * 0.02));
		g.drawString("outputLayer", (int)(this.width * 0.75), (int)(this.height * 0.02));
		g.drawString("realAnswer", (int)(this.width * 0.05), (int)(this.height * 0.175));

		input.render(g);
		ans.render(g);

		// for(Neuron n : inputNeuron) {
		// n.render(g);
		// }
		for (Neuron n : h1Neuron) {
			n.render(g);
		}
		for (Neuron n : h2Neuron) {
			n.render(g);
		}
		for (Neuron n : outputNeuron) {
			n.render(g);
		}

		for (Synapse s : synapses) {
			s.render(g);
		}
		g.dispose();
		bs.show();
	}

	public void init(int[] neuronNumbers) {

		input = new Neuron((int)(this.width * 0.05), (int)(this.height * 0.5), (int)(this.width * 0.15), (int)(this.height * 0.1), "inputNeuron");
		ans = new Neuron((int)(this.width * 0.05), (int)(this.height * 0.2), 0, 0, "Answer");

		// input neurons
		// for(int i = 0;i < neuronNumbers[0];i++) {
		// inputNeuron.add(new Neuron(50,(30 * i ) + 35,20,20,"inputNeuron"));
		// }

		// h1 neurons

		double temp = ((this.height - (this.height * 0.035)) / neuronNumbers[1]);
		int height = (int) temp;

		for (int i = 0; i < neuronNumbers[1]; i++) {
			h1Neuron.add(new Neuron((int) (this.width * 0.25), (int) ((height * i) + (this.height * 0.035)),
					(int) (this.width * 0.05), height, "h1Neuron"));
		}

		// h2 neurons

		temp = ((this.height - (this.height * 0.035)) / neuronNumbers[2]);
		height = (int) temp;
		for (int i = 0; i < neuronNumbers[2]; i++) {
			h2Neuron.add(new Neuron((int) (this.width * 0.5), (int) ((height * i) + (this.height * 0.035)),
					(int) (this.width * 0.05), height, "h2Neuron"));
		}

		// output neurons

		for (int i = 0; i < neuronNumbers[3]; i++) {
			outputNeuron
					.add(new Neuron((int) (this.width * 0.75), (int) ((this.height * 0.09 * i) + (this.height * 0.05)),
							(int) (this.width * 0.05), (int) (this.height * 0.05), "outputNeuron"));
		}

		// synapses

		for (int i = 0; i < h1Neuron.size(); i++) {
			synapses.add(new Synapse());
		}

		// for(int i = 0;i < inputNeuron.size();i++) {
		// for(int j = 0;j < h1Neuron.size();j++) {
		// synapses.add(new Synapse());
		// }
		// }

		for (int i = 0; i < h1Neuron.size(); i++) {
			for (int j = 0; j < h2Neuron.size(); j++) {
				synapses.add(new Synapse());
			}
		}

		for (int i = 0; i < h2Neuron.size(); i++) {
			for (int j = 0; j < outputNeuron.size(); j++) {
				synapses.add(new Synapse());
			}
		}
	}

	public void tick(double[] inputNeurons, double[] h1Neurons, double[] h2Neurons, double[] outputNeurons,String text,
			String ans, List<DoubleMatrix> weights) {
		
		input.tick(text);

		// for(int i = 0;i < inputNeurons.length;i++) {
		// inputNeuron.get(i).tick(new BigDecimal(inputNeurons[i]).round(new
		// MathContext(4)).doubleValue());
		// }

		for (int i = 0; i < h1Neurons.length; i++) {
			h1Neuron.get(i).tick(new BigDecimal(h1Neurons[i]).round(new MathContext(4)).doubleValue());
		}

		for (int i = 0; i < h2Neurons.length; i++) {
			h2Neuron.get(i).tick(new BigDecimal(h2Neurons[i]).round(new MathContext(4)).doubleValue());
		}

		double[] temp = Utils.getLargestAsArray(outputNeurons);

		int ind = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == 1) {
				ind = i;
				break;
			}
		}

		String answer = NeuralNetwork.outputDecoder(temp);
		for (int i = 0; i < outputNeurons.length; i++) {
			if (ind == i) {
				try {
				outputNeuron.get(i).tick(new BigDecimal(outputNeurons[i]).round(new MathContext(4)).doubleValue(), true,
						answer);
				}
				catch(NumberFormatException e) {
					
					System.out.println(outputNeurons[i] + " neuron is : " + i);
				}
			} else {
				outputNeuron.get(i).tick(new BigDecimal(outputNeurons[i]).round(new MathContext(4)).doubleValue());
			}
		}
		if (ans.equalsIgnoreCase(answer)) {
			this.ans.tick(ans, true);
		} else {
			this.ans.tick(ans);
		}

		// synapses
		int count = 0;
		int x1;
		int x2;
		int y1;
		int y2;
		Color c;

		for (int i = 0; i < weights.get(0).columns; i++) {
			x1 = input.getX() + input.getWidth();
			y1 = input.getY() + input.getHeight() / 2;
			x2 = h1Neuron.get(i).getX();
			y2 = h1Neuron.get(i).getY() + h1Neuron.get(0).getHeight() / 2;

			if (weights.get(0).get(0, i) <= 0.0) {
				c = Color.RED;
			} else {
				c = Color.BLUE;
			}

			synapses.get(count).tick(x1, y1, x2, y2, c,
					new BigDecimal(weights.get(0).get(0, i)).round(new MathContext(5)).doubleValue());
			count++;
		}

		// for(int i = 0;i < weights.get(0).rows;i++) {
		// for(int j = 0;j < weights.get(0).columns;j++) {
		//
		// x1 = inputNeuron.get(i).getX()+ inputNeuron.get(0).getWidth();
		// y1 = inputNeuron.get(i).getY();
		// x2 = h1Neuron.get(j).getX();
		// y2 = h1Neuron.get(j).getY();
		//
		// if(weights.get(0).get(i, j) <= 0.0) {
		// c = Color.YELLOW;
		// }
		// else {
		// c = Color.WHITE;
		// }
		//
		// synapses.get(count).tick(x1, y1, x2, y2, c, new
		// BigDecimal(weights.get(0).get(i, j)).round(new
		// MathContext(5)).doubleValue());
		// count++;
		// }
		// }

		for (int i = 0; i < weights.get(1).rows; i++) {
			for (int j = 0; j < weights.get(1).columns; j++) {

				x1 = h1Neuron.get(i).getX() + h1Neuron.get(0).getWidth();
				y1 = h1Neuron.get(i).getY() + h1Neuron.get(0).getHeight() / 2;
				x2 = h2Neuron.get(j).getX();
				y2 = h2Neuron.get(j).getY() + h2Neuron.get(0).getHeight() / 2;

				if (weights.get(1).get(i, j) <= 0.0) {
					c = Color.RED;
				} else {
					c = Color.BLUE;
				}

				synapses.get(count).tick(x1, y1, x2, y2, c,
						new BigDecimal(weights.get(1).get(i, j)).round(new MathContext(5)).doubleValue());
				count++;
			}
		}

		for (int i = 0; i < weights.get(2).rows; i++) {
			for (int j = 0; j < weights.get(2).columns; j++) {

				x1 = h2Neuron.get(i).getX() + h2Neuron.get(0).getWidth();
				y1 = h2Neuron.get(i).getY() + h2Neuron.get(0).getHeight() / 2;
				x2 = outputNeuron.get(j).getX();
				y2 = outputNeuron.get(j).getY() + outputNeuron.get(0).getHeight() / 2;

				if (weights.get(2).get(i, j) <= 0.0) {
					c = Color.RED;
				} else {
					c = Color.BLUE;
				}

				synapses.get(count).tick(x1, y1, x2, y2, c,
						new BigDecimal(weights.get(2).get(i, j)).round(new MathContext(5)).doubleValue());
				count++;
			}
		}
	}
}
