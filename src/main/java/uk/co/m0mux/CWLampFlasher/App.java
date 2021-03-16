package uk.co.m0mux.CWLampFlasher;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import uk.co.m0mux.CWLampFlasher.utils.MyProperties;

@SuppressWarnings("serial")
public class App extends HttpServlet
{
	private static final org.apache.logging.log4j.Logger Logger = LogManager.getLogger(App.class.getName());
	GpioController gpio;
	private static MyProperties appProperties;
	private static PinThreadController ptc;
	


	public static PinThreadController getPtc() {
		return ptc;
	}

	private static String configFilename = "/opt/cwlampflasher/cwlf.properties";
	//private static String configFilename = "c:/dev/cwlf.properties";
	
    public void init() throws ServletException
    {
    	Logger.info("=====================================================================");
		Logger.info("======================== CWLampFlasher ==============================");
		Logger.info("====================  Andy Mace 2020/2021 ===========================");
		Logger.info("=====================================================================");
		
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			configFilename = "c:/dev/cwlf.properties";	
		}
		
		ArrayList<String> requiredProperties = new ArrayList<String>(Arrays.asList("appName"));
		appProperties = new MyProperties(configFilename, requiredProperties);
	
		Logger.info("App Name is : ["+ appProperties.getProperty("appName") +"]");
		//Logger.info("Listening on: ["+ appProperties.getProperty("localIPAddress") + ":" + appProperties.getProperty("listeningPort")+ "]");
		Logger.info("==============================================================");
		    		
	   
          
        try {
          
        	if (!System.getProperty("os.name").startsWith("Windows"))
    		{
    		
        	
        	// create gpio controller
          gpio = GpioFactory.getInstance();

    		}
          } catch (UnsatisfiedLinkError e) {
        	  Logger.fatal("Can't find WiringPi Library.... Are you running on a pi?");
        	  System.exit(0);
          }
         
          ptc = new PinThreadController(appProperties, gpio);
          
    }
    
    public void destroy()
    {
    	ptc.gracefulShutdown(); 
    }
}