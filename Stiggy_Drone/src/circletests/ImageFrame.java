package circletests;

import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opencv.core.KeyPoint;

public class ImageFrame extends JFrame{
	
	private KeyPoint[] points;
	private BufferedImage imgToShow;
	private JTextField first;
	private JTextField second;
	private JTextField third;
	private JTextField fourth;
	private JTextField fifth;
	private JTextField sixth;
	private JPanelImage imagePanel;
	private JButton refresh;
	
	public ImageFrame(){
		super("Whatever");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		imagePanel = new JPanelImage();
		JPanel panel = new JPanel();
		first = new JTextField();
		first.setText("15353");
		second = new JTextField();
		second.setText("23453");
		third = new JTextField();
		third.setText("33453");
		fourth = new JTextField();
		fourth.setText("43455");
		fifth = new JTextField();
		fifth.setText("55345");
		sixth = new JTextField();
		sixth.setText("63455");
		refresh = new JButton();
		refresh.setText("Refresh!");
		panel.add(first);
		panel.add(second);
		panel.add(third);
		panel.add(fourth);
		panel.add(fifth);
		panel.add(sixth);
		panel.add(refresh);
		mainPanel.add(panel);
		mainPanel.add(imagePanel);
		this.add(mainPanel);
		this.setSize(700,800);
		this.setVisible(true);
	}
	
	
	public void setPoints(KeyPoint[] points){
		imagePanel.points = points;
		imagePanel.repaint();
	}
	
	public void setImage(BufferedImage img){
		imagePanel.image = img;
		imagePanel.repaint();
	}

}
