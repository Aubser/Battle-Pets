package edu.dselent.domain;

import java.util.ArrayList;
import java.util.List;

import edu.dselent.player.Playable;

public class Fight implements Winnable
{
	private boolean ended;
	private List<Playable> playableList;
	private List<Round> roundList;
	
	// Can calculate from the list of round data, but could be very slow
	// Keeping this as an extra variable for performance reasons
	private List<Double> randomDamageDifferenceList;

	public Fight(List<Playable> playableList)
	{
		this.ended = false;
		this.playableList = playableList;
		roundList = new ArrayList<>();
		randomDamageDifferenceList = new ArrayList<>();

		for(int i=0; i<playableList.size(); i++)
		{
			randomDamageDifferenceList.add(0.0);
		}

	}

	public List<Playable> getPlayableList()
	{
		return playableList;
	}

	public boolean hasEnded()
	{
		return ended;
	}

	public void setEnded(boolean ended)
	{
		this.ended = ended;
	}

	public List<Round> getRoundList()
	{
		return roundList;
	}

	// When using reversal of fortune on same turn, it swaps
	// Not same turn = zero balance
	// I suppose this is okay
	public double getRandomDamageDifference(int playerIndex)
	{
		return randomDamageDifferenceList.get(playerIndex);
	}
	
	private void setRandomDamageDifference(int playerIndex, double randomDamageDifference)
	{
		randomDamageDifferenceList.set(playerIndex, randomDamageDifference);
	}
	
	public void updateRandomDamageDifference(int playerIndex, double randomDamage)
	{
		double oldRandomDamage = getRandomDamageDifference(playerIndex);
		double newRandomDamage = oldRandomDamage + randomDamage;
		setRandomDamageDifference(playerIndex, newRandomDamage);
	}

	/**
	 * Assumes fight is over
	 *
	 * @return Returns the winners of the fight
	 */
	public List<Playable> calculateWinners()
	{
		if(!ended)
		{
			throw new RuntimeException("calculateWinners cannot be called until the fight is over");
		}

		List<Playable> winnerList = new ArrayList<>();
		List<PlayerRoundData> lastRoundDataList = roundList.get(roundList.size()-1).getPlayerRoundDataList();

		// extract pets who were awake on the last turn
		// sort by their current hp

		// Would prefer to use a value of a pet, however the first pet hp may not have been awake on the last turn but may be the highest hp
		Double currentMaxHp = null;

		for(int i=0; i<lastRoundDataList.size(); i++)
		{
			PlayerRoundData lastRoundData = lastRoundDataList.get(i);

			// Were awake at the start of last turn
			if(lastRoundData.isAwake())
			{
				if(currentMaxHp == null || lastRoundData.getHpAfter() > currentMaxHp)
				{
					currentMaxHp = lastRoundData.getHpAfter();
					winnerList.clear();
					winnerList.add(playableList.get(i));
				}
				else if(lastRoundData.getHpAfter() == currentMaxHp)
				{
					winnerList.add(playableList.get(i));
				}
			}
		}

		return winnerList;
	}

	/**
	 * Assumes fight is over
	 *
	 * @return Returns the losers of the fight
	 */
	public List<Playable> calculateLosers()
	{
		if(!ended)
		{
			throw new RuntimeException("calculateLosers cannot be called until the fight is over");
		}

		List<Playable> winnerList = calculateWinners();
		List<Playable> loserList = new ArrayList<>(playableList);
		loserList.removeAll(winnerList);

		return loserList;
	}
}
