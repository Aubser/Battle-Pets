package edu.dselent.control.settings;

import edu.dselent.io.IoManager;
import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.io.Inputtable;
import edu.dselent.io.Outputtable;
import edu.dselent.settings.BattleSettings;
import edu.dselent.settings.FightSettings;
import edu.dselent.settings.SettingsConstants;
import edu.dselent.utils.StringConstants;
import edu.dselent.utils.StringConstants.StringKeys;

public class BattleSettingsControl
{
	// Since all methods use the same IoManager, I made it a class variable rather than retrieving it each time
	// Design choice
	private IoManager ioManager;
	private Outputtable output;
	private Inputtable input;

	private BattleSettings battleSettings;
	private FightSettingsControl fightSettingsControl;
	
	public BattleSettingsControl()
	{		
		ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		output = ioManager.getOutputSender();
		input = ioManager.getInputGetter();

		fightSettingsControl = new FightSettingsControl();
	}

	public BattleSettings getBattleSettings()
	{
		return battleSettings;
	}

	/**
	 * Facade for simple call to get all battle settings
	 * 
	 * @return All necessary settings to run the battle
	 */
	public BattleSettings retrieveBattleSettings()
	{
		int numberOfFights = retrieveNumberOfFights();

		FightSettings fightSettings = fightSettingsControl.retrieveFightSettings();
		battleSettings = new BattleSettings(numberOfFights, fightSettings);

		return battleSettings;
	}
	
	private Long retrieveRandomSeed()
	{
		Long randomSeed = null;
		
		while(randomSeed == null)
		{
			String outputMessage = StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_RANDOM_SEED_KEY);
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

	private int retrieveNumberOfFights()
	{	
		int numberOfFights = -1;
		boolean validNumberOfFights = false;
		
		while(!validNumberOfFights)
		{
			String outputMessage = StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_NUMBER_OF_FIGHTS_KEY);
			output.outputString(outputMessage);
			
			String numberOfFightsString = input.getString();
			
			try
			{
				numberOfFights = Integer.parseInt(numberOfFightsString);

				if(numberOfFights < SettingsConstants.MINIMUM_NUMBER_OF_FIGHTS)
				{
					String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_NUMBER_OF_FIGHTS_KEY, numberOfFightsString);
					output.outputString(errorMessage);
				}
				else
				{
					validNumberOfFights = true;
				}
			}
			catch(Exception e)
			{
				String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_NUMBER_OF_FIGHTS_KEY, numberOfFightsString);
				output.outputString(errorMessage);
			}
		}
		
		return numberOfFights;
	}
	
}
