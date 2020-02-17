

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Graph extends Canvas {

	private int width;
	private int height;
	private double scaleX = 0.01;
	private double scaleY = 6;
	private double scaleX2 = 0.1;
	private double scaleY2 = 1;
	private int sub = 0;
	private List<Double> costList;
	private List<double[]> currentPosition;
	private String text = "";
	private String title = "Cost";
	private int mouseX = 0;
	private int mouseY = 0;
	private int x = 0;
	private int y = 0;
	private BufferedImage img;
	private boolean showCost = true;
	private List<Double> correctList;
	private List<double[]> currentCorrect;
	private int correctTickRate;
	private int tickRate = 0;

	public Graph(int w,int h,int correctTickRate,int tickRate) {

		this.setPreferredSize(new Dimension(w,h));
		this.setMaximumSize(new Dimension(w,h));
		this.setMinimumSize(new Dimension(w,h));
		
		this.width = w;
		this.height = h;
		this.correctTickRate = correctTickRate;
		this.tickRate = tickRate;

		try {
			img = ImageIO.read(IOUtils.getFileFromResourcesFolder("cool-dark-background-wallpapersafari-19.jpeg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		costList = new ArrayList<Double>();
		correctList = new ArrayList<Double>();
		currentPosition = new ArrayList<double[]>();
		currentCorrect = new ArrayList<double[]>();

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				mouseX = e.getX();
				mouseY = e.getY();

				int xCurrent = 0;
				int yCurrent = 0;
				
				if(showCost) {
				
				for (int i = 0; i < currentPosition.size(); i++) {
					xCurrent = (int) currentPosition.get(i)[0];
					yCurrent = (int) currentPosition.get(i)[1];

					if (mouseX >= (xCurrent - (10)) && mouseX <= (xCurrent + (10))) {
						if (mouseY >= (yCurrent - (10)) && mouseY <= (yCurrent + (10))) {
							text = "cost is " + Double.toString(currentPosition.get(i)[2]);

							if (mouseX + 200 < width) {
								x = mouseX;
								y = (int) (mouseY - 10);
							} else {
								x = mouseX - 200;
								y = (int) (mouseY - 10);
							}

						}
					}

				}
				}
				else {
					for (int i = 0; i < currentCorrect.size(); i++) {
						xCurrent = (int) currentCorrect.get(i)[0];
						yCurrent = (int) currentCorrect.get(i)[1];

						if (mouseX >= (xCurrent - (10)) && mouseX <= (xCurrent + (10))) {
							if (mouseY >= (yCurrent - (10)) && mouseY <= (yCurrent + (10))) {
								text = Double.toString(currentCorrect.get(i)[2]);

								if (mouseX + 30 < width) {
									x = mouseX;
									y = (int) (mouseY - 10);
								} else {
									x = mouseX - 30;
									y = (int) (mouseY - 10);
								}

							}
						}

					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				text = "";
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				text = "";
			}

		});

	}

	public void setScaleX(double x) {
		this.scaleX = x;
	}
	
	public void setScaleX2(double x) {
		this.scaleX2 = x;
	}

	public void showCost() {
		this.showCost = true;
		title = "Cost";
	}

	public void showCorrect() {
		this.showCost = false;
		title = "Validation Rate";
	}

	public void setScaleY(double x) {
		this.scaleY = x;
	}
	
	public void setScaleY2(double x) {
		this.scaleY2 = x;
	}


	public double getScaleX() {
		return scaleX;
	}
	
	public double getScaleX2() {
		return scaleX2;
	}
	
	public double getScaleY2() {
		return scaleY2;
	}

	public double getScaleY() {
		return scaleY;
	}

	public double getCostAt(int x) {
		return costList.get(x + sub);
	}

	public void tick(double cost) {
		costList.add(cost);
	}
	
	public void setTrainingRate(int trainingRate) {
		this.tickRate = trainingRate;
	}

	public void tickCorrect(double correct) {
		correctList.add(correct);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, null);

		if (showCost) {

			if (costList.size() * scaleX * tickRate > width) {
				sub = (int) ((costList.size() * tickRate * scaleX) - width);
			}
			int x1 = 0;
			int x2 = 0;
			int y1 = 0;
			int y2 = 0;
			double[] temp = new double[3];

			for (int i = 0; i < costList.size() - 1; i++) {

				x1 = (int) (i * scaleX * tickRate);
				x2 = (int) ((i + 1) * scaleX * tickRate);
				y1 = (int) ((height) - Math.pow((costList.get(i).doubleValue() * scaleY), 1.5));
				y2 = (int) ((height) - Math.pow((costList.get(i + 1).doubleValue() * scaleY), 1.5));

				x1 -= sub;
				x2 -= sub;

				if (x2 >= 0) {
					temp[0] = x2;
					temp[1] = y2;
					temp[2] = costList.get(i + 1);
					currentPosition.add(temp);
					g.setColor(Color.YELLOW);
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
		else {
			if (correctList.size() * scaleX2 * correctTickRate> width) {
				sub = (int) ((correctList.size() * scaleX2 * correctTickRate) - width);
			}
			int x1 = 0;
			int x2 = 0;
			int y1 = 0;
			int y2 = 0;
			double[] temp = new double[3];

			for (int i = 0; i < correctList.size() - 1; i++) {

				x1 = (int) (i * scaleX2 * correctTickRate);
				x2 = (int) ((i + 1) * scaleX2 * correctTickRate);
				y1 = (int) ((height) - (correctList.get(i).doubleValue() * scaleY2));
				y2 = (int) ((height) - (correctList.get(i + 1).doubleValue() * scaleY2));

				x1 -= sub;
				x2 -= sub;

				if (x2 >= 0) {
					temp[0] = x2;
					temp[1] = y2;
					temp[2] = correctList.get(i + 1);
					currentCorrect.add(temp);
					g.setColor(Color.YELLOW);
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
		
		g.setColor(Color.WHITE);
		g.drawString(text, x, y);
		g.setColor(Color.CYAN);
		g.drawString(title, (int) (width * 0.45), (int) (height * 0.1));

		g.dispose();
		bs.show();
	}
}
