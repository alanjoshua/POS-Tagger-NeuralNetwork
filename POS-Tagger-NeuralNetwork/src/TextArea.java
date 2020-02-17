

import java.awt.image.BufferedImage;

import javax.swing.JTextArea;

public class TextArea extends JTextArea {
	
	private BufferedImage img;
	public TextArea(BufferedImage img) {
		this.img = img;
	}
	
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.drawImage(img, 0, 0, null);
//	}
	
}
