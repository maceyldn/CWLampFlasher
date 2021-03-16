package uk.co.m0mux.CWLampFlasher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

import uk.co.m0mux.CWLampFlasher.utils.MyProperties;

public class PinOutputThread extends Thread  {

	
	private static final Logger Logger = LogManager.getLogger(PinOutputThread.class.getName());
	
    private MyProperties appProperties;

	private int port;
	public GpioPinDigitalOutput pin;

	private GpioController gpio;

	private String morse;

	private volatile boolean running = true;
			
	private int DIT_MS	=	500;				/* 240mS => 5WPM  360 => 3.7 WPM  480 => 2.5 WPM */
	
	public void setSpeed(int dIT_MS) {
		DIT_MS = dIT_MS;
	}

	public int getSpeed() {
		return DIT_MS;
	}


	private int DAH_MS		=	(DIT_MS*3);
	private int SPACE_MS	=	(DIT_MS*3);
    
    public PinOutputThread(MyProperties _properties, int _port, GpioController _gpio, int speed, String _string) {
    	this.appProperties = _properties;
    	this.port = _port;
    	this.gpio = _gpio;
    	this.morse = _string;
    	this.DIT_MS = speed;
    	
    	DAH_MS = (DIT_MS*3);
    	SPACE_MS = (DIT_MS*3);
    	
    	Logger.info(this);
    	
    	if (gpio != null)
    	{
    	switch (port)
    	{
    	case 1:
    		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "output");
    		break;
    	case 2:
    		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "output");
    		break;
    	case 3:
    		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "output");
    		break;
    	case 4:
    		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "output");
    		break;
    	case 5:
    		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "output");
    		break;
    	}
    	} else {
    		Logger.fatal("No Wiring Pi harness found");
    	}
    	
    }
    
	
	@Override
	public String toString() {
		return "PinOutputThread [port=" + port + ", morse=" + morse + ", DIT_MS=" + DIT_MS + "]";
	}

	public String getMorse() {
		return morse;
	}

	public void setMorse(String morse) {
		this.morse = morse;
	}

	public void run() {
		
		Logger.info("Start Flash Thread for OP: " + port);
								
		while(running) {
			
			for (char c : morse.toCharArray())
			{
				if (running)
				{
				switch (c)
				{
				case '.':
					SendDot();
					break;
				case '-':
					SendDash();
					break;
				case ' ':
					SendSpace();
					break;
				default:
					break;
				}
				}
				//Logger.debug(c);
					
			}
			if (running)
			{
				SendFullOn();
			}
			}
			
		
		Logger.info("Thread has ended. Hurrah!");
	}

	private void SendDot() {
		
		try {
			pin.high();
			Thread.sleep(DIT_MS);			
			pin.low();
			Thread.sleep(DIT_MS);
			} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void SendDash() {
		try {
			pin.high();
			Thread.sleep(DAH_MS);			
			pin.low();
			Thread.sleep(DIT_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	private void SendSpace() {
		
		try {
			pin.low();
			Thread.sleep(SPACE_MS);
			pin.low();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void SendFullOn() {
		try {
			//Logger.debug("Full on");
			pin.high();
			Thread.sleep(DAH_MS*10);
			pin.low();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override  
	public void interrupt(){  
		Logger.info("Received interrupt. Closing");
		running = false;

		if (gpio != null)
		{
		gpio.unprovisionPin(pin);
		}
		
		//return;
	}
	
}
