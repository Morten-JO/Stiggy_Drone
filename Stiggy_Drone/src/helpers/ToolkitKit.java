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
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class ToolkitKit {

	public static boolean saveImage(BufferedImage img, String text){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String index = dateFormat.format(date);
		File outputfile = new File("saved"+index+"_"+text+".png");
		try {
			outputfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	public static BufferedImage MatToBufferedImage(Mat matrix, BufferedImage bimg)
	{
	    if ( matrix != null ) { 
	        int cols = matrix.cols();  
	        int rows = matrix.rows();  
	        int elemSize = (int)matrix.elemSize();  
	        byte[] data = new byte[cols * rows * elemSize];  
	        int type;  
	        matrix.get(0, 0, data);  
	        switch (matrix.channels()) {  
	        case 1:  
	            type = BufferedImage.TYPE_BYTE_GRAY;  
	            break;  
	        case 3:  
	            type = BufferedImage.TYPE_3BYTE_BGR;  
	            // bgr to rgb  
	            byte b;  
	            for(int i=0; i<data.length; i=i+3) {  
	                b = data[i];  
	                data[i] = data[i+2];  
	                data[i+2] = b;  
	            }  
	            break;  
	        default:  
	            return null;  
	        }  

	        // Reuse existing BufferedImage if possible
	        if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
	        	if(cols > 1 && rows > 1){
	        		bimg = new BufferedImage(cols, rows, type);
	        	} else{
	        		bimg = new BufferedImage(1,1,type);
	        	}
	            
	        }        
	        bimg.getRaster().setDataElements(0, 0, cols, rows, data);
	    } else { // mat was null
	        bimg = null;
	    }
	    return bimg;  
	} 
	
	public static BufferedImage MatToBufferedImage(Mat m, int type){
		BufferedImage gray = new BufferedImage(m.width(), m.height(), type);
		byte[] bytedata = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
		m.get(0, 0, bytedata);
		return gray;
	}
	
	
}
