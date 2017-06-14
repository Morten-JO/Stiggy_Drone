package scanners;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
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

	public static int moveQR(BufferedImage img, ARDrone drone){
		Result res;
		try {
			res = findQR(img);
		} catch (ChecksumException e) {
			return 2;
		}
		if(res == null){
			return -1;
		} else {
			int x = 0;
			int y = 0;
			for (ResultPoint rp : res.getResultPoints()){
				x += rp.getX();
				y += rp.getY();
			}
			x = (int) (x/res.getResultPoints().length);
			y = (int) (y/res.getResultPoints().length);
			boolean move = CircleARObject.moveBasedOnLocation(drone, x, y, false);
			if(move){
				return 1;
			} else{
				return 0;
			}
		}
	}
	
	/**
	 * EZ FIND http://www.codepool.biz/zxing-write-read-qrcode.html
	 * @param img
	 * @return
	 */
	public static Result findQR(BufferedImage img) throws ChecksumException{
		Map<DecodeHintType,Object> hints = new EnumMap<DecodeHintType,Object>(DecodeHintType.class);
	    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		
		BinaryBitmap binaryBitmap = null;
	    
	    Result resultToReturn = null;
	 
	    int[] pixelsOnQR = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
	    
	    RGBLuminanceSource luminance = new RGBLuminanceSource(img.getWidth(), img.getHeight(), pixelsOnQR);
	    
	    binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminance));
	 
	    if(binaryBitmap != null){
	    	QRCodeReader reader = new QRCodeReader();   
		    try {
		        resultToReturn = reader.decode(binaryBitmap, hints);
		        System.out.println("QR Code data is: "+resultToReturn.getText());
		    } catch (NotFoundException e){
		    	e.printStackTrace();
		    } catch(FormatException g){
		    	 g.printStackTrace();
		    } 
	    }
	    return resultToReturn;
	}
}