package main;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRClass {
	
	Result result;

	
	public Result getResult(BufferedImage img){
		
		LuminanceSource source = new BufferedImageLuminanceSource(img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		
		QRCodeReader reader = new QRCodeReader();
		
			try {
				result = reader.decode(bitmap);
			} catch (NotFoundException e) {
				
				e.printStackTrace();
				return null;
			} catch (ChecksumException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		
		
		return result;
				
	}
	
}
