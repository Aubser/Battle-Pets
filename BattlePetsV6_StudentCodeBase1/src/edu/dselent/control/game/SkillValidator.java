package edu.dselent.control.game;

import edu.dselent.player.Playable;
import edu.dselent.skill.Skills;

import java.util.*;

public class SkillValidator
{
    // playable uid -> map of skills -> recharge times
    private Map<Integer, Map<Skills, Integer>> skillRechargeMap = new HashMap<>();

    public SkillValidator(List<Playable> playableList)
    {
        for(Playable playable : playableList)
        {
            int playableUid = playable.getPlayableUid();
            Set<Skills> skillSet = playable.getSkillSet();

            Map<Skills, Integer> currentSkillRechargeMap = new HashMap<>();

            for (Skills nextSkill : skillSet)
            {
                currentSkillRechargeMap.put(nextSkill, 0);
            }

            skillRechargeMap.put(playableUid, currentSkillRechargeMap);
        }
    }

    // Could make this a one-liner
    public boolean validateSkillChoice(int playableUid, Skills skillChoice)
    {
        boolean valid = false;

        if(skillChoice != null)
        {
            int rechargeTime = skillRechargeMap.get(playableUid).get(skillChoice);

            if (!(rechargeTime > 0))
            {
                valid = true;
            }
            else
            {
                System.out.println(skillChoice);
            }
        }

        return valid;
    }

    public void setRechargeTime(int playableUid, Skills skill, int rechargeTime)
    {
        skillRechargeMap.get(playableUid).put(skill, rechargeTime);
    }

    public void updateRechargeTime()
    {
        Set<Integer> allUids = skillRechargeMap.keySet();

        for (int nextPlayableUid : allUids)
        {
            Set<Skills> skillKeySet = skillRechargeMap.get(nextPlayableUid).keySet();

            for (Skills currentSkill : skillKeySet)
            {
                updateRechargeTime(nextPlayableUid, currentSkill);
            }
        }
    }

    public void updateRechargeTimes(int playableUid)
    {
        Set<Skills> skillKeySet = skillRechargeMap.get(playableUid).keySet();

        for (Skills currentSkill : skillKeySet)
        {
            updateRechargeTime(playableUid, currentSkill);
        }
    }

    public void updateRechargeTime(int playableUid, Skills skill)
    {
        int currentRechargeTime = skillRechargeMap.get(playableUid).get(skill);

        if(currentRechargeTime > 0)
        {
            currentRechargeTime--;
        }

        skillRechargeMap.get(playableUid).put(skill, currentRechargeTime);
    }

    public void reset()
    {
        Set<Integer> allUids = skillRechargeMap.keySet();

        for (int nextPlayableUid : allUids)
        {
            Set<Skills> skillKeySet = skillRechargeMap.get(nextPlayableUid).keySet();

            for (Skills currentSkill : skillKeySet)
            {
                setRechargeTime(nextPlayableUid, currentSkill, 0);
            }
        }
    }
}
