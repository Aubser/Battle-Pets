package edu.dselent.domain;

import edu.dselent.player.Playable;

import java.util.*;

/**
 * Circle method
 */
public class SeasonIteratorImpl1 implements Iterator<Battle>
{
    private List<Playable> playableList;
    private Set<Battle> battleSet;
    private Map<Integer, Integer> indexUidMap;

    private int[][] currentMatchUpArray;
    private int currentBattleIndex = 0;
    private int currentBattleInRound = 0;

    public SeasonIteratorImpl1(List<Playable> playableList, Set<Battle> battleSet)
    {
        this.playableList = playableList;
        this.battleSet = battleSet;

        indexUidMap = new HashMap<>();

        for(int i=0; i<playableList.size(); i++)
        {
            Playable playable = playableList.get(i);
            int uid = playable.getPlayableUid();

            indexUidMap.put(i, uid);
        }

        currentMatchUpArray = new int[2][((int) Math.ceil(playableList.size() / 2.0))];

        Arrays.fill(currentMatchUpArray[0], -1);
        Arrays.fill(currentMatchUpArray[1], -1);

        for(int i=0; i<Math.ceil(playableList.size()/2.0); i++)
        {
            currentMatchUpArray[0][i] = i;
        }

        for(int j=(int)Math.ceil(playableList.size()/2.0), i=(int)Math.ceil(playableList.size()/2.0)-1; j<playableList.size() && i>=0; j++, i--)
        {
            currentMatchUpArray[1][i] = j;
        }

        // System.out.println("x");
    }

    @Override
    public boolean hasNext()
    {
        return currentBattleIndex < battleSet.size();
    }

    @Override
    public Battle next()
    {
        int playable1Index = currentMatchUpArray[0][currentBattleInRound];
        int playable2Index = currentMatchUpArray[1][currentBattleInRound];

        // TODO handle bye better
        while(playable1Index == -1 || playable2Index == -1)
        {
            currentBattleInRound++;

            // If season round is over update the 2d array and reset current battle
            boolean roundOver = isRoundOver();

            if(roundOver && hasNext())
            {
                currentBattleInRound = 0;

                // first array starts with playable 0
                // second array starts with playable n

                int lastPlayableFirstArray = currentMatchUpArray[0][currentMatchUpArray[0].length-1];
                int firstPlayableSecondArray = currentMatchUpArray[1][0];

                for(int i=currentMatchUpArray[0].length-1; i>=2; i--)
                {
                    currentMatchUpArray[0][i] = currentMatchUpArray[0][i-1];
                }

                currentMatchUpArray[0][1] = firstPlayableSecondArray;

                for(int i=0; i<currentMatchUpArray[1].length-1; i++)
                {
                    currentMatchUpArray[1][i] = currentMatchUpArray[1][i+1];
                }

                currentMatchUpArray[1][currentMatchUpArray[1].length-1] = lastPlayableFirstArray;
            }

            playable1Index = currentMatchUpArray[0][currentBattleInRound];
            playable2Index = currentMatchUpArray[1][currentBattleInRound];
        }

        int playable1Uid = indexUidMap.get(playable1Index);
        int playable2Uid = indexUidMap.get(playable2Index);

        // find battle with playables that match the two uids

        Battle nextBattle = findBattle(playable1Uid, playable2Uid);

        currentBattleIndex++;
        currentBattleInRound++;

        // If season round is over update the 2d array and reset current battle
        boolean roundOver = isRoundOver();

        if(roundOver && hasNext())
        {
            currentBattleInRound = 0;

            // first array starts with playable 0
            // second array starts with playable n

            int lastPlayableFirstArray = currentMatchUpArray[0][currentMatchUpArray[0].length-1];
            int firstPlayableSecondArray = currentMatchUpArray[1][0];

            for(int i=currentMatchUpArray[0].length-1; i>=2; i--)
            {
                currentMatchUpArray[0][i] = currentMatchUpArray[0][i-1];
            }

            currentMatchUpArray[0][1] = firstPlayableSecondArray;

            for(int i=0; i<currentMatchUpArray[1].length-1; i++)
            {
                currentMatchUpArray[1][i] = currentMatchUpArray[1][i+1];
            }

            currentMatchUpArray[1][currentMatchUpArray[1].length-1] = lastPlayableFirstArray;
       }

        return nextBattle;

    }

    // TODO
    // Not generalized to n playables in a battle
    private Battle findBattle(int playable1Uid, int playable2Uid)
    {
        Iterator<Battle> battleSetIterator = battleSet.iterator();
        boolean found = false;
        Battle theBattle = null;

        while(!found && battleSetIterator.hasNext())
        {
            Battle battle = battleSetIterator.next();

            List<Playable> playableList = battle.getPlayableList();

            boolean foundOne = false;
            boolean foundTwo = false;

            for(Playable playable : playableList)
            {
                int currentPlayableUid = playable.getPlayableUid();

                if(playable1Uid == currentPlayableUid)
                {
                    foundOne = true;
                }
                else if(playable2Uid == currentPlayableUid)
                {
                    foundTwo = true;
                }
            }

            if(foundOne && foundTwo)
            {
                found = true;
                theBattle = battle;
            }
        }

        return theBattle;
    }

    private boolean isRoundOver()
    {
        boolean roundOver = false;

        if(currentBattleInRound >= currentMatchUpArray[0].length)
        {
            roundOver = true;
        }
        /*else
        {
            int playable1Index = currentMatchUpArray[0][currentBattleInRound];
            int playable2Index = currentMatchUpArray[1][currentBattleInRound];

            if(playable1Index == -1 || playable2Index == -1)
            {
                roundOver = true;
            }
        }*/

        return roundOver;
    }
}
