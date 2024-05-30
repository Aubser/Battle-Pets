package edu.dselent.unused;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import edu.dselent.player.PetTypes;



//singleton
/*
 * There can be only one default regardless of how many games/battles there are
 * Read information from properties file
 */
public class DefaultConfig
{
	private static DefaultConfig instance;
	public static String DEFAULT_PROPERTIES_PATH = "src" + File.separator + "resources" + File.separator + "properties.properties";
	private Properties properties;
	
	private static final int DEFAULT_HP = 100;
	private static final int DEFAULT_NUMBER_OF_PLAYERS = 2;
	private static String DEFAULT_PLAYER_NAME_PREFIX = "Player ";
	private static String DEFAULT_PET_NAME_PREFIX = "Pet ";
	private static PetTypes DEFAULT_PET_TYPE = PetTypes.POWER;
	
	
	private DefaultConfig(String propertiesPath)
	{
		properties = new Properties();
		
		try
		{
			FileChannel channel = FileChannel.open(Paths.get(propertiesPath), StandardOpenOption.READ);
			properties.load(Channels.newInputStream(channel));
		}
		catch(IOException ioe)
		{
			// store hard-coded defaults
			properties = new Properties();
			
		}
	}
	
	public static synchronized DefaultConfig getInstance()
	{
		if(instance != null)
		{
			instance = new DefaultConfig(DEFAULT_PROPERTIES_PATH);
		}
		
		return instance;
	}
	
	public static synchronized DefaultConfig getInstance(String propertiesPath)
	{
		if(instance != null)
		{
			instance = new DefaultConfig(propertiesPath);
		}
		
		return instance;
	}
}
