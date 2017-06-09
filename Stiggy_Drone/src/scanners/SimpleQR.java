package scanners;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import centering.CircleARObject;
import de.yadrone.base.ARDrone;

public class SimpleQR {

	public static boolean moveQR(BufferedImage img, ARDrone drone){
		Result res = findQR(img);
		if(res == null){
			return false;
		} else {
			int x = 0;
			int y = 0;
			for (ResultPoint rp : res.getResultPoints()){
				x += rp.getX();
				y += rp.getY();
			}
			x = (int) (x/res.getResultPoints().length);
			y = (int) (y/res.getResultPoints().length);
			return CircleARObject.moveBasedOnLocation(drone, x, y, false);
		}
	}
	
	/**
	 * EZ FIND http://www.codepool.biz/zxing-write-read-qrcode.html
	 * @param img
	 * @return
	 */
	public static Result findQR(BufferedImage img){
	    BinaryBitmap binaryBitmap = null;
	    
	    Result resultToReturn = null;
	 
	    int[] pixelsOnQR = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
	    
	    RGBLuminanceSource luminance = new RGBLuminanceSource(img.getWidth(), img.getHeight(), pixelsOnQR);
	    
	    binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminance));
	 
	    if(binaryBitmap != null){
	    	QRCodeReader reader = new QRCodeReader();   
		    try {
		        resultToReturn = reader.decode(binaryBitmap);
		        System.out.println("QR Code data is: "+resultToReturn.getText());
		    } catch (NotFoundException | ChecksumException | FormatException e) {
		    	e.printStackTrace();
		    } 
	    }
	    return resultToReturn;
	}
}