package com.zzb.configuration;



import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;

/**
 * Configuration manager class which loads all 'config-definition.xml' files and
 * creates a Configuration object which holds all properties from the underlying
 * configuration resource
 */
public class ConfigurationManager {

  /** configuration definition file name. */
  private static String DEFAULT_CONFIG_DEFINITION_FILE_NAME = "config-definition.xml";

  /** Stores a map with the configuration for each path specified. */
  private static Map<String, Configuration> configurationsCache = new HashMap<String, Configuration>();

//  /** The Constant LOGGER. */
//  private static final Logger LOGGER = Logger
//      .getLogger(ConfigurationManager.class);

  /**
   * Common method to load content of all configuration resources defined in
   * 'config-definition.xml'.
   * 
   * @param configDefFilePath
   *          the config def file path
   * @return Configuration
   */
  public static Configuration getConfiguration(String configDefFilePath) {
    if (configurationsCache.containsKey(configDefFilePath)) {
      return configurationsCache.get(configDefFilePath);
    }
    CombinedConfiguration configuration = null;
    synchronized (configurationsCache) {
      if (configurationsCache.containsKey(configDefFilePath)) {
        return configurationsCache.get(configDefFilePath);
      }
      DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
      String fielPath = getConfigDefFilePath(configDefFilePath);
//      LOGGER.info("loading from 'configDefFilePath' :" + fielPath);
      builder.setFile(new File(fielPath));
      try {
        configuration = builder.getConfiguration(true);
        configurationsCache.put(fielPath, configuration);
      } catch (ConfigurationException e) {
//        LOGGER.info("Exception in loading property files.", e);
      }
    }
    return configuration;
  }

  /**
   * Removes the configuration created from a config definition file located at
   * 'configDefFilePath'.
   * 
   * @param configDefFilePath
   *          path to the config definition file
   */
  public static void clearConfiguration(String configDefFilePath) {
    configurationsCache.remove(configDefFilePath);
  }

  /**
   * Gets the configuration.
   * 
   * @return the configuration
   */
  public static Configuration getConfiguration() {
    return getConfiguration(null);
  }

  /**
   * Returns the 'config-definition.xml' file path. 1. If the param
   * 'configDefFilePath' has a valid value, returns configDefFilePath 2. If the
   * system property key 'configDefFilePath' has a valid value, returns the
   * value 3. By default, it returns the file name 'config-definition.xml'
   * 
   * @param configDefFilePath
   *          given input path to the config definition file
   * @return the config def file path
   */
  private static String getConfigDefFilePath(String configDefFilePath) {
    if (StringUtils.isNotEmpty(configDefFilePath)) {
      return configDefFilePath;
    }
    return DEFAULT_CONFIG_DEFINITION_FILE_NAME;
  }

  /**
   * The main method.
   * 
   * @param args
   *          the args
   * @throws InterruptedException
   *           the interrupted exception
   */
  public static void main(String[] args) throws InterruptedException {
    Configuration config = ConfigurationManager
        .getConfiguration("/Users/root/Documents/config/config-definition-dpi.xml");
    System.out.println("elastic.search.cluster ="
        + config.getString("elastic.search.cluster"));
    Thread.sleep(10000); 
  }

}