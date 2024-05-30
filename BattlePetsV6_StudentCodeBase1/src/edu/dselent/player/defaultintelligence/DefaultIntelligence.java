package edu.dselent.player.defaultintelligence;

import edu.dselent.event.*;
import edu.dselent.player.PetTypes;
import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.ArrayList;
import java.util.Random;

public class DefaultIntelligence extends Pet
{	
	private Random skillGenerator; // new random generator for AI
	private final long SEED = 1234567890; //hard-coded seed for AI random skillGenerator
	private int myIndex;
	private int myVictimIndex;
	private int myAttackerIndex;
	private double damageDifference;
	private ArrayList<ImportantPetInfo> petInfoList;
	private int playableUid;

	public DefaultIntelligence(int playableUid, PlayerSettings playerSettings)
	{
		super(playerSettings);
		this.playableUid = playableUid;
		this.skillGenerator = new Random(SEED);
		setPlayerType(PlayerTypes.DEFAULT_INTELLIGENCE);
		petInfoList = new ArrayList<ImportantPetInfo>();
	}

	@Override
	public int getPlayableUid()
	{
		return playableUid;
	}



	/**
	 * gets input from the AI and returns a valid skill
	 */
	public Skills chooseSkill() 
	{
		Skills currentPetSkill = null;
		ImportantPetInfo victim = petInfoList.get(myVictimIndex);
		if(victim.getPetType() == PetTypes.INTELLIGENCE)
		{
			currentPetSkill = intelligenceVsIntelligence();
		}
		else
		{
			currentPetSkill = intelligenceVsOther();
		}


		while(getSkillRechargeTime(currentPetSkill) > 0)
		{
			int randomChoiceInt = skillGenerator.nextInt(Skills.values().length);
			currentPetSkill = Skills.values()[randomChoiceInt];

			if(currentPetSkill == Skills.SHOOT_THE_MOON)
			{
				predictSkill();
			}
		}

		//getSkill(currentPetSkill).useSkill();

		return currentPetSkill;
	}

	@Override
	public void setPlayableUid(int playableUid)
	{
		this.playableUid = playableUid;
	}

	/**
	 * If SmartAi and its opponent are both of type intelligence, this method selects
	 * the skill for the smart Ai.  It is based on a offensive strategy.
	 * optimized for 2 pet battles, may not work well in 3+ pet battles
	 * @return
	 */
	private Skills intelligenceVsIntelligence() 
	{
		Skills currentPetSkill = null;
		ImportantPetInfo victim = petInfoList.get(myVictimIndex);
		ImportantPetInfo myInfo = petInfoList.get(myIndex);
		// three checks for +3 conditional damage
		if(!victim.isRecharged(Skills.SCISSORS_POKE) && myInfo.isRecharged(Skills.ROCK_THROW))
		{
			currentPetSkill = Skills.ROCK_THROW;
		}
		else if(!victim.isRecharged(Skills.PAPER_CUT) && myInfo.isRecharged(Skills.SCISSORS_POKE))
		{
			currentPetSkill = Skills.SCISSORS_POKE;
		}
		else if(!victim.isRecharged(Skills.ROCK_THROW) && myInfo.isRecharged(Skills.PAPER_CUT))
		{
			currentPetSkill = Skills.PAPER_CUT;
		}
		// if we will get +2 from Moon recharging, use a +2 skill
		else if(!victim.isRecharged(Skills.SHOOT_THE_MOON))
		{
			if(!victim.isRecharged(Skills.SCISSORS_POKE) && myInfo.isRecharged(Skills.SCISSORS_POKE))
			{
				currentPetSkill = Skills.SCISSORS_POKE;
			}
			else if(!victim.isRecharged(Skills.PAPER_CUT) && myInfo.isRecharged(Skills.PAPER_CUT))
			{
				currentPetSkill = Skills.PAPER_CUT;
			}
			else if(!victim.isRecharged(Skills.ROCK_THROW) && myInfo.isRecharged(Skills.ROCK_THROW))
			{
				currentPetSkill = Skills.ROCK_THROW;
			}
			else
			{
				currentPetSkill = chooseRandomSkill();
			}
		}
		// we can't do conditional based on Rock, Paper, or Scissors, so try to use Reversal
		else if(myInfo.isRecharged(Skills.REVERSAL_OF_FORTUNE))
		{
			currentPetSkill = Skills.REVERSAL_OF_FORTUNE;
		}
		// if no skill chosen, pick randomly
		else
		{
			currentPetSkill = chooseRandomSkill();
		}
		return currentPetSkill;
	}
	
	/**
	 * If SmartAi is of type intelligence and its opponent is not, this method selects
	 * the skill for the smart Ai.  This is based on a defensive strategy.  
	 * Our AI will choose the skill that would be critical hit by the enemy's recharging skill.
	 * Optimized for 2 pet battles, may not work well in 3+ pet battles.
	 * @return
	 */
	private Skills intelligenceVsOther() 
	{
		Skills currentPetSkill = null;
		ImportantPetInfo attacker = petInfoList.get(myAttackerIndex);
		ImportantPetInfo myInfo = petInfoList.get(myIndex);
		
		if(!attacker.isRecharged(Skills.ROCK_THROW) && myInfo.isRecharged(Skills.SCISSORS_POKE))
		{
			currentPetSkill = Skills.SCISSORS_POKE;
		}
		else if(!attacker.isRecharged(Skills.SCISSORS_POKE) && myInfo.isRecharged(Skills.PAPER_CUT))
		{
			currentPetSkill = Skills.PAPER_CUT;
		}
		else if(!attacker.isRecharged(Skills.PAPER_CUT) && myInfo.isRecharged(Skills.ROCK_THROW))
		{
			currentPetSkill = Skills.ROCK_THROW;
		}
		else if(myInfo.isRecharged(Skills.SHOOT_THE_MOON) && !(attacker.getPetType() == PetTypes.INTELLIGENCE))
		{
			currentPetSkill = Skills.SHOOT_THE_MOON;
			predictSkill();
		}
		else if(myInfo.isRecharged(Skills.REVERSAL_OF_FORTUNE) && damageDifference > 0)
		{
			currentPetSkill = Skills.REVERSAL_OF_FORTUNE;
		}
		else
		{
			currentPetSkill = chooseRandomSkill();
		}
		return currentPetSkill;
	}

	/**
	 * Given a skillNum, this method returns a skill instance
	 * @param skillEnum
	 * @return
	 */
	private Skill getSkill(Skills skillEnum)
	{
		Skill skill = null;
		switch (skillEnum) 
		{
		case ROCK_THROW:
			skill = getRock();
			break;
		case PAPER_CUT:
			skill = getPaper();
			break;
		case SCISSORS_POKE:
			skill = getScissors();
			break;
		case SHOOT_THE_MOON:
			skill = getMoon();
			break;
		case REVERSAL_OF_FORTUNE:
			skill = getReversal();
			break;
		}
		return skill;
	}

	/**
	 * sets predicted skill
	 */
	private void predictSkill() 
	{
		Skills predictedSkill = null;
		int skillNum = 0;
		boolean isRecharged = false;
		while (!isRecharged)
		{
		 	skillNum = getSkillNum();
		 	if(skillNum < 0)
				skillNum += Skills.values().length;
			predictedSkill = Skills.values()[skillNum];
			isRecharged = petInfoList.get(myVictimIndex).isRecharged(predictedSkill);
		}
		setCurrentSkillPrediction(predictedSkill);
	}
	
	/**
	 * returns a number 0 to Skills.values().length 
	 *                  0 to 4
	 * this number is the index of Skills.values() that we want
	 * @return
	 */
	private int getSkillNum() 
	{
		int skillNum = ((skillGenerator.nextInt() % Skills.values().length));
		if(skillNum < 0) // % can be negative
			skillNum += Skills.values().length;
		return skillNum;
	}

	/**
	 * Does the logic for random skill selection
	 * including predicting a skill
	 */
	private Skills chooseRandomSkill()
	{
		Skills currentPetSkill = null;
		boolean valid = false;
		int skillNum = -1;
		while(!valid)
		{
			skillNum = getSkillNum();
			currentPetSkill = Skills.values()[skillNum];
			valid = !isRecharging(currentPetSkill);
		}
		return currentPetSkill;
	}
	
	/**
	 * Takes Events from the Referee calls the appropriate method
	 */
	@Override
	public void update(Object event)
	{
		if(event instanceof BaseEvent)
		{
			switch(((BaseEvent) event).getEventType())
			{
			case ATTACK:
				handleAttackEvent((AttackEvent)event);
				break;
			case FIGHT_START:
				handleFightStartEvent((FightStartEvent)event);
				break;
			case ROUND_START:
				handleRoundStartEvent((RoundStartEvent)event);
				break;
			}
		}
	}
	
	/**
	 * decrements recharge times
	 * @param RoundStartEvent Isn't used now, but might be in the future
	 */
	private void handleRoundStartEvent(RoundStartEvent RoundStartEvent)
	{
		for(int i = 0; i < petInfoList.size(); i++)
		{
			petInfoList.get(i).decrementRechargeTimes();
		}
	}

	/**
	 * Clear knowledge of previous fight
	 * Create a new list of ImportantPetInfo
	 * @param fightStartEvent
	 */
	private void handleFightStartEvent(FightStartEvent fightStartEvent) 
	{
		damageDifference = 0;
		if(petInfoList != null)
			petInfoList.clear();
		
		ArrayList<PlayerEventInfo> playerEventInfoList 
				= (ArrayList<PlayerEventInfo>) fightStartEvent.getPlayerEventInfoList();
		
		for(int i = 0; i < playerEventInfoList.size(); i++)
		{
			PlayerEventInfo playerInfo = playerEventInfoList.get(i);
			if(getPetName().equals(playerInfo.getPetName()))
				this.myIndex = i;
			petInfoList.add(new ImportantPetInfo(playerInfo.getPetType(), playerInfo.getStartingHp()));
		}
	}

	/**
	 * takes the AttackEvent and uses it to update ImportantPetInfoList
	 */
	private void handleAttackEvent(AttackEvent attackEvent) 
	{
		int attackingIndex = attackEvent.getAttackingPlayableUid();
		int victimIndex = attackEvent.getVictimPlayableUid();
		ImportantPetInfo attackingPetInfo = petInfoList.get(attackingIndex);
		ImportantPetInfo victimPetInfo = petInfoList.get(victimIndex);
		double randomDamage = attackEvent.getDamage().getRandomDamage();
		
		attackingPetInfo.useSkill(attackEvent.getAttackingSkillChoice());
		victimPetInfo.updateHp(randomDamage + attackEvent.getDamage().getConditionalDamage());
		
		if(attackingIndex == myIndex)
		{
			damageDifference -= randomDamage; 
			myVictimIndex = victimIndex;
		}
		if(victimIndex == myIndex)
		{
			damageDifference += randomDamage; 
			myAttackerIndex = attackingIndex;
		}
	}

	/**
	 * An inner class that represents Pets.  Only the information
	 * important to our algorithms is stored.
	 */
	public static class ImportantPetInfo
	{
		private PetTypes petType;
		private double hp;
		private double maxHp;
		private RockThrow rock = new RockThrow();
		private ScissorsPoke scissors = new ScissorsPoke();
		private PaperCut paper = new PaperCut();
		private ShootTheMoon moon = new ShootTheMoon();
		private ReversalOfFortune reversal = new ReversalOfFortune();
		
		public ImportantPetInfo(PetTypes petType, double maxHp)
		{
			this.petType = petType;
			this.hp = maxHp;
			this.maxHp = maxHp;
		}
		
		/**
		 * Takes a skills enum and uses the skill, updating the recharge times
		 * @param skill
		 */
		public void useSkill(Skills skill) 
		{
			switch (skill) 
			{
			case ROCK_THROW:
				rock.useSkill();
				break;
			case PAPER_CUT:
				paper.useSkill();
				break;
			case SCISSORS_POKE:
				scissors.useSkill();
				break;
			case SHOOT_THE_MOON:
				moon.useSkill();
				break;
			case REVERSAL_OF_FORTUNE:
				reversal.useSkill();
				break;
			}
		}

		public PetTypes getPetType() 
		{
			return petType;
		}

		public double getHp() 
		{
			return hp;
		}
		
		public double getMaxHp()
		{ 
			return maxHp;
		}

		/**
		 * updates this.hp to take damage
		 * @param hp
		 */
		public void updateHp(double hp) 
		{
			this.hp -= hp;
		}
				
		/**
		 * takes an int representing the skills enum and returns
		 * the recharge time associated with it
		 * @param skillNum
		 * @return
		 */
		public boolean isRecharged(int skillNum)
		{
			boolean isCharged = false;
			switch (skillNum) 
			{
			case 1:
				isCharged = rock.isCharged();
				break;
			case 2:
				isCharged = paper.isCharged();
				break;
			case 3:
				isCharged = scissors.isCharged();
				break;
			case 4:
				isCharged = moon.isCharged();
				break;
			case 5:
				isCharged = reversal.isCharged();
				break;
			}
			return isCharged;
		}
		
		/**
		 * takes a skills enum and returns
		 * the recharge time associated with it
		 * @return
		 */
		public boolean isRecharged(Skills skill)
		{
			boolean isCharged = false;
			switch (skill) 
			{
			case ROCK_THROW:
				isCharged = rock.isCharged();
				break;
			case PAPER_CUT:
				isCharged = paper.isCharged();
				break;
			case SCISSORS_POKE:
				isCharged = scissors.isCharged();
				break;
			case SHOOT_THE_MOON:
				isCharged = moon.isCharged();
				break;
			case REVERSAL_OF_FORTUNE:
				isCharged = reversal.isCharged();
				break;
			}
			return isCharged;
		}
		
		/**
		 * recharges each skill by 1
		 */
		public void decrementRechargeTimes() 
		{
			rock.recharge();
			scissors.recharge();
			paper.recharge();
			moon.recharge();
			reversal.recharge();
		}
	}
}
