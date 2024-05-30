package edu.dselent.control.settings;

import edu.dselent.player.PlayerTypes;

import java.util.ArrayList;
import java.util.List;

public class PlayerSettingsControl
{
	private List<edu.dselent.settings.PlayerSettings> playerSettingsList;

	// Based on the type of player, delegate to other player setting controls
	// Argument against inheritance = do not know the type of control needed until retrieving the player type
	// Need the control to retrieve the player type
	private PlayerSettingsRetrievable playerSettingsRetrievable;
	private DefaultPlayerSettingsRetrievable defaultPlayerSettingsRetrievable;
	
	public PlayerSettingsControl()
	{
		this.playerSettingsList = new ArrayList<>();
		defaultPlayerSettingsRetrievable = new DefaultPlayerSettingsRetrievable();
	}

	public List<edu.dselent.settings.PlayerSettings> getPlayerSettingsList()
	{
		return playerSettingsList;
	}

	// Called form TextGameRunner to retrieve everything
	// Delegate to other controls to retrieve specific information based on the player type
	// Different types of players may require extra information (e.g. Computer player random seed)
	public List<edu.dselent.settings.PlayerSettings> retrievePlayerSettingsList(int numberOfPlayers)
	{
		List<edu.dselent.settings.PlayerSettings> playerSettingsList = new ArrayList<>();

		for(int i=0; i<numberOfPlayers; i++)
		{
			PlayerTypes playerType = defaultPlayerSettingsRetrievable.retrievePlayerType(i);

			edu.dselent.settings.PlayerSettings playerSettings;

			if(playerType == PlayerTypes.COMPUTER)
			{
				this.playerSettingsRetrievable = new ComputerPlayerSettingsRetrievable();
				playerSettings = this.playerSettingsRetrievable.retrievePlayerSettings(i, playerType);
			}
			else
			{
				this.playerSettingsRetrievable = new DefaultPlayerSettingsRetrievable();
				playerSettings = this.playerSettingsRetrievable.retrievePlayerSettings(i, playerType);
			}
			// Add more as necessary

			playerSettingsList.add(playerSettings);
		}
		
		return playerSettingsList;
	}

}
