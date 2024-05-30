package edu.dselent.player.defaultspeed;

import edu.dselent.event.AttackEvent;
import edu.dselent.event.BaseEvent;
import edu.dselent.event.EventTypes;
import edu.dselent.event.FightStartEvent;
import edu.dselent.player.PetInstance;
import edu.dselent.player.PetTypes;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DefaultSpeed extends PetInstance
{
    private Random randomGenerator;
    private ArrayList<Skills> availableSkills; //List of skills not on CD
    private ArrayList<Skills> allSkills;
    // apparently our indices in the PlayerEventInfo will correspond to the
    // indices in the AttackEvents, so we need to cache them and change as needed.
    private OpponentTracker tracker;
    private int opponentPetIndex;
    private int selfIndex;
    private Skills skillPrediction;

    public DefaultSpeed(int playableUid, PlayerSettings info)
    {
        super(playableUid, info);
        this.opponentPetIndex = 0;
        this.selfIndex = 0;
        this.availableSkills = new ArrayList<>();
        this.allSkills = new ArrayList<>();
        allSkills.addAll(Arrays.asList(Skills.values()));
        this.randomGenerator = new Random(20181215);
    }

    @Override
    public Skills chooseSkill()
    {
        availableSkills.clear();

        for(Skills skill: Skills.values())
        {
            if(super.getSkillRechargeTime(skill) == 0)
                availableSkills.add(skill);
        }
        return makeDecision();
    }

    // Had to override this, as I didn't have access to PetInstance's skill map, so there was just
    // no way to use the ShootTheMoonInstance to do this properly.
    @Override
    public Skills getSkillPrediction()
    {
        return skillPrediction;
    }

    private void setSmartPrediction(Skills skill)
    {
        skillPrediction = skill;
    }

    @Override
    public void update(Object event)
    {
        if(event instanceof BaseEvent)
        {
            BaseEvent base = (BaseEvent) event;
            if(base.getEventType() == EventTypes.FIGHT_START)
            {
                FightStartEvent fightStart = (FightStartEvent) base;
                // for simplicity's purposes, we'll just assume that this pet's info
                // will always be in the playereventinfo list.

                // if it somehow isn't found in the playereventinfo, we should handle
                // it with null checks in our AI.
                for(int i = 0; i < fightStart.getPlayerEventInfoList().size(); i++)
                {
                    // if we're the last pet initialized, we know our opponent will be
                    // the first in the list, since the list will loop back.
                    if(i == fightStart.getPlayerEventInfoList().size() - 1)
                    {
                        opponentPetIndex = 0;
                        selfIndex = fightStart.getPlayerEventInfoList().size() - 1;
                    }
                    else
                    {
                        // wherever we find our info, our opponent will be the next pet.
                        if(fightStart.getPlayerEventInfoList().get(i).getPetName().equals(super.getPetName()))
                        {
                            opponentPetIndex = i + 1;
                            selfIndex = i;
                            break;
                        }
                    }
                }
                this.tracker = new OpponentTracker(fightStart.getPlayerEventInfoList());
            }
            else if(base.getEventType() == EventTypes.ROUND_START)
            {
                tracker.updateCooldowns();
            }
            else if(base.getEventType() == EventTypes.ATTACK)
            {
                AttackEvent attack = (AttackEvent) base;
                // if we're attacking someone, we need to check some stuff.
                if(attack.getAttackingPlayableUid() == selfIndex)
                {
                    // our original opponent died and we need to change it.
                    if(attack.getVictimPlayableUid() != opponentPetIndex)
                    {
                        opponentPetIndex = attack.getVictimPlayableUid();
                    }

                }
                tracker.track(attack);
            }
        }
    }

    private Skills makeDecision()
    {
        if(tracker.getPetInfo().get(opponentPetIndex).getPetType().equals(PetTypes.POWER))
            return enemyPowerType();
        else if(tracker.getPetInfo().get(opponentPetIndex).getPetType().equals(PetTypes.SPEED))
            return enemySpeedType();
        else
            return enemyIntelligenceType();
    }

    private Skills enemyPowerType()
    {
        if(tracker.getPetHps().get(opponentPetIndex) >= 100 && availableSkills.contains(Skills.SHOOT_THE_MOON))
        {
            shootTheMoon();
            return Skills.SHOOT_THE_MOON;
        }
        else if(tracker.getPetHps().get(opponentPetIndex) >= 75)
        {
            if(tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.ROCK_THROW) > 0)
            {
                if(availableSkills.contains(Skills.SCISSORS_POKE))
                    return Skills.SCISSORS_POKE;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SCISSORS_POKE) > 0))
            {
                if(availableSkills.contains(Skills.PAPER_CUT))
                    return Skills.PAPER_CUT;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.PAPER_CUT) > 0))
            {
                if(availableSkills.contains(Skills.ROCK_THROW))
                    return Skills.ROCK_THROW;
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SHOOT_THE_MOON) > 4))
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE) > 4))
            {
                if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                    return Skills.REVERSAL_OF_FORTUNE;
            }
            //If we can choose the ability then

            if(availableSkills.contains(Skills.ROCK_THROW))
                return Skills.ROCK_THROW;
            else
            {
                int random = randomGenerator.nextInt(2);
                if(random == 1)
                    return Skills.SCISSORS_POKE;
                else
                    return Skills.PAPER_CUT;
            }


        }
        else if(tracker.getPetHps().get(opponentPetIndex) < 75 && tracker.getPetHps().get(opponentPetIndex) > 25)
        {
            if(tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.ROCK_THROW) > 0)
            {
                if(availableSkills.contains(Skills.SCISSORS_POKE))
                    return Skills.SCISSORS_POKE;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SCISSORS_POKE) > 0))
            {
                if(availableSkills.contains(Skills.PAPER_CUT))
                    return Skills.PAPER_CUT;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.PAPER_CUT) > 0))
            {
                if(availableSkills.contains(Skills.ROCK_THROW))
                    return Skills.ROCK_THROW;
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SHOOT_THE_MOON) > 4))
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE) > 4))
            {
                if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                    return Skills.REVERSAL_OF_FORTUNE;
            }
            //If we can choose the ability then
            if(availableSkills.contains(Skills.SCISSORS_POKE))
                return Skills.SCISSORS_POKE;
            else
            {
                int random = randomGenerator.nextInt(2);
                if(random == 1)
                    return Skills.ROCK_THROW;
                else
                    return Skills.PAPER_CUT;
            }
        }
        else
        {
            if(tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.ROCK_THROW) > 0)
            {
                if(availableSkills.contains(Skills.SCISSORS_POKE))
                    return Skills.SCISSORS_POKE;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SCISSORS_POKE) > 0))
            {
                if(availableSkills.contains(Skills.PAPER_CUT))
                    return Skills.PAPER_CUT;
            }
            else if((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.PAPER_CUT) > 0))
            {
                if(availableSkills.contains(Skills.ROCK_THROW))
                    return Skills.ROCK_THROW;
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SHOOT_THE_MOON) > 4))
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
            }
            else if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE) > 4))
            {
                if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                    return Skills.REVERSAL_OF_FORTUNE;
            }

            if(availableSkills.contains(Skills.PAPER_CUT))
                return Skills.PAPER_CUT;
            else
            {
                int random = randomGenerator.nextInt(2);
                if(random == 1)
                    return Skills.SCISSORS_POKE;
                else
                    return Skills.ROCK_THROW;
            }
        }
    }

    private Skills enemySpeedType()
    {
        if((tracker.getPetRandDmgsTaken().get(selfIndex) - tracker.getPetRandDmgsGiven().get(selfIndex)) >= 0)
        {
            if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                return Skills.REVERSAL_OF_FORTUNE;
        }
        // always want to use reversal after the enemy does.
        if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE) > 4))
        {
            if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                return Skills.REVERSAL_OF_FORTUNE;
        }
        if(tracker.getPetHps().get(opponentPetIndex) >= 75)
        {
            //If we can choose the ability then
            if(availableSkills.contains(Skills.ROCK_THROW))
                return Skills.ROCK_THROW;
            else
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
                else
                {
                    int random = randomGenerator.nextInt(2);
                    if(random == 1)
                        return Skills.SCISSORS_POKE;
                    else
                        return Skills.PAPER_CUT;
                }
            }

        }
        else if(tracker.getPetHps().get(opponentPetIndex) < 75 && tracker.getPetHps().get(opponentPetIndex) > 25)
        {
            if(availableSkills.contains(Skills.SCISSORS_POKE))
                return Skills.SCISSORS_POKE;
            else
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
                else
                {
                    int random = randomGenerator.nextInt(2);
                    if(random == 1)
                        return Skills.ROCK_THROW;
                    else
                        return Skills.PAPER_CUT;
                }
            }
        }
        else
        {
            if(availableSkills.contains(Skills.PAPER_CUT))
                return Skills.PAPER_CUT;
            else
            {
                if(availableSkills.contains(Skills.SHOOT_THE_MOON))
                {
                    shootTheMoon();
                    return Skills.SHOOT_THE_MOON;
                }
                else
                {
                    int random = randomGenerator.nextInt(2);
                    if(random == 1)
                        return Skills.ROCK_THROW;
                    else
                        return Skills.SCISSORS_POKE;
                }
            }
        }
    }

    private Skills enemyIntelligenceType()
    {
        if(availableSkills.contains(Skills.SHOOT_THE_MOON))
        {
            shootTheMoon();
            return Skills.SHOOT_THE_MOON;
        }
        int rt = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.ROCK_THROW);
        int sp = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SCISSORS_POKE);
        int pc = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.PAPER_CUT);
        int stm = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SHOOT_THE_MOON);
        int rof = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE);
        int count = 0;
        if(rt > 0)
            count++;
        if(sp > 0)
            count++;
        if(pc > 0)
            count++;
        if(stm > 0)
            count++;
        if(rof > 0)
            count++;
        if(count >= 3)
        {
            if(rt > 0)
                setSmartPrediction(Skills.PAPER_CUT);
            else if(sp > 0)
                setSmartPrediction(Skills.ROCK_THROW);
            else
                setSmartPrediction(Skills.SCISSORS_POKE);
            if(availableSkills.contains(Skills.SHOOT_THE_MOON))
            {
                return Skills.SHOOT_THE_MOON;
            }
        }

        if((tracker.getPetRandDmgsTaken().get(selfIndex) - tracker.getPetRandDmgsGiven().get(selfIndex)) >= 0)
        {
            if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                return Skills.REVERSAL_OF_FORTUNE;
        }
        // always want to use reversal after the enemy does.
        if ((tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE) > 4))
        {
            if(availableSkills.contains(Skills.REVERSAL_OF_FORTUNE))
                return Skills.REVERSAL_OF_FORTUNE;
        }
        if(tracker.getPetHps().get(opponentPetIndex) >= 75)
        {
            //If we can choose the ability then
            if(availableSkills.contains(Skills.ROCK_THROW))
                return Skills.ROCK_THROW;
            else
            {
                //int random = randomGenerator.nextInt(2);
                if(rt > 0)
                    return Skills.PAPER_CUT;
                else
                    return Skills.SCISSORS_POKE;
            }

        }
        else if(tracker.getPetHps().get(opponentPetIndex) < 75 && tracker.getPetHps().get(opponentPetIndex) > 25)
        {
            if(availableSkills.contains(Skills.SCISSORS_POKE))
                return Skills.SCISSORS_POKE;
            else
            {
                //int random = randomGenerator.nextInt(2);
                if(sp > 0)
                    return Skills.ROCK_THROW;
                else
                    return Skills.PAPER_CUT;
            }
        }
        else
        {
            if(availableSkills.contains(Skills.PAPER_CUT))
                return Skills.PAPER_CUT;
            else
            {
                //int random = randomGenerator.nextInt(2);
                if(pc > 0)
                    return Skills.SCISSORS_POKE;
                else
                    return Skills.ROCK_THROW;
            }
        }
    }

    private void shootTheMoon()
    {
        int rt = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.ROCK_THROW);
        int sp = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SCISSORS_POKE);
        int pc = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.PAPER_CUT);
        int stm = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.SHOOT_THE_MOON);
        int rof = tracker.getPetCooldowns().get(opponentPetIndex).get(Skills.REVERSAL_OF_FORTUNE);
        int count = 0;
        if(rt > 0)
            count++;
        if(sp > 0)
            count++;
        if(pc > 0)
            count++;
        if(stm > 0)
            count++;
        if(rof > 0)
            count++;
        if(count >= 3)
        {
            if(rt > 0)
                setSmartPrediction(Skills.PAPER_CUT);
            else if(sp > 0)
                setSmartPrediction(Skills.ROCK_THROW);
            else
                setSmartPrediction(Skills.SCISSORS_POKE);
        }
        else if(rt > 0)
            setSmartPrediction(Skills.PAPER_CUT);
        else if(sp > 0)
            setSmartPrediction(Skills.ROCK_THROW);
        else if(pc > 0)
            setSmartPrediction(Skills.SCISSORS_POKE);
        else
            setSmartPrediction(Skills.ROCK_THROW);
    }
}
