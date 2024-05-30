package edu.dselent.player.defaultintelligence;
import edu.dselent.player.PetTypes;
import edu.dselent.player.Playable;
import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;



@SuppressWarnings("deprecation")
public abstract class Pet implements Playable
{
	private PetTypes petType;
	private String petName;
	private String playerName;
	private double maxHp;
	private double hp;
	private Skills currentSkillPrediction = null;
	private PlayerTypes playerType; 
	private Pet watchedPet;
	
	private RockThrow rock = new RockThrow();
	private ScissorsPoke scissors = new ScissorsPoke();
	private PaperCut paper = new PaperCut();
	private ShootTheMoon moon = new ShootTheMoon();
	private ReversalOfFortune reversal = new ReversalOfFortune();
	
	public RockThrow getRock() {
		return rock;
	}

	public ScissorsPoke getScissors() {
		return scissors;
	}

	public PaperCut getPaper() {
		return paper;
	}

	public ShootTheMoon getMoon() {
		return moon;
	}

	public ReversalOfFortune getReversal() {
		return reversal;
	}

	public Pet(PlayerSettings playerSettings)
	{
		this.playerName = playerSettings.getPlayerName();
		this.petName = playerSettings.getPetName();
		this.petType = playerSettings.getPetType();
		this.maxHp = playerSettings.getStartingHp();
		watchedPet = this;
	}

	public double getStartingHp()
	{
		return maxHp;
	}

	@Override
	public String getPlayerName() 
	{
		return playerName;
	}
	
	@Override
	public String getPetName()
	{
		return petName;
	}

	@Override
	public PlayerTypes getPlayerType()
	{
		return playerType;
	}

	@Override
	public PetTypes getPetType() 
	{
		return petType;
	}
	
	@Override
	public double getCurrentHp() 
	{
		return hp;
	}
	
	@Override
	public abstract Skills chooseSkill();

	@Override
	public void updateHp(double hp) 
	{
		this.hp -= hp;
	}
	
	@Override
	public void resetHp() 
	{
		this.hp = maxHp;
	}
	
	@Override
	public void setCurrentHp(double currentHp) 
	{
		this.hp = currentHp;
	}
	
	@Override
	public boolean isAwake() 
	{
		return hp > 0;
	}
	
	@Override
	public Skills getSkillPrediction()
	{
		return currentSkillPrediction;
	}

	@Override
	public int getSkillRechargeTime(Skills skill) 
	{
		if(skill == Skills.ROCK_THROW)
		    return rock.getRechargeTime();
		else if(skill == Skills.SCISSORS_POKE)
		    return scissors.getRechargeTime();
		else if(skill == Skills.PAPER_CUT)
		    return paper.getRechargeTime();
		else if(skill == Skills.SHOOT_THE_MOON)
		    return moon.getRechargeTime();
		else if(skill == Skills.REVERSAL_OF_FORTUNE)
		    return reversal.getRechargeTime();
		else
			return 0;
	}

	@Override
	public double calculateHpPercent() 
	{
		return (hp / maxHp);
	}

	@Override
	public void reset()
	{
		resetHp();
		rock.fullyCharge();
		scissors.fullyCharge();
		paper.fullyCharge();
		moon.fullyCharge();
		reversal.fullyCharge();
		currentSkillPrediction = null;
	}
	
	@Override
	public void decrementRechargeTimes() 
	{
		rock.recharge();
		scissors.recharge();
		paper.recharge();
		moon.recharge();
		reversal.recharge();
	}
	
	@Override
	public void setRechargeTime(Skills skill, int rechargeTime)
	{
		if(skill == Skills.ROCK_THROW)
		    rock.setRechargeTime(rechargeTime);
		else if(skill == Skills.SCISSORS_POKE)
		    scissors.setRechargeTime(rechargeTime);
		else if(skill == Skills.PAPER_CUT)
		    paper.setRechargeTime(rechargeTime);
		else if(skill == Skills.SHOOT_THE_MOON)
		    moon.setRechargeTime(rechargeTime);
		else if(skill == Skills.REVERSAL_OF_FORTUNE)
		    reversal.setRechargeTime(rechargeTime);
	}
	
	//Above is interface methods
	//Below is protected methods common to AI and Human
	
	protected void setCurrentSkillPrediction(Skills skill)
	{
		this.currentSkillPrediction = skill;
	}
	
	protected void setPlayerType(PlayerTypes type) 
	{
		this.playerType = type;
	}
	
	/**
	 * Takes a skill and checks if it is recharging
	 * @param skill
	 * @return
	 */
	protected boolean isRecharging(Skills skill) 
	{
		boolean isRecharging = false;
		if(getSkillRechargeTime(skill) != 0)
		{
			if(playerType == PlayerTypes.HUMAN)
				System.out.println(skill + " is on cooldown, select another.");
			isRecharging = true;
		}
		return isRecharging;
	}
}
