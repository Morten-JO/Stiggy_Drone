package detection;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import centering.CircleARObject;
import controllers.BasicController;
import de.yadrone.base.ARDrone;
import frames.VideoFrame;
import helpers.Toolkit;
import helpers.Values;

public class CustomQRScanner {
	
	public String qrt = "";
	
	public int findQR(Mat frame, ARDrone drone, String currentQrToFind){
		Result result;
		try {
			result = getQRResult(frame);
		} catch (ChecksumException e) {
			e.printStackTrace();
			return 2;
		}
		if(result == null){
			VideoFrame.first = null;
			VideoFrame.second = null;
			VideoFrame.third = null;
		} else {
			int x = 0;
			int y = 0;
			for (ResultPoint rp : result.getResultPoints()){
				x += rp.getX();
				y += rp.getY();
			}
			x = (int) (x/result.getResultPoints().length);
			y = (int) (y/result.getResultPoints().length);

			boolean move = CircleARObject.moveBasedOnLocation(drone, x, y, false, BasicController.SEARCHQR);
			if(move){
				return 1;
			} else{
				return 0;
			}
		}
		return -1;
	}
	
	public Result getQRResult(Mat frame) throws ChecksumException{
		Mat tempImage = new Mat();
		frame.copyTo(tempImage);
		
		Imgproc.cvtColor(tempImage, tempImage, Imgproc.COLOR_RGB2GRAY);
		Imgproc.threshold(tempImage, tempImage, 100, 255, Imgproc.THRESH_BINARY);
		
		BufferedImage image = Toolkit.MatToBufferedImage(tempImage, BufferedImage.TYPE_BYTE_GRAY);

		VideoFrame.img2 = image;
		
		Result result = null;
	   
		LuminanceSource ls = new BufferedImageLuminanceSource((BufferedImage)image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(ls));
		QRCodeReader qrReader = new QRCodeReader();
		
		try {
			Map<DecodeHintType,Object> hints = new EnumMap<DecodeHintType,Object>(DecodeHintType.class);
		    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			result = qrReader.decode(bitmap, hints);
			System.out.println("QR Code data is: "+result.getText());
			qrt = result.getText();
			int x = 0;
			int y = 0;
			for (ResultPoint rp : result.getResultPoints()){
				x += rp.getX();
				y += rp.getY();
			}
			ResultPoint[] points = result.getResultPoints();
			ResultPoint a = points[1]; // top-left
			ResultPoint b = points[2]; // top-right
			
			
			double theta = 0;
			double zdist = Math.abs(a.getX() - b.getX());
			double xdist = Math.abs(a.getY() - b.getY());
			theta = (Math.atan(xdist / zdist));
			theta = theta * (180 / Math.PI);
			Values.THETA = measureAngle(result);
			Values.DISTANCE = measureDistance((float) zdist);
		
			x = (int) (x/result.getResultPoints().length);
			y = (int) (y/result.getResultPoints().length);
			qrt += "," + x + "," + y;
			
		} catch (NotFoundException e) {
		} catch (FormatException e) {
		}
		return result;
	}
	
	public double measureDistance(float pixels){
		return Values.REALWIDTH * (Values.FOCAL / pixels);	
	}
	
	public double measureAngle(Result result){
		double angle = 0;
		ResultPoint[] points = result.getResultPoints();
		
		float bottomLeftX = points[0].getX();
		float bottomLeftY = points[0].getY();
		float topLeftX = points[1].getX();
		float topLeftY = points[1].getY();
		float topRightX = points[2].getX();
		float topRightY = points[2].getY();
		
		VideoFrame.first = new Point((int)bottomLeftX,(int)bottomLeftY);
		VideoFrame.second = new Point((int)topLeftX, (int)topLeftY);
		VideoFrame.third = new Point((int)topRightX, (int)topRightY);
		
		float leftSide = (bottomLeftY - topLeftY);
		float topSide = topRightX - topLeftX;
		float hypo =  (float) Math.sqrt((Math.pow((double) bottomLeftY-topRightY, 2)) + (Math.pow((double) bottomLeftX-topRightX, 2)));
		
		
		angle = Math.acos((Math.pow((double) topSide, 2)+ Math.pow((double) leftSide, 2) - Math.pow((double) hypo, 2))
				/(2*leftSide*topSide))* (180 / Math.PI);;
		
		return angle;
	}
}
