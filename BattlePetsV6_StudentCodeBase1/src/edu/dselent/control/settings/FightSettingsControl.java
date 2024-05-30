package edu.dselent.control.settings;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.io.Inputtable;
import edu.dselent.io.IoManager;
import edu.dselent.io.Outputtable;
import edu.dselent.settings.FightSettings;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.settings.SettingsConstants;
import edu.dselent.utils.StringConstants;
import edu.dselent.utils.StringConstants.StringKeys;

import java.util.List;

public class FightSettingsControl
{
	private IoManager ioManager;
	private Outputtable output;
	private Inputtable input;

	private FightSettings fightSettings;
	private PlayerSettingsControl playerSettingsControl;

	public FightSettingsControl()
	{		
		ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		output = ioManager.getOutputSender();
		input = ioManager.getInputGetter();

		playerSettingsControl = new PlayerSettingsControl();
	}

	public FightSettings getFightSettings()
	{
		return fightSettings;
	}

	/**
	 * Facade for simple call to get all fight settings
	 * 
	 * @return All necessary settings to run the fight
	 */
	public FightSettings retrieveFightSettings()
	{
		long randomSeed = retrieveRandomSeed();
		int numberOfPlayers = retrieveNumberOfPlayers();
		List<PlayerSettings> playerSettingsList = playerSettingsControl.retrievePlayerSettingsList(numberOfPlayers);

		fightSettings = new FightSettings(randomSeed, numberOfPlayers, playerSettingsList);

		return fightSettings;
	}
	
	private Long retrieveRandomSeed()
	{
		Long randomSeed = null;
		
		while(randomSeed == null)
		{
			String outputMessage = StringConstants.getFormattedString(StringKeys.ENTER_RANDOM_SEED_KEY);
			output.outputString(outputMessage);
			
			String randomSeedString = input.getString();
			
			try
			{
				randomSeed = Long.parseLong(randomSeedString);
			}
			catch(Exception e)
			{
				String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_RANDOM_SEED_KEY, randomSeedString);
				output.outputString(errorMessage);
			}

		}
		
		return randomSeed;
	}
	
	private int retrieveNumberOfPlayers()
	{
		int numberOfPlayers = -1;
		boolean validNumberOfPlayers = false;
		
		while(!validNumberOfPlayers)
		{
			String outputMessage = StringConstants.getFormattedString(StringKeys.ENTER_NUMBER_OF_PLAYERS_KEY);
			output.outputString(outputMessage);
			
			String numberOfPlayersString = input.getString();
			
			try
			{
				numberOfPlayers = Integer.parseInt(numberOfPlayersString);

				if(numberOfPlayers < SettingsConstants.MINIMUM_NUMBER_OF_PLAYERS)
				{
					String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_NUMBER_OF_PLAYERS_KEY, numberOfPlayersString);
					output.outputString(errorMessage);
				}
				else
				{
					validNumberOfPlayers = true;
				}
			}
			catch(Exception e)
			{
				String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_NUMBER_OF_PLAYERS_KEY, numberOfPlayersString);
				output.outputString(errorMessage);
			}
		}
		
		return numberOfPlayers;
	}
}
