package uk.co.m0mux.CWLampFlasher;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;

import uk.co.m0mux.CWLampFlasher.Morse.CWUtils;
import uk.co.m0mux.CWLampFlasher.utils.MyProperties;

public class PinThreadController {
	
	
	private static final Logger Logger = LogManager.getLogger(PinThreadController.class.getName());

    public TreeMap<Integer,PinOutputThread> pinthreads = new TreeMap<Integer, PinOutputThread>();
    private final MyProperties properties;
    
    private static boolean alreadyInstantiated = false;
    
    private GpioController gpio;

	public PinThreadController (MyProperties _properties, GpioController _gpio) {
		Logger.info("Starting PinOutputThreadController.");
		properties = _properties;
		gpio = _gpio;
		
		if (alreadyInstantiated == true) { 
			Logger.error("Looks like you're trying to spin up two PinOutputThreadControllers. Not good. Exiting.");
			System.exit(-1);
			}
		
		alreadyInstantiated = true;
		setupOutputThreads();
    }

	private void setupOutputThreads() {
    	
		int pin = 1;
		String s = "cq cq cq test g8t g8t baalieve in better";
		
		PinOutputThread t1 = new PinOutputThread(properties, pin, gpio, 480, CWUtils.decodeEnglish(s));
		t1.setName("POT-"+pin);
		pinthreads.put(pin, t1);
		
		pin = 2;
		s = "cq cq cq test g8t g8t baalieve in better";
		PinOutputThread t2 = new PinOutputThread(properties, pin, gpio, 480, CWUtils.decodeEnglish(s));
		t2.setName("POT-"+pin);
		pinthreads.put(pin, t2);
		
		
		pin = 3;
		s = "cq cq cq test g8t g8t baalieve in better";
		PinOutputThread t3 = new PinOutputThread(properties, pin, gpio, 480, CWUtils.decodeEnglish(s));
		t3.setName("POT-"+pin);
		pinthreads.put(pin, t3);
		
		pin = 4;
		s = "cq cq cq test g8t g8t baalieve in better";
		PinOutputThread t4 = new PinOutputThread(properties, pin, gpio, 480, CWUtils.decodeEnglish(s));
		t4.setName("POT-"+pin);
		pinthreads.put(pin, t4);
		
		pinthreads.get(1).start();
		pinthreads.get(2).start();		
		pinthreads.get(3).start();
		pinthreads.get(4).start();
		
    }
	
	public void updateOutputThread(int pin, String cwtext, int speed)
	{
		try {
			Logger.info("Update called");
			
			
			//Kill the Thread.
			pinthreads.get(pin).interrupt();
			
			//Sleep a second to ensure its dead.
			Thread.sleep(2000);
			
			//Remove Pin from Thread Pool.
			pinthreads.remove(pin);
			
			Logger.info("Restarting Thread with updated");
			//Insert new Thread to pool.
			PinOutputThread t2 = new PinOutputThread(properties, pin, gpio, speed, CWUtils.decodeEnglish(cwtext));
			t2.setName("POT-"+pin);
			pinthreads.put(pin, t2);
				
			//Start new thread
			pinthreads.get(pin).start();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gracefulShutdown() 
	{
		Logger.debug("Gracefully shutting down. Sending request to Threads");
		for (Entry<Integer, PinOutputThread> kvp : pinthreads.entrySet())
		{
			kvp.getValue().interrupt();
		}
	}
    
    
    public void stopOutputPinThreads()
    {
        try 
        {
        	Logger.info("Stopping Output Pin Thread");
        	for (Entry<Integer, PinOutputThread> kvp : pinthreads.entrySet())
    		{
    			kvp.getValue().interrupt();
    			
    		}	
        } catch (Exception ex) {
        	Logger.error("Something went wrong killing off the output threads");
        }
    }
}