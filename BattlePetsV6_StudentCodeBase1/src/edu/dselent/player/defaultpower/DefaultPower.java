package edu.dselent.player.defaultpower;

import edu.dselent.event.AttackEvent;
import edu.dselent.event.AttackEventShootTheMoon;
import edu.dselent.event.FightStartEvent;
import edu.dselent.event.PlayerEventInfo;
import edu.dselent.player.PetInstance;
import edu.dselent.player.PetTypes;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
//import java.util.*;


public class DefaultPower extends PetInstance
{
	private static final int ROCK_THROW_COMMAND = 1;
	private static final int SCISSORS_POKE_COMMAND  = 2;
	private static final int PAPER_CUT_COMMAND = 3;
	private static final int SHOOT_THE_MOON_COMMAND = 4;
	private static final int REVERSAL_OF_FORTUNE_COMMAND = 5;

	private int myIndex = 0;
	
	private int victimIndex = 0;
	private String victimString = "";
	private PetTypes victimType;
	private Random rand;
	
	private int selfCumRandomDmg = 0;
	private int enemyCumRandomDmg = 0;
	private int enemyROFCooldown = 0;
	private Skills attackingSkillChoice;
	private Skills predictedSkillEnum;
	private double randomDamage;
	private double conditionalDamage;
	
	private int enemyRockThrowCD = 0;
	private int enemyScissorPokeCD = 0;
	private int enemyPaperCutCD = 0;
	private int enemyShootTheMoonCD = 0;
	private int enemyReversalOfFortuneCD = 0;
	

	
	public DefaultPower(int playableUid, PlayerSettings playerSettings)
	{
		super(playableUid, playerSettings);
		rand = new Random(5);
	}
	
	

	
	
	public int selectForROF()
	{
		
		
		
		
		
		
		return 0;
	}
	
	
	public Skills selectForRockThrow() 
	{
		
		
		
		
		
		
		
		
		
		return Skills.PAPER_CUT;
	}
	


	/**
	 * returns the chosen skill for the computer pet
	 */
	public Skills chooseSkill()
	{
		//System.out.println("Pick a skill based on events");
		//System.out.println("I am in index: " + this.myIndex);
		
		int skillChoice = 0;
		int predictedSkillChoice = 0;
		boolean validAIChoice = false;
		String strCmdAI = "  ";
		 
		
		if(this.victimType == PetTypes.POWER || this.victimType == PetTypes.SPEED)
		{
		while(!validAIChoice)
		{
			
			if(attackingSkillChoice == Skills.ROCK_THROW)
			{
				
				if(this.validateSelection(this.SCISSORS_POKE_COMMAND, "oo"))
					return Skills.SCISSORS_POKE;
				else if(this.validateSelection(this.SHOOT_THE_MOON_COMMAND, "oo"))
					return Skills.SHOOT_THE_MOON;
				else
					return Skills.ROCK_THROW;
				
			}
			
			else if(attackingSkillChoice == Skills.SCISSORS_POKE)
			{
				if(this.validateSelection(this.PAPER_CUT_COMMAND, "oo"))
					return Skills.PAPER_CUT;
				else if(this.validateSelection(this.SHOOT_THE_MOON_COMMAND, "oo"))
					return Skills.SHOOT_THE_MOON;
				else
					return Skills.SCISSORS_POKE;
			}
			
			else if(attackingSkillChoice == Skills.PAPER_CUT)
			{
				if(this.validateSelection(this.ROCK_THROW_COMMAND, "oo"))
					return Skills.ROCK_THROW;
				else if(this.validateSelection(this.SHOOT_THE_MOON_COMMAND, "oo"))
					return Skills.SHOOT_THE_MOON;
				else
					return Skills.PAPER_CUT;
			}
			
			
			else if(attackingSkillChoice == Skills.SHOOT_THE_MOON)
			{
//				int informedChoice = skillChoiceGen.coinFlip();
//				
//				if(informedChoice == 1)
//					return Skills.ROCK_THROW;
//				else
//					return Skills.PAPER_CUT;
			}
			
			else if(attackingSkillChoice == Skills.REVERSAL_OF_FORTUNE)
			{
//				int informedChoice = skillChoiceGen.coinFlip();
//				
//				if(informedChoice == 1)
//					return Skills.ROCK_THROW;
//				else
//					return Skills.PAPER_CUT;
			}
		
			
			
			
			
			skillChoice = (int) Math.ceil(getZeroToFiveNum());
			validAIChoice = validateSelection(skillChoice,strCmdAI);
			
			if(skillChoice == SHOOT_THE_MOON_COMMAND)
			{
				boolean rockAvailable = true;
				boolean scissorAvailable = true;
				boolean paperAvailable = true;
				boolean shootTheMoonAvailable = true;
				boolean reversalOfFortuneAvailable = true;
				if(this.enemyRockThrowCD >= 1)
					rockAvailable = false;
				
				if(this.enemyScissorPokeCD >= 1)
					scissorAvailable = false;
				
				if(this.enemyPaperCutCD >= 1)
					paperAvailable = false;
				
				if(this.enemyShootTheMoonCD >= 1)
					shootTheMoonAvailable = false;
				
				if(this.enemyReversalOfFortuneCD >= 1)
					reversalOfFortuneAvailable = false;
				
				//////////////////////////////////////////////////////////////
				
				if(shootTheMoonAvailable)
					predictedSkillChoice = 4;
				
				else if(rockAvailable)
					predictedSkillChoice = 1;
				
				else if(scissorAvailable)
					predictedSkillChoice = 2;
				
				else if(paperAvailable)
					predictedSkillChoice = 3;
				
				else if(reversalOfFortuneAvailable)
					predictedSkillChoice = 5;
				
			}
		}
		
		return getChosenSkill(skillChoice);
		}
		else
		{
			
			// TODO: Intelligence
			
			while(!validAIChoice)
			{
				
				if(attackingSkillChoice == Skills.ROCK_THROW)
				{
					int informedChoice = coinFlip();
					
					if(informedChoice == 1&& getSkillRechargeTime(Skills.ROCK_THROW) == 0)
						return Skills.ROCK_THROW;
					else if(getSkillRechargeTime(Skills.SCISSORS_POKE) == 0)
						return Skills.SCISSORS_POKE;
					else
						return Skills.ROCK_THROW;
				}
				
				else if(attackingSkillChoice == Skills.SCISSORS_POKE)
				{
					int informedChoice = coinFlip();
					
					if(informedChoice == 1 && getSkillRechargeTime(Skills.PAPER_CUT) == 0)
						return Skills.PAPER_CUT;
					else if(getSkillRechargeTime(Skills.SCISSORS_POKE) == 0)
						return Skills.SCISSORS_POKE;
					else
						return Skills.PAPER_CUT;
				}
				
				else if(attackingSkillChoice == Skills.PAPER_CUT)
				{
					int informedChoice = coinFlip();
					
					if(informedChoice == 1 && getSkillRechargeTime(Skills.ROCK_THROW) == 0)
						return Skills.PAPER_CUT;
					else if(getSkillRechargeTime(Skills.ROCK_THROW) == 0)
						return Skills.ROCK_THROW;
					else
						return Skills.PAPER_CUT;
				}
				
//				else if(attackingSkillChoice == Skills.SHOOT_THE_MOON)
//				{
//					
//					
//					
//				}
//				
//				else if(attackingSkillChoice == Skills.REVERSAL_OF_FORTUNE)
//				{
	//
//					
//				}
			
				
				skillChoice = (int) Math.ceil(getZeroToFiveNum());
				validAIChoice = validateSelection(skillChoice,strCmdAI);
				
				if(skillChoice == this.SHOOT_THE_MOON_COMMAND)
				{
					boolean moonCheck = false;
					
					while(!moonCheck) 
					{
						predictedSkillChoice = (int) Math.ceil(getZeroToFiveNum());
						moonCheck = validateSelection(predictedSkillChoice,strCmdAI);
						Skills temp = getChosenSkill(predictedSkillChoice);
						
						if(temp == attackingSkillChoice)
							moonCheck = false;
					}	
				}
			}
			
			return getChosenSkill(skillChoice);
		}
		
	}
	
	/**
	 * REturns the skill prediction for the computer player
	 */
	public Skills getSkillPrediction() 
	{
		int predictedSkillChoice = 0;
		
		
		boolean rockAvailable = true;
		boolean scissorAvailable = true;
		boolean paperAvailable = true;
		boolean shootTheMoonAvailable = true;
		boolean reversalOfFortuneAvailable = true;
		
		if(this.enemyRockThrowCD >= 1)
			rockAvailable = false;
		
		if(this.enemyScissorPokeCD >= 1)
			scissorAvailable = false;
		
		if(this.enemyPaperCutCD >= 1)
			paperAvailable = false;
		
		if(this.enemyShootTheMoonCD >= 1)
			shootTheMoonAvailable = false;
		
		if(this.enemyReversalOfFortuneCD >= 1)
			reversalOfFortuneAvailable = false;
		
		//////////////////////////////////////////////////////////////
		
		if(shootTheMoonAvailable)
			predictedSkillChoice = 4;
		
		else if(rockAvailable)
			predictedSkillChoice = 1;
		
		else if(scissorAvailable)
			predictedSkillChoice = 2;
		
		else if(paperAvailable)
			predictedSkillChoice = 3;
		
		else if(reversalOfFortuneAvailable)
			predictedSkillChoice = 5;
		///////////////////////////////////////////////////
		
		switch (predictedSkillChoice) 
		{
		case ROCK_THROW_COMMAND:
			return Skills.ROCK_THROW;
			
		case SCISSORS_POKE_COMMAND:
			return Skills.SCISSORS_POKE;
			
		case PAPER_CUT_COMMAND:
			return Skills.PAPER_CUT;
			
		case SHOOT_THE_MOON_COMMAND:
			return Skills.SHOOT_THE_MOON;
			
		case REVERSAL_OF_FORTUNE_COMMAND:
			return Skills.REVERSAL_OF_FORTUNE;
			
		default:
			return null;
		}
	};
	
	/**
	 *	validates the selection for the computer player
	 */
	protected boolean validateSelection(int cmd, String strCmd)
	{
		if (strCmd == "") 
			throw new InputMismatchException();
		
		else if (cmd < 0 || cmd > 5) 
			throw new InputMismatchException(); 
		
		else if (cmd == ROCK_THROW_COMMAND && getSkillRechargeTime(Skills.ROCK_THROW) > 0) 
			return false;
		
		else if (cmd == SCISSORS_POKE_COMMAND && getSkillRechargeTime(Skills.SCISSORS_POKE) > 0) 
			return false;

		else if (cmd == PAPER_CUT_COMMAND && getSkillRechargeTime(Skills.PAPER_CUT) > 0) 
			return false;
		
		else if (cmd == SHOOT_THE_MOON_COMMAND && getSkillRechargeTime(Skills.SHOOT_THE_MOON) > 0) 
			return false;
		
		else if (cmd == REVERSAL_OF_FORTUNE_COMMAND && getSkillRechargeTime(Skills.REVERSAL_OF_FORTUNE) > 0) 
			return false;
		
		return true;
	};
	
	private void decreaseCD()
	{
		if(this.enemyRockThrowCD > 0)
			this.enemyRockThrowCD--;
		
		if(this.enemyScissorPokeCD > 0)
			this.enemyScissorPokeCD--;
		
		if(this.enemyPaperCutCD > 0)
			this.enemyPaperCutCD--;
		
		if(this.enemyShootTheMoonCD > 0)
			this.enemyShootTheMoonCD--;
		
		if(this.enemyReversalOfFortuneCD > 0)
			this.enemyReversalOfFortuneCD--;
	}
	
	private void updateCooldowns(Skills skillUsed)
	{
		this.decreaseCD();
		
		if(skillUsed == Skills.ROCK_THROW)
		{
			this.enemyRockThrowCD = 1;
		}
		else if(skillUsed == Skills.SCISSORS_POKE)
		{
			this.enemyScissorPokeCD = 1;
		}
		else if(skillUsed == Skills.PAPER_CUT)
		{
			this.enemyPaperCutCD = 1;
		}
		else if(skillUsed == Skills.SHOOT_THE_MOON)
		{
			this.enemyShootTheMoonCD = 6;
		}
		else if(skillUsed == Skills.REVERSAL_OF_FORTUNE)
		{
			this.enemyReversalOfFortuneCD = 6;
		}
	}
	
	/*
	 * private final Skills attackingSkillChoice;
		private final Skills predictedSkillEnum;
		private final double randomDamage;
		private final double conditionalDamage;
	 */
	@Override
	public void update(Object arg)
	{
		// TODO Auto-generated method stub
		
		if(arg instanceof AttackEvent)
		{
			AttackEvent ae = (AttackEvent)arg;
			
			int victimPlayableIndex = ae.getVictimPlayableUid();
			if(victimPlayableIndex == this.victimIndex)
			{
				Skills attackingSkill1 = ae.getAttackingSkillChoice();
				Skills predictedSkillEnum1 = null;

				if(ae instanceof AttackEventShootTheMoon)
				{
					predictedSkillEnum1 = ((AttackEventShootTheMoon)ae).getPredictedSkillEnum();
				}
				double randomDamage1 = ae.getDamage().getRandomDamage();
				double conditonalDamage1 = ae.getDamage().getConditionalDamage();

				this.attackingSkillChoice = attackingSkill1;
				this.predictedSkillEnum = predictedSkillEnum1;
				this.randomDamage = randomDamage1;
				this.conditionalDamage = conditonalDamage1;
				
				this.updateCooldowns(attackingSkill1);
				

				
				//----------------------------------------------------
				this.selfCumRandomDmg += 1;
				this.enemyCumRandomDmg += 1;
				//----------------------------------------------------
			}
			
			
			
		}
		else if(arg instanceof FightStartEvent)
		{
			FightStartEvent fe = (FightStartEvent)arg;
			List<PlayerEventInfo> playerEventInfo = fe.getPlayerEventInfoList();
			
			for(int i = 0; i < playerEventInfo.size(); i++)
			{
				if(playerEventInfo.get(i).getPetName() == this.getPetName())
				{
					this.myIndex = i;
					if((i+1) == playerEventInfo.size())
					{
						this.victimIndex = 0;
					}
					else
					{
						this.victimIndex = (i+1);
					}
				}
			}
			this.victimString = playerEventInfo.get(this.victimIndex).getPetName();
			this.victimType = playerEventInfo.get(this.victimIndex).getPetType();
		}		
	}
	
	private double getZeroToFiveNum() {
		double randomValue = 0 + (5 - 0) * rand.nextDouble();
		return randomValue;
	}
	
	public int coinFlip() {
		int randomValue = 0 + (1 - 0) * rand.nextInt();
		return randomValue;
	}
	
	private Skills getChosenSkill(int cmd)
	{
		switch (cmd) 
		{
		case ROCK_THROW_COMMAND:
			return Skills.ROCK_THROW;
			
		case SCISSORS_POKE_COMMAND:
			return Skills.SCISSORS_POKE;
			
		case PAPER_CUT_COMMAND:
			return Skills.PAPER_CUT;
			
		case SHOOT_THE_MOON_COMMAND:
			return Skills.SHOOT_THE_MOON;
			
		case REVERSAL_OF_FORTUNE_COMMAND:
			return Skills.REVERSAL_OF_FORTUNE;
			
		default:
			return null;
		}
	}


}
