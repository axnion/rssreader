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

    public Configuration()
    {
        feeds = null;
        configFile = null;
    }

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

    public void update()
    {
        // Check Feed for updates, returns true if found something new, if not false.
        // If previous was true then update itemList, if false do not update.
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
            Configuration config = mapper.readValue(configFile, Configuration.class);
            feeds = config.feeds;
            configFile = config.configFile;
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

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
