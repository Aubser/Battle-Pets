package edu.dselent.player.defaultintelligence;

import edu.dselent.skill.Skills;

public class RockThrow extends Skill
{
	private static final int RECHARGE_TIME = 1;
	
	public RockThrow()
	{
		super(RECHARGE_TIME);
		setRechargeTime(0);
		setSkillName(Skills.ROCK_THROW.toString());
	}
	
	
}
