

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private BufferedImage img;
	public MainFrame(String title,BufferedImage img) {
		super(title);
		this.img = img;
	}
	
}
