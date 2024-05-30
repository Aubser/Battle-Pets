package edu.dselent.settings;

import java.util.List;
import java.util.Objects;

public class FightSettings
{
	private long randomSeed;
	private int numberOfPlayers;
	private List<PlayerSettings> playerSettings;

	public FightSettings(long randomSeed, int numberOfPlayers, List<PlayerSettings> playerSettings)
	{
		this.randomSeed = randomSeed;
		this.numberOfPlayers = numberOfPlayers;
		this.playerSettings = playerSettings;
	}
		
	public long getRandomSeed()
	{
		return randomSeed;
	}

	public int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	public List<PlayerSettings> getPlayerSettings()
	{
		return playerSettings;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof FightSettings))
			return false;
		FightSettings that = (FightSettings) o;
		return randomSeed == that.randomSeed && numberOfPlayers == that.numberOfPlayers && Objects.equals(playerSettings, that.playerSettings);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(randomSeed, numberOfPlayers, playerSettings);
	}

	@Override
	public String toString()
	{
		return "FightSettings{" +
				"randomSeed=" + randomSeed +
				", numberOfPlayers=" + numberOfPlayers +
				", playerSettings=" + playerSettings +
				'}';
	}
}
