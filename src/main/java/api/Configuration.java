package api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Class Configuration
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Configuration
{
    private Feed[] feeds;
    private File configFile;

    public Configuration(String pathToConfig)
    {
        configFile = new File(pathToConfig);
        if(!configFile.exists())
        {
            try
            {
                configFile.createNewFile();
            }
            catch(IOException err)
            {
                System.out.println("Could not create configuration file");
            }
        }
    }

    public void saveConfigurationFile()
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.writeValue(configFile, this);
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    public void loadConfigurationFile()
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.readValue(configFile, Configuration.class);
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    public Feed[] getFeeds()
    {
        return feeds;
    }

    public File getConfigFile()
    {
        return configFile;
    }

    public void setFeeds(Feed[] feeds)
    {
        this.feeds = feeds;
    }

    public void setConfigFile(File configFile)
    {
        this.configFile = configFile;
    }
}

// Created: 2016-04-19
