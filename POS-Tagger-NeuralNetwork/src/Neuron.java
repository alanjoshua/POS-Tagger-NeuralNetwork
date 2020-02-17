

import java.awt.Color;
import java.awt.Graphics;

public class Neuron {
	int x;
	 int y;
	 int width;
	 int height;
	 Color color;
	 Color defColor;
	 double num;
	 String text = "";
	 String ID = "";
	 
	 Neuron(int x,int y,int width,int height,String ID) {
		 this.x = x;
		 this.y = y;
		 this.width = width;
		 this.height = height;
		 this.ID = ID;
		 if(ID.equalsIgnoreCase("outputNeuron")) {
			 defColor = Color.RED;
		 }
		 else if(ID.equalsIgnoreCase("inputNeuron")) {
			 defColor = Color.YELLOW;
		 }
		 else {
			 defColor = Color.ORANGE;
		 }
		 
		 color = defColor;
	 }
	 
	 public void tick(double num) {
		 this.num = num;
		 text = Double.toString(num);
		 color = defColor;
	 }
	 
	 public void tick(double num,boolean setColor,String text) {
		this.num = num;
		if(setColor) {
			color = Color.GREEN;
		}
		this.text = Double.toString(num) + "   " + text;
	 }
	 
	 public void tick(String text,boolean setColor) {
		 this.text = text;
		 color = Color.GREEN;
	 }
	 
	 public void tick(String text) {
		 this.text = text;
		 color = defColor;
	 }
	 
	 public int getX() {
		 return x;
	 }
	 
	 public int getWidth() {
		 return width;
	 }
	 
	 public int getHeight() {
		 return height;
	 }
	 
	 public int getY() {
		 return y;
	 }
	 
	 public void render(Graphics g) {
		 g.setColor(color);
		 g.drawOval(x, y, width, height);
		 g.setColor(color);
		 if(ID.equalsIgnoreCase("inputNeuron")) {
			 g.drawString(text, x + (width / 3), y + height/2);
		 }
		 else if(ID.equalsIgnoreCase("outputNeuron")) {
			 g.drawString(text, x + width, y + height/2);
		 }
		 else {
			 g.drawString(text, x, y + height/2);
		 }
		
	 }
	 
}
