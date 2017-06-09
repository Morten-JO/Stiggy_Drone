package scanners;

import java.awt.image.BufferedImage;
import java.util.Scanner;

import org.opencv.core.Core;

import centering.CircleARObject;
import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CalibrationCommand;
import de.yadrone.base.command.Device;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.video.ImageListener;
import helpers.Toolkit;
import scanners.CustomQRScanner;

public class AltitudeTest {
	
	
	private ARDrone drone;
	public static boolean print = false;
	static int altitude = 0;
	CustomQRScanner qrScan;
	static boolean weThrough = false;
	static boolean takeOff = false;
	
	public AltitudeTest(){
		
		drone = new ARDrone();
		qrScan = new CustomQRScanner();
	}
	
	public void flyplis(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.drone.start();
		AltitudeListener listener =new AltitudeListener() {
			
			@Override
			public void receivedExtendedAltitude(Altitude arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receivedAltitude(int arg0) {
				altitude = arg0;
				if (arg0 > 500){
					
					takeOff = true;
				}
				if (takeOff && arg0 < 200){
					weThrough = true;
					
				}
				if (weThrough){
					System.out.println("We are through!");
				}
				if(print){
					System.out.println(arg0);
				}
				
				print = false;
				
			}
		};
		drone.getNavDataManager().addAltitudeListener(listener);
		
		drone.getVideoManager().addImageListener(new ImageListener() {
			
			@Override
			public void imageUpdated(BufferedImage arg0) {
				
				qrScan.applyFilters(Toolkit.bufferedImageToMat(arg0), drone);
				
			}
		});
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				Scanner scan = new Scanner(System.in);
				while (true){
					String in = scan.next();
					switch (in){
					
					case "y":
						drone.landing();
						break;
					case "f":
						drone.freeze();
					case "d":
						drone.getCommandManager().down(10).doFor(1000);
						break;
					case "w" :
						print = true;
						
			
						
						//if (altitude < 1500){
							drone.getCommandManager().up(10).doFor(1000);
							
							drone.getCommandManager().setCommand(new CalibrationCommand(Device.MAGNETOMETER));
							//}
						break;
					case "re":
						drone.reset();
						System.out.println("Reset");
						break;
						
					case "t":
						drone.getCommandManager().takeOff().doFor(5000);
						
						System.out.println("Take off");
						break;
					
					case "die": 
						drone.stop();
						System.exit(0);
						break;
					case "o" :
						drone.getCommandManager().forward(10).doFor(1000);
						break;
					
				case "l" :
					drone.getCommandManager().backward(10).doFor(1000);
					break;
				case "æ" :
					drone.getCommandManager().goRight(10).doFor(1000);
					break;
				case "k" :
					drone.getCommandManager().goLeft(10).doFor(1000);
					break;
				}
				}
					
				}
				
			
			
		});
		t.start();
		this.drone.getCommandManager().setMaxAltitude(1850);
		//drone.getCommandManager().takeOff().doFor(5000);
		
		
		
		
	}
	
	public static void main(String[] args){
		
		AltitudeTest test = new AltitudeTest();
		
		test.flyplis();
		
		
		
	}

}
