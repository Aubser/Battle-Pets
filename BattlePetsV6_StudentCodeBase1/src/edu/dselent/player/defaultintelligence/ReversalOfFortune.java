package edu.dselent.player.defaultintelligence;

import edu.dselent.skill.Skills;

public class ReversalOfFortune extends Skill
{
	private static final int RECHARGE_TIME = 6;
	
	public ReversalOfFortune()
	{
		super(RECHARGE_TIME);
		setRechargeTime(0);
		setSkillName(Skills.REVERSAL_OF_FORTUNE.toString());
	}
}
