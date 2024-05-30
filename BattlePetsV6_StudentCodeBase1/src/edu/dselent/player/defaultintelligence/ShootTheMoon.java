package edu.dselent.player.defaultintelligence;

import edu.dselent.skill.Skills;

public class ShootTheMoon extends Skill
{
	private static final int RECHARGE_TIME = 6;

	public ShootTheMoon()
	{
		super(RECHARGE_TIME);
		setRechargeTime(0);
		setSkillName(Skills.SHOOT_THE_MOON.toString());
	}
}
