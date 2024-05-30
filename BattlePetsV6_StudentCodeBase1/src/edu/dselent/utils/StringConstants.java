package edu.dselent.utils;

import java.util.HashMap;
import java.util.Map;

import edu.dselent.player.PetTypes;
import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.GameModes;

public class StringConstants
{
	public enum StringKeys
	{
		ENTER_GAME_MODE_KEY,
		ENTER_GAME_MODE_CHOICES_KEY,
		INVALID_GAME_MODE_KEY,
		ENTER_RANDOM_SEED_KEY,
		INVALID_RANDOM_SEED_KEY,
		ENTER_NUMBER_OF_PLAYERS_KEY,
		INVALID_NUMBER_OF_PLAYERS_KEY,
		ENTER_NUMBER_OF_FIGHTS_KEY,
		INVALID_NUMBER_OF_FIGHTS_KEY,
		ENTER_PLAYER_TYPE_KEY,
		INVALID_PLAYER_TYPE_KEY,
		ENTER_PLAYER_TYPE_CHOICES_KEY,
		ENTER_PET_TYPE_KEY,
		INVALID_PET_TYPE_KEY,
		ENTER_PET_TYPE_CHOICES_KEY,
		ENTER_PLAYER_NAME_KEY,
		ENTER_PET_NAME_KEY,
		ENTER_STARTING_HP_KEY,
		INVALID_STARTING_HP_KEY,
		ENTER_COMPUTER_RANDOM_SEED_KEY,
		INVALID_COMPUTER_RANDOM_SEED_KEY,
		ENTER_SKILL_CHOICE_KEY,
		INVALID_SKILL_CHOICE_KEY,
		SKILL_CHOICE_RECHARGING_KEY,
		ENTER_SKILL_PREDICTION_CHOICE_KEY;
	}
	private static final String ENTER_GAME_MODE_STRING = "Choose a game mode";
	private static final String GAME_MODE_CHOICES_STRING;
	private static final String INVALID_GAME_MODE_STRING = "Invalid game mode choice: %s";
	private static final String ENTER_RANDOM_SEED_STRING = "Enter a random seed";
	private static final String INVALID_RANDOM_SEED_STRING = "Invalid random seed: %s";
	private static final String ENTER_NUMBER_OF_PLAYERS_STRING = "Enter a number of players";
	private static final String INVALID_NUMBER_OF_PLAYERS_STRING = "Invalid number of players: %s";
	private static final String ENTER_NUMBER_OF_FIGHTS_STRING = "Enter a number of fights";
	private static final String INVALID_NUMBER_OF_FIGHTS_STRING = "Invalid number of fights: %s";
	private static final String ENTER_PLAYER_TYPE_STRING = "Enter the player type for player %d";
	private static final String INVALID_PLAYER_TYPE_STRING = "Invalid player type: %s";
	private static final String PLAYER_TYPE_CHOICES_STRING;
	private static final String ENTER_PET_TYPE_STRING = "Enter the pet type for player %d";
	private static final String INVALID_PET_TYPE_STRING = "Invalid pet type: %s";
	private static final String PET_TYPE_CHOICES_STRING;
	private static final String ENTER_PLAYER_NAME_STRING = "Enter a name for player %s";
	private static final String ENTER_PET_NAME_STRING = "%s: Enter a name for your pet";
	private static final String ENTER_STARTING_HP_STRING = "Enter a starting hp for %s";
	private static final String INVALID_STARTING_HP_STRING = "Invalid starting hp: %s";
	private static final String ENTER_COMPUTER_RANDOM_SEED_STRING = "Enter a random seed to use for %s";
	private static final String INVALID_COMPUTER_RANDOM_SEED_STRING = "Invalid random seed: %s";
	private static final String ENTER_SKILL_CHOICE_STRING = "%s: Enter a skill choice\n%s";
	private static final String INVALID_SKILL_CHOICE_STRING = "Invalid skill choice: %s";
	private static final String SKILL_CHOICE_RECHARGING_STRING = "%s is recharging";
	private static final String ENTER_SKILL_PREDICTION_CHOICE_STRING = "%s: Enter a skill prediction choice\n%s";
	
	private static final Map<StringKeys, String> stringKeyMap;
	
	static
	{
		// Generalized the enum choice format
		// Can specific specific functions if format ever differes
		PLAYER_TYPE_CHOICES_STRING = getEnumChoices(PlayerTypes.values());
		PET_TYPE_CHOICES_STRING = getEnumChoices(PetTypes.values());
		GAME_MODE_CHOICES_STRING = getEnumChoices(GameModes.values());
		
		//////////////////////////////////////////////////////////////
		
		stringKeyMap = new HashMap<>();

		stringKeyMap.put(StringKeys.ENTER_GAME_MODE_KEY, ENTER_GAME_MODE_STRING);
		stringKeyMap.put(StringKeys.ENTER_GAME_MODE_CHOICES_KEY, GAME_MODE_CHOICES_STRING);
		stringKeyMap.put(StringKeys.INVALID_GAME_MODE_KEY, INVALID_GAME_MODE_STRING);
		stringKeyMap.put(StringKeys.ENTER_RANDOM_SEED_KEY, ENTER_RANDOM_SEED_STRING);
		stringKeyMap.put(StringKeys.INVALID_RANDOM_SEED_KEY, INVALID_RANDOM_SEED_STRING);
		stringKeyMap.put(StringKeys.ENTER_NUMBER_OF_PLAYERS_KEY, ENTER_NUMBER_OF_PLAYERS_STRING);
		stringKeyMap.put(StringKeys.INVALID_NUMBER_OF_PLAYERS_KEY, INVALID_NUMBER_OF_PLAYERS_STRING);
		stringKeyMap.put(StringKeys.ENTER_NUMBER_OF_FIGHTS_KEY, ENTER_NUMBER_OF_FIGHTS_STRING);
		stringKeyMap.put(StringKeys.INVALID_NUMBER_OF_FIGHTS_KEY, INVALID_NUMBER_OF_FIGHTS_STRING);
		stringKeyMap.put(StringKeys.ENTER_PLAYER_TYPE_KEY, ENTER_PLAYER_TYPE_STRING);
		stringKeyMap.put(StringKeys.INVALID_PLAYER_TYPE_KEY, INVALID_PLAYER_TYPE_STRING);
		stringKeyMap.put(StringKeys.ENTER_PLAYER_TYPE_CHOICES_KEY, PLAYER_TYPE_CHOICES_STRING);
		stringKeyMap.put(StringKeys.ENTER_PET_TYPE_KEY, ENTER_PET_TYPE_STRING);
		stringKeyMap.put(StringKeys.INVALID_PET_TYPE_KEY, INVALID_PET_TYPE_STRING);
		stringKeyMap.put(StringKeys.ENTER_PET_TYPE_CHOICES_KEY, PET_TYPE_CHOICES_STRING);
		stringKeyMap.put(StringKeys.ENTER_PLAYER_NAME_KEY, ENTER_PLAYER_NAME_STRING);
		stringKeyMap.put(StringKeys.ENTER_PET_NAME_KEY, ENTER_PET_NAME_STRING);
		stringKeyMap.put(StringKeys.ENTER_STARTING_HP_KEY, ENTER_STARTING_HP_STRING);
		stringKeyMap.put(StringKeys.INVALID_STARTING_HP_KEY, INVALID_STARTING_HP_STRING);
		stringKeyMap.put(StringKeys.ENTER_COMPUTER_RANDOM_SEED_KEY, ENTER_COMPUTER_RANDOM_SEED_STRING);
		stringKeyMap.put(StringKeys.INVALID_COMPUTER_RANDOM_SEED_KEY, INVALID_COMPUTER_RANDOM_SEED_STRING);
		stringKeyMap.put(StringKeys.ENTER_SKILL_CHOICE_KEY, ENTER_SKILL_CHOICE_STRING);
		stringKeyMap.put(StringKeys.INVALID_SKILL_CHOICE_KEY, INVALID_SKILL_CHOICE_STRING);
		stringKeyMap.put(StringKeys.SKILL_CHOICE_RECHARGING_KEY, SKILL_CHOICE_RECHARGING_STRING);
		stringKeyMap.put(StringKeys.ENTER_SKILL_PREDICTION_CHOICE_KEY, ENTER_SKILL_PREDICTION_CHOICE_STRING);
		
	}

	private static String getEnumChoices(Object[] enumValues)
	{
		StringBuilder sbPlayerType = new StringBuilder();

		for(int i=0; i<enumValues.length; i++)
		{
			sbPlayerType.append((i+1) + ": " + enumValues[i] + "\n");
		}

		return sbPlayerType.toString();
	}


	public static String getFormattedString(StringKeys stringKey, Object... args)
	{
		String unformattedString = stringKeyMap.get(stringKey);
		return String.format(unformattedString, args);
	}
}
