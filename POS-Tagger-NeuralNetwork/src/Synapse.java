

import java.awt.Color;
import java.awt.Graphics;

public class Synapse {
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color c;
	private double num;
	
	public void tick(int x1,int y1,int x2,int y2,Color c,double num) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.c = c;
		this.num = num;
	}
	
	public void render(Graphics g) {
		g.setColor(c);
		
			g.drawLine(x1, y1, x2, y2);
		
//		g.drawString(Double.toString(num), (x1 + x2) / 2, (y1 + y2) / 2);
	}
	
}
