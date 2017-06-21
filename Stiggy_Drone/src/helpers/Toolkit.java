package helpers;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Toolkit {

	public static boolean saveImage(BufferedImage img, String text){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String index = dateFormat.format(date);
		File outputfile = new File("saved"+index+"_"+text+".png");
		try {
			outputfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException l) {
			l.printStackTrace();
		}
		return true;
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
	
	public static BufferedImage MatToBufferedImage(Mat originalMat, int type){
		
		BufferedImage imageOfType = new BufferedImage(originalMat.width(), originalMat.height(), type);
		
		byte[] byteDataOfImage = ((DataBufferByte) imageOfType.getRaster().getDataBuffer()).getData();
		originalMat.get(0, 0, byteDataOfImage);
		return imageOfType;
	}
	
	
}
