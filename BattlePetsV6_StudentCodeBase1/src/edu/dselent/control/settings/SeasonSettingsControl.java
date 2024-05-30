package edu.dselent.control.settings;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.io.Inputtable;
import edu.dselent.io.IoManager;
import edu.dselent.io.Outputtable;
import edu.dselent.settings.BattleSettings;
import edu.dselent.settings.FightSettings;
import edu.dselent.settings.SeasonSettings;
import edu.dselent.settings.SettingsConstants;
import edu.dselent.utils.StringConstants;
import edu.dselent.utils.StringConstants.StringKeys;

public class SeasonSettingsControl
{

	private IoManager ioManager;
	private Outputtable output;
	private Inputtable input;

	private SeasonSettings seasonSettings;
	private BattleSettingsControl battleSettingsControl;

	public SeasonSettingsControl()
	{
		ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		output = ioManager.getOutputSender();
		input = ioManager.getInputGetter();

		battleSettingsControl = new BattleSettingsControl();
	}

	public SeasonSettings getSeasonSettings()
	{
		return seasonSettings;
	}

	public SeasonSettings retrieveSeasonSettings()
	{
		// Currently no specific season settings
		// Having this here for extensibility

		BattleSettings battleSettings = battleSettingsControl.retrieveBattleSettings();
		seasonSettings = new SeasonSettings(battleSettings);

		return seasonSettings;
	}
	
}
