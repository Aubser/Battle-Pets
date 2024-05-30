package edu.dselent.domain;

import edu.dselent.damage.DamageInfo;
import edu.dselent.skill.skilldata.SkillData;

public class PlayerRoundData
{
	private SkillData skillData;
	private DamageInfo damageInfo;
	private double hpAfter;
	private boolean awake;

	// TODO
	// Think of a way to make this immutable
	// Currently at the time this is created and stored it needs to exist incomplete
	public PlayerRoundData()
	{
	}

	public SkillData getSkillData()
	{
		return skillData;
	}

	public void setSkillData(SkillData skillData)
	{
		this.skillData = skillData;
	}

	public DamageInfo getDamageInfo()
	{
		return damageInfo;
	}

	public void setDamageInfo(DamageInfo damageInfo)
	{
		this.damageInfo = damageInfo;
	}

	public double getHpAfter()
	{
		return hpAfter;
	}

	public void setHpAfter(double hpAfter)
	{
		this.hpAfter = hpAfter;
	}

	public boolean isAwake()
	{
		return awake;
	}

	public void setAwake(boolean awake)
	{
		this.awake = awake;
	}


}
