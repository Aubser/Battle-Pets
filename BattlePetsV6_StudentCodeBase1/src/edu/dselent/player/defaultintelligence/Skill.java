package edu.dselent.player.defaultintelligence;

public abstract class Skill
{
	private int rechargeTime;
	private String skillName;
	
	private final int maxRechargeTime;
	
	public Skill(int maxRechargeTime)
	{
		this.maxRechargeTime = maxRechargeTime;
	}
	
	/**
	 * Sets the recharge time as if the skill was just used
	 */
	public void useSkill()
	{
		setRechargeTime(maxRechargeTime + 1);
		//+1 because a skill doesn't recharge the turn it was used
	}
	
	/**
	 * Recharges the skill by 1
	 */
	public void recharge()
	{
		if(this.rechargeTime > 0)
			rechargeTime--;
	}
	
	/**
	 * Checks if the skill is charged
	 * @return true if rechargeTime == 0
	 */
	public boolean isCharged()
	{
		return (rechargeTime == 0);
	}
	
	/**
	 * Sets rechargeTime to 0
	 */
	public void fullyCharge()
	{
		rechargeTime = 0;
	}
	
	@Override
	public String toString()
	{
		String skillString = "";
		if(isCharged())
			skillString = "";
		else if(!isCharged())
			skillString = skillName + " is recharging ";
		return skillString;
	}
	
	public int getRechargeTime()
	{
		return rechargeTime;
	}
	
	public void setRechargeTime(int rechargeTime)
	{
		this.rechargeTime = rechargeTime;
	}
	
	public String getSkillName()
	{
		return skillName;
	}
	
	public void setSkillName(String skillName)
	{
		this.skillName = skillName;
	}
}
