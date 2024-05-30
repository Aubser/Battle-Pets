package edu.dselent.settings;

import java.util.Objects;

/**
 * Class to hold settings for the entire game
 *
 * I decided that if settings related to a battle or players are the same for all battles or fights that
 * those settings should still remain in game or player settings
 *
 * This class is for settings not used for a battle or player
 * Currently this is only the game mode but is extendable
 */
public class GameSettings
{
	private GameModes gameMode;


	public GameSettings(GameModes gameMode)
	{
		this.gameMode = gameMode;
	}

	public GameModes getGameMode()
	{
		return gameMode;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof GameSettings))
			return false;
		GameSettings that = (GameSettings) o;

		return gameMode == that.gameMode;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(gameMode);
	}

	@Override
	public String toString()
	{
		return "GameSettings{ gameMode=" + gameMode + "}";
	}
}
