package edu.dselent.domain;

import edu.dselent.player.Playable;

import java.util.*;

// Season can contain a set of Battles to Iterate in round fashion or other ways


public class Season implements Winnable, SeasonIterable<Battle>
{
    private List<Playable> playableList;
    private Set<Battle> battleSet;
    private boolean ended;

    /**
     * Constructs a season given a list of playable and a set of battles
     *
     * @param playableList A list of playable that compete in the season
     * @param battleSet A set of all battle that will occur in the season
     */
    public Season(List<Playable> playableList, Set<Battle> battleSet)
    {
        this.playableList = playableList;
        this.battleSet = battleSet;
        ended = false;
    }

    public List<Playable> getPlayableList()
    {
        return playableList;
    }

    public Set<Battle> getBattleSet()
    {
        return battleSet;
    }

    public boolean hasEnded()
    {
        return ended;
    }

    public void setEnded(boolean ended)
    {
        this.ended = ended;
    }

    public int getNumberOfSeasonRounds()
    {
        int seasonRounds = -1;

        if(playableList.size() % 2 == 0)
        {
            seasonRounds = playableList.size() - 1;
        }
        else
        {
            seasonRounds = playableList.size();
        }

        return seasonRounds;
    }

    // TODO
    // Both Battle and Season have similar implementations, but not Fight
    // hasEnded also seems generalizable
    @Override
    public List<Playable> calculateWinners()
    {
        if(!ended)
        {
            throw new RuntimeException("calculateWinnerList cannot be called until the season is over");
        }

        List<Playable> initialWinnerList = new ArrayList<>();
        List<Playable> winnerList = new ArrayList<>();

        Map<Integer, Integer> idWinMap = new HashMap<>();

        for(Playable playable : playableList)
        {
            idWinMap.put(playable.getPlayableUid(), 0);
        }

        for(Winnable battle : battleSet)
        {
            List<Playable> battleWinnerList = battle.calculateWinners();

            for(Playable battleWinner : battleWinnerList)
            {
                int winningUid = battleWinner.getPlayableUid();
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
                        initialWinnerList.add(currentPlayable);
                        playableFound = true;
                    }
                }

            }
        }

        // Use fight wins to break ties

        Map<Integer, Integer> fightWinMap = calculateFightWins();

        int maxFightWins = 0;

        for(Playable playable : initialWinnerList)
        {
            int playableUid = playable.getPlayableUid();
            int currentWins = fightWinMap.get(playableUid);

            if(currentWins > maxFightWins)
            {
                maxFightWins = currentWins;
                winnerList.clear();
                winnerList.add(playable);
            }
            else if(currentWins == maxFightWins)
            {
                winnerList.add(playable);
            }
        }

        return winnerList;
    }

    // Inefficient but consistent with data
    private Map<Integer, Integer> calculateFightWins()
    {
        Map<Integer, Integer> fightWins = new HashMap<>();

        for(Battle battle : battleSet)
        {
            Fight[] fights = battle.getFightArray();

            for(Fight fight : fights)
            {
                List<Playable> fightWinners = fight.calculateWinners();

                for(Playable fightWinner : fightWinners)
                {
                    int winnerUid = fightWinner.getPlayableUid();

                    Integer currentWins = fightWins.get(winnerUid);

                    if(currentWins == null)
                    {
                        currentWins = 0;
                    }

                    fightWins.put(winnerUid, ++currentWins);
                }
            }

        }

        return fightWins;
    }

    @Override
    public Iterator<Battle> iterator()
    {
        return new SeasonIteratorImpl1(playableList, battleSet);
    }

    @Override
    public Iterator<Battle> iterator2()
    {
        return new SeasonIteratorImpl2(playableList, battleSet);
    }

}
