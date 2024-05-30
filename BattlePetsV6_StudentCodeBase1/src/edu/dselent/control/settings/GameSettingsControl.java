package edu.dselent.control.settings;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.io.Inputtable;
import edu.dselent.io.IoManager;
import edu.dselent.io.Outputtable;
import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.BattleSettings;
import edu.dselent.settings.GameModes;
import edu.dselent.settings.GameSettings;
import edu.dselent.settings.SettingsConstants;
import edu.dselent.utils.StringConstants;
import edu.dselent.utils.StringConstants.StringKeys;

public class GameSettingsControl
{
	private IoManager ioManager;
	private Outputtable output;
	private Inputtable input;

	private GameSettings gameSettings;

	public GameSettingsControl()
	{		
		ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		output = ioManager.getOutputSender();
		input = ioManager.getInputGetter();
	}

	public GameSettings getGameSettings()
	{
		return gameSettings;
	}

	/**
	 * Facade for simple call to get all game settings
	 * 
	 * @return All necessary settings to run the game
	 */
	public GameSettings retrieveGameSettings()
	{
		GameModes gameMode = retrieveGameMode();
		gameSettings = new GameSettings(gameMode);

		return gameSettings;
	}

	GameModes retrieveGameMode()
	{
		GameModes gameMode = null;
		int gameModeInt;

		while(gameMode == null)
		{
			String outputMessage1 = StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_GAME_MODE_KEY);
			output.outputString(outputMessage1);

			String outputMessage2 = StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_GAME_MODE_CHOICES_KEY);
			output.outputString(outputMessage2);

			String gameModeString = input.getString();

			try
			{
				gameModeInt = Integer.parseInt(gameModeString);
				gameMode = GameModes.values()[gameModeInt-1];
			}
			catch(Exception e)
			{
				String errorMessage = StringConstants.getFormattedString(StringKeys.INVALID_GAME_MODE_KEY, gameModeString);
				output.outputString(errorMessage);
			}
		}

		return gameMode;
	}

}
