package com.winestory.serverside.utils;

import com.winestory.serverside.vo.ServiceVO;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtil {
	
	private static Map<String, XMLConfiguration> xmlConfigMap = new HashMap<String, XMLConfiguration>();
	private static Configuration config;
	
	static {
		
		try{
			ConfigurationFactory factory = new ConfigurationFactory();
			String configFile = PropertiesUtil.getProperty("app_services");
			URL configURL = new File(configFile).toURL();
			factory.setConfigurationURL(configURL);
			config = factory.getConfiguration();
			XMLConfiguration xmlConfig = new XMLConfiguration(configFile);
			xmlConfigMap.put("APPXML", xmlConfig);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public ServiceVO getServiceClass(String serviceId){
		ServiceVO serviceVO = null;
		XMLConfiguration xmlConfiguration = getXmlConfig("APPXML");
		List<HierarchicalConfiguration> xmlFieldsList = xmlConfiguration.configurationsAt("ALL_SERVICES.SERVICE");
				for (HierarchicalConfiguration hierarchicalConfiguration : xmlFieldsList) {
					String xmlServiceId   = hierarchicalConfiguration.getString("SERVICE_ID");
					if(serviceId.equals(xmlServiceId)){
						serviceVO = new ServiceVO();
						serviceVO.setServiceClass(hierarchicalConfiguration.getString("CLASS_NAME"));
						serviceVO.setServiceID(serviceId);
						serviceVO.setQuery(hierarchicalConfiguration.getString("QUERY"));
					}
				}
		return serviceVO;
	}
	

	public static XMLConfiguration getXmlConfig(String xmlPath) {
		return xmlConfigMap.get(xmlPath);
	}

}
