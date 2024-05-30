package edu.dselent.player.defaultspeed;

import edu.dselent.event.AttackEvent;
import edu.dselent.event.PlayerEventInfo;
import edu.dselent.skill.Skills;

import java.util.*;


public class OpponentTracker
{
    public int numPlayers;
    public List<PlayerEventInfo> petInfo;
    public List<Double> petHps;
    public List<Double> petRandDmgsTaken;
    public List<Double> petRandDmgsGiven;
    public List<Map<Skills, Integer>> cooldowns;

    // assumes creation at the beginning of a fight.
    public OpponentTracker(List<PlayerEventInfo> info)
    {
        this.petInfo = info;
        this.numPlayers = petInfo.size();
        this.petHps = new ArrayList<Double>();
        this.petRandDmgsTaken = new ArrayList<Double>(Collections.nCopies(numPlayers, 0.00d));
        this.petRandDmgsGiven = new ArrayList<Double>(Collections.nCopies(numPlayers, 0.00d));
        this.cooldowns = new ArrayList<Map<Skills, Integer>>();
        for(int i = 0; i < petInfo.size(); i++)
        {
            petHps.add(petInfo.get(i).getStartingHp());
            cooldowns.add(new HashMap<Skills, Integer>());
            cooldowns.get(i).put(Skills.ROCK_THROW, 0);
            cooldowns.get(i).put(Skills.PAPER_CUT, 0);
            cooldowns.get(i).put(Skills.SCISSORS_POKE, 0);
            cooldowns.get(i).put(Skills.SHOOT_THE_MOON, 0);
            cooldowns.get(i).put(Skills.REVERSAL_OF_FORTUNE, 0);
        }
    }

    public void track(AttackEvent event)
    {
        switch(event.getAttackingSkillChoice())
        {
            case ROCK_THROW:
            case PAPER_CUT:
            case SCISSORS_POKE:
                cooldowns.get(event.getAttackingPlayableUid())
                        .put(event.getAttackingSkillChoice(), 2);
                break;
            case SHOOT_THE_MOON:
                cooldowns.get(event.getAttackingPlayableUid())
                        .put(event.getAttackingSkillChoice(), 6);
                break;
            case REVERSAL_OF_FORTUNE:
                cooldowns.get(event.getAttackingPlayableUid())
                        .put(event.getAttackingSkillChoice(), 6);
                resetRandDmgs(event.getAttackingPlayableUid());
                break;
        }

        double victimCurrentHp = petHps.get(event.getVictimPlayableUid());
        double victimNewHp = victimCurrentHp -
                (event.getDamage().getRandomDamage() + event.getDamage().getConditionalDamage());

        petHps.set(event.getVictimPlayableUid(), victimNewHp);

        petRandDmgsGiven.set(event.getAttackingPlayableUid(),
                petRandDmgsGiven.get(event.getAttackingPlayableUid()) + event.getDamage().getRandomDamage());
        petRandDmgsTaken.set(event.getVictimPlayableUid(),
                petRandDmgsTaken.get(event.getVictimPlayableUid()) + event.getDamage().getRandomDamage());
    }

    public void updateCooldowns()
    {
        for(int i = 0; i < numPlayers; i++)
        {
            for(Skills key : cooldowns.get(i).keySet())
            {
                if(cooldowns.get(i).get(key) != 0)
                {
                    cooldowns.get(i).put(key,
                            cooldowns.get(i).get(key) - 1);
                }
            }
        }
    }

    public void resetRandDmgs(int attackingIndex)
    {
        petRandDmgsGiven.set(attackingIndex, 0.00d);
        petRandDmgsTaken.set(attackingIndex, 0.00d);
    }

    public List<Double> getPetHps()
    {
        return petHps;
    }

    public List<Double> getPetRandDmgsGiven()
    {
        return petRandDmgsGiven;
    }

    public List<Double> getPetRandDmgsTaken()
    {
        return petRandDmgsTaken;
    }

    public List<Map<Skills, Integer>> getPetCooldowns()
    {
        return cooldowns;
    }

    public List<PlayerEventInfo> getPetInfo()
    {
        return petInfo;
    }
}
