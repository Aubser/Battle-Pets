package edu.dselent.player;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

import edu.dselent.player.defaultintelligence.DefaultIntelligence;
import edu.dselent.player.defaultpower.DefaultPower;
import edu.dselent.player.defaultspeed.DefaultSpeed;
import edu.dselent.player.spring2022.team05.AI.Team05AI;
import edu.dselent.settings.ComputerPlayerSettings;
import edu.dselent.settings.PlayerSettings;


public class PlayableInstantiator
{
	public static List<Playable> instantiatePlayables(List<PlayerSettings> playerSettingsList)
	{
		List<Playable> playableList = new ArrayList<>();

		for(int i = 0; i< playerSettingsList.size(); i++)
		{
			PlayerSettings playerSettings = playerSettingsList.get(i);
			Playable playable = instantiatePlayable(i, playerSettings);
			playableList.add(playable);
		}

		return playableList;
	}

	public static Playable instantiatePlayable(int playableUid, PlayerSettings playerSettings)
	{
		Playable thePlayable = null;

		PlayerTypes playerType = playerSettings.getPlayerType();

		if(playerType == PlayerTypes.HUMAN)
		{
			thePlayable = new HumanPetInstance(playableUid, playerSettings);
		}
		else if(playerType == PlayerTypes.COMPUTER)
		{
			if(playerSettings instanceof ComputerPlayerSettings)
			{
				// Doug's default AI
				thePlayable = new ComputerPetInstance(playableUid, (ComputerPlayerSettings) playerSettings);
			}
			else
			{
				// TODO
				//Look into this more, added because of fall 2020 team 10
				ComputerPlayerSettings computerPlayerSettings = new ComputerPlayerSettings.ComputerPlayerSettingsBuilder()
						.withPetType(playerSettings.getPetType())
						.withPlayerType(playerSettings.getPlayerType())
						.withStartingHp(playerSettings.getStartingHp())
						.withPetName(playerSettings.getPetName())
						.withSkillSet(playerSettings.getSkillSet())
						.withRandomSeed(-1)
						.build();

				thePlayable = new ComputerPetInstance(playableUid, computerPlayerSettings);
			}
		}
		else if(playerType == PlayerTypes.DEFAULT_POWER)
		{
			thePlayable = new DefaultPower(playableUid, playerSettings);
		}
		else if(playerType == PlayerTypes.DEFAULT_SPEED)
		{
			thePlayable = new DefaultSpeed(playableUid, playerSettings);
		}
		else if(playerType == PlayerTypes.DEFAULT_INTELLIGENCE) {
			thePlayable = new DefaultIntelligence(playableUid, playerSettings);
		}
		else if (playerType == PlayerTypes.TEAM_05){
			thePlayable = new Team05AI(playableUid, playerSettings);
		}
		else
		{
			// TODO make custom exception
			throw new RuntimeException("Invalid playerType: " + playerType);
		}
		
		
		return thePlayable;
	}

}
