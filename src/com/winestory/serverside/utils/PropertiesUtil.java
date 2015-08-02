package com.winestory.serverside.utils;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil
{
 
  private static Properties props;
 
  static
  {
    props = new Properties();
    try{
      PropertiesUtil util = new PropertiesUtil();


      String envFilePath = "conf/properties/env.properties";
      
      props = util.getPropertiesFromClasspath(envFilePath);
    }catch (FileNotFoundException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }catch(Exception e){
    	e.printStackTrace();
    }
  }
 
  // private constructor
  private PropertiesUtil(){
	  
  }
 
  public static String getProperty(String key){
    return props.getProperty(key);
  }
 
  public static Set<Object> getkeys()  {
    return props.keySet();
  }
 
  public static Properties getProperties()  {
    return props;
  }
 
  /**
   * loads properties file from classpath
   *
   * @param propFileName
   * @return
   * @throws IOException
   */
  private Properties getPropertiesFromClasspath(String propFileName) throws IOException {
    Properties props = new Properties();
    InputStream inputStream = null;
    try{
    	String absolute = getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm();
    	absolute = absolute.substring(6);
    	inputStream = new FileInputStream(propFileName);
	    if (inputStream == null){
	        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
	    }
      props.load(inputStream);
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
      inputStream.close();
    }
    return props;
  }
}