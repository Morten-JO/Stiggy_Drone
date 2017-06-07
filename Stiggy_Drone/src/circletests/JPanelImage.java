package circletests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.KeyPoint;

public class JPanelImage extends JPanel{

	public BufferedImage image;
	public KeyPoint[] points;
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(image != null){
			g.drawImage(image, 0, 0, null);
		}
		if(points != null){
			for(int i = 0; i < points.length; i++){
				g.setColor(Color.GREEN);
				int size = (int)points[i].size;
				g.drawOval((int)points[i].pt.x-size, (int)points[i].pt.y-size, (int)size*2, (int)size*2);
			}
		}
	}
	
}
