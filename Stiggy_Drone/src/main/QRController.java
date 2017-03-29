package main;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import de.yadrone.apps.paperchase.TagListener;

public class QRController  {


	public void printCoordinates(Result res){
		
		ResultPoint[] points = res.getResultPoints();
		
		for (int i = 0; i < points.length; i++){
			
			System.out.println(points[i]);
		}
	}

}
