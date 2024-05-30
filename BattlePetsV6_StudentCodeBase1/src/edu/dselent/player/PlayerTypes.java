package edu.dselent.player;

import edu.dselent.utils.Utils;

public enum PlayerTypes
{
	HUMAN,
	COMPUTER,
	DEFAULT_POWER,
	DEFAULT_SPEED,
	DEFAULT_INTELLIGENCE,
	TEAM_05;

	
	@Override
	public String toString()
	{		
		return Utils.convertEnumString(this.name());
	}
}
