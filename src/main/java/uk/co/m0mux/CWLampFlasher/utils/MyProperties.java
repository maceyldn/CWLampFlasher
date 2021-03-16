package uk.co.m0mux.CWLampFlasher.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

public class MyProperties  {

	private static final Logger Logger = LogManager.getLogger(MyProperties.class.getName());

	private Properties prop;

	
	public MyProperties(String propertiesFileName, ArrayList<String> requiredPropertiesList)  
	{
		InputStream newInputStream;
		
        try 
        {
            newInputStream = new FileInputStream(propertiesFileName); 
			loadInPropertiesData(newInputStream, requiredPropertiesList);
			
        } catch (FileNotFoundException e) 
        {
            Logger.error("Unable to load the properties file. Not found: [" + propertiesFileName + "].");
            e.printStackTrace();
            throw new IllegalArgumentException("Unable to load the properties file. Not found: [" + propertiesFileName + "].");
        }
	}
		
	
	public MyProperties(InputStream newInputStream, ArrayList<String> requiredPropertiesList)
    {
	    loadInPropertiesData(newInputStream, requiredPropertiesList);
	    
	    try {
            newInputStream.close();
        } catch (IOException e) {
            Logger.warn("Unable to close MyProperties InputStream. Reason Message: [" + e.getMessage() + "]");
        }
	    
    }


    public void loadInPropertiesData(InputStream receivedInputStream, ArrayList<String> requiredProperties)
    {
       try 
       {
            prop = new Properties();             
 
            if (receivedInputStream != null) 
            {
                prop.load(receivedInputStream);
            } 
            else
            {
                Logger.error("Unable to load the properties file. Input stream is null.");	                
            }
 
            //Check if all the properties we need are present...
            final ArrayList<String> missingPropertiesList = new ArrayList<String>();
            for (final String requiredProperty: requiredProperties) {
                if (prop.getProperty(requiredProperty) == null) {
                    missingPropertiesList.add(requiredProperty);
                }               
            }            
            // If there are any items in the missing properties list, fail!         
            for (final String missedProperty : missingPropertiesList) {
                Logger.error("Unable to find property ["+missedProperty+"] in the config file.");
            }
            if ( ! missingPropertiesList.isEmpty()) throw new IllegalArgumentException("Missing the following properties [" + missingPropertiesList.toString() + "] in the configuration file.");
        
       } catch (Exception e) 
       {
            Logger.error("Exception: [" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()) + "].");
            throw new IllegalArgumentException("Exception [" + e.getMessage() + "].");
       } finally 
       {
            try 
            {
                if (receivedInputStream != null) receivedInputStream.close();
            } catch (IOException e) {
                Logger.error("Exception: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()) + ".");
                throw new IllegalArgumentException("An IOException was thrown whilst trying to load the properties data from the file. Reason message: [" + e.getMessage() + "].");
            }
        }  
    }

    
    
    
    public @NonNull String getProperty(final String key) 
    {
        String result = prop.getProperty(key);
        if (result == null) return "";
        return result;		
	}

    public @NonNull boolean getBooleanProperty(final String key) 
    {
        String result = prop.getProperty(key);
        if (result == null) return false;
        if (result == "true") return true;
        if (result == "false") return false;
        return false;
	}

    
	public int getIntProperty(final String key) {		
	    
	    String requestedProperty = prop.getProperty(key);
	    
	    if (requestedProperty == null) 
	    {
	        Logger.error("Integer property [" + key + "] is null. It's missing from the config file. Please add it and try again.");
	        throw new IllegalArgumentException("Property [" + key + "] is null (it's missing from the config file).");	        
	    }
	    
	    //Remove any whitespace.
	    requestedProperty = requestedProperty.replaceAll("\\s","");
	    	    
		try 
		{
			return Integer.parseInt(requestedProperty);				
		} 
		catch (NumberFormatException e) 
		{
			Logger.error("Tried to convert config key ["+key+"] of value ["+prop.getProperty(key)+"] to an integer, but failed: [" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()) + "].");
			throw new IllegalArgumentException("Tried to convert config key ["+key+"] of value ["+prop.getProperty(key)+"] to an integer, but failed: [" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()) + "].");		
		}
		
	}
	
	
}