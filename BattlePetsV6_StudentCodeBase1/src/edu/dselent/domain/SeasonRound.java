package edu.dselent.domain;

public class SeasonRound
{
    private Battle[] battleArray;

    public SeasonRound(Battle[] battleArray)
    {
        this.battleArray = battleArray;
    }

    public Battle[] getBattleArray()
    {
        return battleArray;
    }

    public Battle getBattle(int index)
    {
        return battleArray[index];
    }
}
