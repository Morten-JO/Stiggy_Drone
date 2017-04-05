package main;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import de.yadrone.apps.paperchase.PaperChase;
import de.yadrone.apps.paperchase.TagListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.LEDAnimation;

public class QRController  {


	public void printCoordinates(Result res){
		
		ResultPoint[] points = res.getResultPoints();
		
		
		for (int i = 0; i < points.length; i++){
			
			System.out.println(points[i]);
		}
		System.out.println("There are " + points.length + " points");
		System.out.println("Top line is " +  (points[2].getX()-points[1].getX())+ " pixels" );
	}
	
	public void centerTag(Result tag, ARDrone drone) throws InterruptedException
	{
		if(!Main.userControl){
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

				drone.getCommandManager().goLeft(Values.SPEED).doFor(100);
				drone.getCommandManager().hover();
//				drone.getCommandManager().goLeft(Values.SPEED);
//				Thread.currentThread().sleep(Values.SLEEP);
			}
			else if (x > (imgCenterX + Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go right");
				drone.getCommandManager().goRight(Values.SPEED).doFor(100);
				drone.getCommandManager().hover();
//				drone.getCommandManager().goRight(Values.SPEED);
//				Thread.currentThread().sleep(Values.SLEEP);
			}
			else if (y < (imgCenterY - Values.TOLERANCE))
			{
				System.out.println("PaperChaseAutoController: Go forward");
				
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
