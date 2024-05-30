package edu.dselent.settings;

import java.util.Objects;

public class SeasonSettings
{
	private BattleSettings battleSettings;

	// Assumes battle settings are the same for all battles
	public SeasonSettings(BattleSettings battleSettings)
	{
		this.battleSettings = battleSettings;
	}

	public BattleSettings getBattleSettings()
	{
		return battleSettings;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SeasonSettings that = (SeasonSettings) o;

		return battleSettings.equals(that.battleSettings);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(battleSettings);
	}

	@Override
	public String toString()
	{
		return "SeasonSettings{" +
				"battleSettings=" + battleSettings +
				'}';
	}
}
