package edu.dselent.settings;

import java.util.Objects;

public class BattleSettings
{
	private int numberOfFights;
	private FightSettings fightSettings;

	// Assumes fight settings are the same for all fights
	public BattleSettings(int numberOfFights, FightSettings fightSettings)
	{
		this.numberOfFights = numberOfFights;
		this.fightSettings = fightSettings;
	}

	public int getNumberOfFights()
	{
		return numberOfFights;
	}

	public FightSettings getFightSettings()
	{
		return fightSettings;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof BattleSettings))
			return false;

		BattleSettings that = (BattleSettings) o;

		return numberOfFights == that.numberOfFights && Objects.equals(fightSettings, that.fightSettings);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(numberOfFights, fightSettings);
	}

	@Override
	public String toString()
	{
		return "BattleSettings{" +
				"numberOfFights=" + numberOfFights +
				", fightSettings=" + fightSettings +
				'}';
	}
}
