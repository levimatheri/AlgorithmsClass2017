/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.utilities;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author cjedwards1
 */
@WebListener
public class ContextListener implements ServletContextListener
{   
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try 
        {
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(new Parameters().properties()
                                    .setFileName("main.properties")
                                    .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
            
            ServletFunctions.properties = builder.getConfiguration();
            
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            rootLogger.setLevel(Level.parse(ServletFunctions.properties.getString("log.level", "INFO")));
            for (Handler h : handlers) 
            {
                if(h instanceof FileHandler)
                    h.setLevel(Level.parse(ServletFunctions.properties.getString("log.level", "INFO")));
            }
        }
        catch (ConfigurationException ex) 
        {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        //Example of how to make a configuration file.
//        try
//        {
//            PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
//            layout.setHeaderComment("The main properties for the website.");
//            layout.setBlancLinesBefore("log.level", 1);
//            layout.setComment("log.level", "Will show the different levels of the logger. Levels: INFO, FINE, FINER, FINNEST");
//            layout.setBlancLinesBefore("name.database", 1);
//            layout.setComment("name.database", "The different names the website needs to access different data.");
//            

//            
//            Configuration config = builder.getConfiguration();
//            config.addProperty("include", "testing.properties");
//            config.addProperty("include", "excel.properties");
//            config.setProperty("log.level", "INFO");
//            config.setProperty("name.database", "WESTNILESPREAD");
//            config.setProperty("name.website", "WestNileSpread");
//            builder.save();
//        } 
//        catch (ConfigurationException ex) 
//        {
//            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}