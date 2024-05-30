package edu.dselent.domain;

import java.util.ArrayList;
import java.util.List;

// TODO should round contain roundIndex?
// TODO should should contain playableList?
public class Round
{
	private Fight fight;
	private int roundIndex;
	private List<PlayerRoundData> playerRoundDataList;

	// Making claim that a round cannot exist without a fight
	public Round(Fight fight, int roundIndex)
	{
		this.fight = fight;
		this.roundIndex = roundIndex;
		playerRoundDataList = new ArrayList<>();
	}

	public Fight getFight()
	{
		return fight;
	}
	
	public int getRoundIndex()
	{
		return roundIndex;
	}

	public List<PlayerRoundData> getPlayerRoundDataList()
	{
		return playerRoundDataList;
	}
	
	
}
