package edu.dselent.domain;

import java.util.*;

import edu.dselent.player.Playable;

public class Battle implements Winnable
{
	private boolean ended;
	private int numberOfFights;
	private Fight[] fightArray;
	private List<Playable> playableList;

	public Battle(int numberOfFights, List<Playable> playableList)
	{
		this.ended = false;
		this.numberOfFights = numberOfFights;
		this.fightArray = new Fight[numberOfFights];

		for(int i=0; i<fightArray.length; i++)
		{
			fightArray[i] = new Fight(playableList);
		}

		this.playableList = playableList;
	}

	public int getNumberOfFights()
	{
		return numberOfFights;
	}

	public Fight[] getFightArray()
	{
		return fightArray;
	}

	public Fight getFight(int index)
	{
		return fightArray[index];
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

	/**
	 * Assumes battle is over
	 *
	 * @return Returns the winners of the battle
	 */
	public List<Playable> calculateWinners()
	{
		if(!ended)
		{
			throw new RuntimeException("calculateWinnerList cannot be called until the battle is over");
		}

		List<Playable> winnerList = new ArrayList<>();


		Map<Integer, Integer> idWinMap = new HashMap<>();

		for(Playable playable : playableList)
		{
			idWinMap.put(playable.getPlayableUid(), 0);
		}

		for(Winnable fight : fightArray)
		{
			List<Playable> fightWinnerList = fight.calculateWinners();

			for(Playable fightWinner : fightWinnerList)
			{
				int winningUid = fightWinner.getPlayableUid();
				int previousWinCount = idWinMap.get(winningUid);
				idWinMap.replace(winningUid, previousWinCount+1);
			}
		}

		Set<Integer> playableUidSet = idWinMap.keySet();
		Iterator<Integer> playableUidIterator = playableUidSet.iterator();
		int maxWins = -1;

		// Okay to initialize max wins to -1
		// All win counts must be greater than 1
		// There must be at least two playables

		while(playableUidIterator.hasNext())
		{
			int currentId = playableUidIterator.next();
			int currentWinCount = idWinMap.get(currentId);

			if(currentWinCount >= maxWins)
			{
				maxWins = currentWinCount;
			}
		}

		//

		for (int currentId : playableUidSet)
		{
			int currentWinCount = idWinMap.get(currentId);

			if (currentWinCount >= maxWins)
			{
				// find playable with the uid and add it to the list
				boolean playableFound = false;

				for (int i = 0; i < playableList.size() && !playableFound; i++)
				{
					Playable currentPlayable = playableList.get(i);

					if (currentPlayable.getPlayableUid() == currentId)
					{
						winnerList.add(currentPlayable);
						playableFound = true;
					}
				}

			}
		}

		return winnerList;
	}

	public int calculateFightWins(int playableUid)
	{
		int fightWins = 0;

		for (Fight fight : fightArray)
		{
			if (fight.hasEnded())
			{
				List<Playable> fightWinners = fight.calculateWinners();

				for (Playable winner : fightWinners)
				{
					if (playableUid == winner.getPlayableUid())
					{
						fightWins++;
					}
				}
			}
		}

		return fightWins;
	}

	public int calculateFightLosses(int playableUid)
	{
		int fightLosses = 0;

		for (Fight fight : fightArray)
		{
			if (fight.hasEnded())
			{
				List<Playable> fightLosers = fight.calculateLosers();

				for (Playable loser : fightLosers)
				{
					if (playableUid == loser.getPlayableUid())
					{
						fightLosses++;
					}
				}
			}
		}
		return fightLosses;
	}
}
