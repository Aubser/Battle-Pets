package edu.dselent.domain;

import edu.dselent.player.Playable;

import java.util.*;

public class SeasonStats
{
    private Map<Integer, SeasonPlayerStats> seasonPlayerStats;

    public SeasonStats(List<Playable> playableList)
    {
        seasonPlayerStats = new HashMap<>();

        for(Playable playable : playableList)
        {
            seasonPlayerStats.put(playable.getPlayableUid(), new SeasonPlayerStats(playable.getPetName()));
        }
    }

    public List<SeasonPlayerStats> getSortedList()
    {
        List<SeasonPlayerStats> statList = new ArrayList<>(seasonPlayerStats.values());
        Collections.sort(statList);
        return statList;
    }

    public void addBattleWins(int playableUid, int battleWins)
    {
        SeasonPlayerStats playerStats = seasonPlayerStats.get(playableUid);
        playerStats.battleWins = playerStats.battleWins + battleWins;
    }

    public void addBattleLosses(int playableUid, int battleLosses)
    {
        SeasonPlayerStats playerStats = seasonPlayerStats.get(playableUid);
        playerStats.battleLosses = playerStats.battleLosses + battleLosses;
    }

    public void addFightWins(int playableUid, int fightWins)
    {
        SeasonPlayerStats playerStats = seasonPlayerStats.get(playableUid);
        playerStats.fightWins = playerStats.fightWins + fightWins;
    }

    public void addFightLosses(int playableUid, int fightLosses)
    {
        SeasonPlayerStats playerStats = seasonPlayerStats.get(playableUid);
        playerStats.fightLosses = playerStats.fightLosses + fightLosses;
    }


    public static class SeasonPlayerStats implements Comparable<SeasonPlayerStats>
    {
        private String petName;
        private int battleWins;
        private int battleLosses;
        private int fightWins;
        private int fightLosses;

        public SeasonPlayerStats(String petName)
        {
            this.petName = petName;
            battleWins = 0;
            battleLosses = 0;
            fightWins = 0;
            fightLosses = 0;
        }


        // Will sort in a default order, by battle wins then by fight wins
        @Override
        public int compareTo(SeasonPlayerStats o)
        {
            int compareValue = 0;

            if(battleWins < o.battleWins)
            {
                compareValue = 1;
            }
            else if(battleWins > o.battleWins)
            {
                compareValue = -1;
            }
            else
            {
                if(fightWins < o.fightWins)
                {
                    compareValue = 1;
                }
                else if(fightWins > o.fightWins)
                {
                    compareValue = -1;
                }
            }

            return compareValue;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();

            sb.append(petName);
            sb.append(",");
            sb.append(battleWins);
            sb.append(",");
            sb.append(battleLosses);
            sb.append(",");
            sb.append(fightWins);
            sb.append(",");
            sb.append(fightLosses);

            return sb.toString();
        }
    }
}
