package edu.dselent.player.defaultintelligence;

import edu.dselent.skill.Skills;

public class PaperCut extends Skill
{	
	private static final int RECHARGE_TIME = 1;

	public PaperCut()
	{
		super(RECHARGE_TIME);
		setRechargeTime(0);
		setSkillName(Skills.PAPER_CUT.toString());
	}
}
