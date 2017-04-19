package main;

import java.awt.image.BufferedImage;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import de.yadrone.apps.paperchase.PaperChase;
import de.yadrone.apps.paperchase.TagListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;

public class QRController  {
	
	private static QRClass qrCode = new QRClass();


	public void printCoordinates(Result res){
		
		ResultPoint[] points = res.getResultPoints();
		
		
		for (int i = 0; i < points.length; i++){
			
			System.out.println(points[i]);
		}
		System.out.println("There are " + points.length + " points");
		System.out.println("Top line is " +  (points[2].getX()-points[1].getX())+ " pixels" );
	}
	
	public void centerDrone(BufferedImage img, ARDrone drone) throws InterruptedException
	{
		if(!Main.userControl){
			Result tag = null;
			try {
				tag = qrCode.getResult(img);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tagText;
			ResultPoint[] points;
			
				points = tag.getResultPoints();	
				tagText = tag.getText();
				
			
			
			int imgCenterX = Values.IMAGE_WIDTH / 2;
			int imgCenterY = Values.IMAGE_HEIGHT / 2;
			
			float x = points[1].getX();
			float y = points[1].getY();
			
			if (x < (imgCenterX - Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go left");

				drone.getCommandManager().goLeft(Values.SPEED).doFor(Values.DOTIME+25);
				drone.getCommandManager().hover();
//				drone.getCommandManager().goLeft(Values.SPEED);
				Thread.currentThread().sleep(Values.DOTIME+25);
			}
		
		
			else if (x > (imgCenterX + Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go right");
				drone.getCommandManager().goRight(Values.SPEED).doFor(Values.DOTIME+25);
				drone.getCommandManager().hover();
//				drone.getCommandManager().goRight(Values.SPEED);
				Thread.currentThread().sleep(Values.DOTIME+25);
			}
			else if (y < (imgCenterY - Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go forward");
				if (y < (imgCenterY - Values.TOLERANCE))
				{
					System.out.println("PaperChaseAutoController: Go Down");

					drone.getCommandManager().down(Values.SPEED).doFor(Values.DOTIME);
					drone.getCommandManager().hover();
					
//					drone.getCommandManager().goLeft(Values.SPEED);
					Thread.currentThread().sleep(Values.DOTIME);
				}
				if (y > (imgCenterY - Values.TOLERANCE))
				{
					System.out.println("PaperChaseAutoController: Go Up");

					drone.getCommandManager().up(Values.SPEED).doFor(Values.DOTIME);
					drone.getCommandManager().hover();
//					drone.getCommandManager().goLeft(Values.SPEED);
					Thread.currentThread().sleep(Values.DOTIME);
					
				}
				
//				drone.getCommandManager().forward(Values.SPEED);
//				Thread.currentThread().sleep(Values.SLEEP);
			}
			else if (y > (imgCenterY + Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go backward");
//				drone.getCommandManager().backward(Values.SPEED);
//				Thread.currentThread().sleep(Values.SLEEP);
			}
			else
			{
				System.out.println("PaperChaseAutoController: Tag centered");
				drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_GREEN, 10, 5);
				
				
			}
		}
	}

}
