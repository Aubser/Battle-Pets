package edu.dselent.control.game;

import java.util.*;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.config.Constants;
import edu.dselent.customexceptions.InvalidSkillChoiceException;
import edu.dselent.damage.Calculatable;
import edu.dselent.damage.Damage;
import edu.dselent.damage.DamageCalculator;
import edu.dselent.damage.DamageInfo;
import edu.dselent.domain.PlayerRoundData;
import edu.dselent.domain.RngHolder;
import edu.dselent.domain.Round;
import edu.dselent.event.AttackEvent;
import edu.dselent.event.AttackEventShootTheMoon;
import edu.dselent.event.RoundStartEvent;
import edu.dselent.io.IoManager;
import edu.dselent.player.PetTypes;
import edu.dselent.player.Playable;
import edu.dselent.skill.Skill;
import edu.dselent.skill.SkillRegistry;
import edu.dselent.skill.Skills;
import edu.dselent.skill.skilldata.ShootTheMoonData;
import edu.dselent.skill.skilldata.SkillData;
import edu.dselent.unused.OutputObject;

// TODO
// When running season mode or battle royale, comment out all output lines in this file

// TODO
// Need to use a bidirectional map for uid and index
// Current code may not work correctly if uid != index
public class TextRoundControl
{
	private TextFightControl textFightControl;
	private Calculatable damageCalculator;
	private Round currentRound;
	private SkillValidator theValidator;
	
	TextRoundControl(TextFightControl textFightControl)
	{
		this.textFightControl = textFightControl;
		this.theValidator = textFightControl.getTheValidator();
		this.damageCalculator = new DamageCalculator(this);
	}

	// TODO refactor
	void runRound(Round round)
	{
		//Scanner s = new Scanner(System.in);
		//s.nextLine();

        // For debugging
		OutputObject outputObject = new OutputObject();

		currentRound = round;

		List<Playable> playableList = currentRound.getFight().getPlayableList();
		
		/*//////*/IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		/*//////*/ioManager.getOutputSender().outputString("Round " + (currentRound.getRoundIndex()+1) + " Started");
		/*//////*/ioManager.getOutputSender().outputString("\n");
		/*//////*/ioManager.getOutputSender().outputString("Pets");
		/*//////*/ioManager.getOutputSender().outputString("\n");
		
		for(int i=0; i<playableList.size(); i++)
		{
			Playable currentPlayer = playableList.get(i);
			/*//////*/ioManager.getOutputSender().outputString("Pet " + (i+1));
			/*//////*/ioManager.getOutputSender().outputString("Pet Name: " + currentPlayer.getPetName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Type: " + currentPlayer.getPetType());
			/*//////*/ioManager.getOutputSender().outputString("Current HP: " + currentPlayer.getCurrentHp());
			/*//////*/ioManager.getOutputSender().outputString("\n");

			outputObject.addHp(currentPlayer.getCurrentHp());

			// TODO output skill set
		}

		/*//////*/ioManager.getOutputSender().outputString("Sorted by HP");
		/*//////*/ioManager.getOutputSender().outputString("\n");
		
		List<Playable> sortedPlayableList = new ArrayList<>(playableList);
		sortedPlayableList.sort((p1, p2) -> Double.compare(p2.getCurrentHp(), p1.getCurrentHp()));
		
		for(int i=0; i<sortedPlayableList.size(); i++)
		{
			Playable currentPlayer = sortedPlayableList.get(i);
			/*//////*/ioManager.getOutputSender().outputString("Pet " + (i+1));
			/*//////*/ioManager.getOutputSender().outputString("Pet Name: " + currentPlayer.getPetName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Type: " + currentPlayer.getPetType());
			/*//////*/ioManager.getOutputSender().outputString("Current HP: " + currentPlayer.getCurrentHp());
			/*//////*/ioManager.getOutputSender().outputString("\n");
			
			// TODO output skill set
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		EventBus eventBus = textFightControl.getEventBus();

		RoundStartEvent roundStartEvent = new RoundStartEvent(currentRound.getRoundIndex());
		eventBus.fireEvent(roundStartEvent);

		// Choose skill and prediction
		for (Playable playable : playableList)
		{
			// What was this for again?
			// ^I use these to store skill choices so I only call from the playable once
			// ^Also used for information storage to keep track of the game history
			// ^Also helpful for applying damage when pets fall asleep
			SkillData skillData;
			boolean awake = true;

			if (playable.isAwake())
			{
				Skills skillChoice = playable.chooseSkill();

				boolean valid = theValidator.validateSkillChoice(playable.getPlayableUid(), skillChoice);

				if(!valid)
				{
					throw new InvalidSkillChoiceException(playable.getPlayableUid(), playable.getPetName(), skillChoice);
				}

				if (skillChoice == Skills.SHOOT_THE_MOON)
				{
					Skills skillPrediction = playable.getSkillPrediction();
					skillData = new ShootTheMoonData(skillChoice, skillPrediction);
				}
				else
				{
					skillData = new SkillData(skillChoice);
				}
			}
			else
			{
				skillData = new SkillData(null);
				awake = false;
			}

			PlayerRoundData playerRoundData = new PlayerRoundData();
			playerRoundData.setSkillData(skillData);
			playerRoundData.setAwake(awake);
			currentRound.getPlayerRoundDataList().add(playerRoundData);
		}


		// Calculate and damage done
		// Used for storing temporary information to handle the adjusting of random damage
		// Victim Uid - damage dealt to victim
		Map<Integer, DamageInfo> damageMap = new HashMap<>();
		int awakeCount = 0;

		for(int i=0; i<playableList.size(); i++)
		{
			Playable player = playableList.get(i);
			
			if(player.isAwake())
			{
				awakeCount++;
				int victimPlayerIndex = findVictimPlayerIndex(i);
				Damage currentDamage = damageCalculator.calculateDamage(i, victimPlayerIndex);
				DamageInfo damageInfo = new DamageInfo(i, victimPlayerIndex, currentDamage);
				damageMap.put(i, damageInfo);
			}
		}

		// Adjust random damage to have a zero balance of random damage difference for awake pets
		// Only if number of awake players > 2, otherwise random damage is useless
		if(awakeCount > 2)
		{
			adjustRandomDamageBalance(playableList, damageMap);
		}

		// Store damage
		for(Integer attackingPlayableIndex : damageMap.keySet())
		{
			DamageInfo damageInfo = damageMap.get(attackingPlayableIndex);

			PlayerRoundData playerRoundData = round.getPlayerRoundDataList().get(attackingPlayableIndex);
			playerRoundData.setDamageInfo(damageInfo);
		}


		// Decrement skill recharge times
		// Set skill recharge time
		// Apply damage

		for (Playable player : playableList)
		{
			if (player.isAwake())
			{
				player.decrementRechargeTimes();
				theValidator.updateRechargeTimes(player.getPlayableUid());
			}
		}
		
		for(int i=0; i<playableList.size(); i++)
		{
			Playable player = playableList.get(i);
			
			if(player.isAwake())
			{
				PlayerRoundData playerRoundData = round.getPlayerRoundDataList().get(i);
				Skills chosenSkill = playerRoundData.getSkillData().getSkill();
				
				//TODO improve
				List<Skill> skillList = SkillRegistry.INSTANCE.getSkillList();
				int rechargeTime = -1;
				
				for(Skill skill : skillList)
				{
					if(skill.getSkillEnum() == chosenSkill)
					{
						rechargeTime = skill.getRechargeTime();
					}
				}
				//
				
				player.setRechargeTime(chosenSkill, rechargeTime);
				theValidator.setRechargeTime(player.getPlayableUid(), chosenSkill, rechargeTime);
			}
		}
		
		// for each player
			// map for given
			// map for taken
		
		Map<Integer, Double> randomTakenMap = new HashMap<>();
		Map<Integer, Double> randomGivenMap = new HashMap<>();
	
		// need random damage difference for this turn
		// fight.updateRandomDamageDifference
		
		List<PlayerRoundData> playerRoundDataList = currentRound.getPlayerRoundDataList();
		for(int attackingPlayableIndex=0; attackingPlayableIndex<playerRoundDataList.size(); attackingPlayableIndex++)
		{
			PlayerRoundData playerRoundData = playerRoundDataList.get(attackingPlayableIndex);

			if(playerRoundData.isAwake())
			{
				SkillData skillData = playerRoundData.getSkillData();
				Skills skillChoice = skillData.getSkill();
				Skills predictedSkillEnum = null;
				
				if(skillData instanceof ShootTheMoonData)
				{
					ShootTheMoonData stmData = (ShootTheMoonData)skillData;
					predictedSkillEnum = stmData.getPredictedSkillEnum();
				}
								
				Damage damage = playerRoundData.getDamageInfo().getDamage();
				int victimPlayerIndex = playerRoundData.getDamageInfo().getVictimPlayableUid();
				
				randomGivenMap.put(attackingPlayableIndex, damage.getRandomDamage());
				randomTakenMap.put(victimPlayerIndex, damage.getRandomDamage());
			
				Playable victimPlayer = playableList.get(victimPlayerIndex);
				victimPlayer.updateHp(damage.calculateTotalDamage());

				AttackEvent attackEvent;

				if(skillChoice == Skills.SHOOT_THE_MOON)
				{
					AttackEventShootTheMoon.AttackEventShootTheMoonBuilder stmBuilder = new AttackEventShootTheMoon.AttackEventShootTheMoonBuilder();
					attackEvent = stmBuilder.withAttackingPlayableUid(attackingPlayableIndex)
							.withVictimPlayableUid(victimPlayerIndex)
							.withAttackingSkillChoice(skillChoice)
							.withDamage(damage)
							.withPredictedSkillEnum(predictedSkillEnum)
							.build();
				}
				else
				{

					attackEvent = new AttackEvent(attackingPlayableIndex, victimPlayerIndex, skillChoice, damage);
				}
				
				eventBus.fireEvent(attackEvent);
				
				StringBuilder sb = new StringBuilder();
				sb.append(playableList.get(attackingPlayableIndex).getPetName());
				sb.append(" Uses ");
				sb.append(skillChoice.toString());
				
				if(skillChoice == Skills.SHOOT_THE_MOON)
				{
					sb.append(" with a prediction of ");
					sb.append(predictedSkillEnum);
				}
				
				sb.append(" and does ");
				sb.append(damage.getRandomDamage());
				sb.append(" random damage and ");
				sb.append(damage.getConditionalDamage());
				sb.append(" conditional damage to ");
				sb.append(playableList.get(victimPlayerIndex).getPetName());


				outputObject.addBalance(round.getFight().getRandomDamageDifference(attackingPlayableIndex));
				outputObject.addSkill(skillChoice);
				outputObject.addRandomDamage(damage.getRandomDamage());
				outputObject.addConditionalDamage(damage.getConditionalDamage());


				/*//////*/ioManager.getOutputSender().outputString(sb.toString());
			}
		}

		// Update hp in playerRoundData after damage was applied to all playables

		for(int i=0; i< playableList.size(); i++)
		{
			Playable playable = playableList.get(i);
			PlayerRoundData playerRoundData = playerRoundDataList.get(i);

			playerRoundData.setHpAfter(playable.getCurrentHp());
		}

		// End of update



		// for each player
			// map for given
			// map for taken
		
			// need random damage difference for this turn
			// fight.updateRandomDamageDifference
		
		for(int i=0; i<playerRoundDataList.size(); i++)
		{			
			PlayerRoundData playerRoundData = playerRoundDataList.get(i);
			
			if(playerRoundData.isAwake())
			{
				double given = randomGivenMap.get(i);
				double taken = randomTakenMap.get(i);
				double difference = given - taken;
				round.getFight().updateRandomDamageDifference(i, difference);
			}
		}
		
		//ioManager.getOutputSender().outputString("\n");

		// Useful for debugging
		//System.out.println(outputObject);
	}

	// TODO refactor
	// I'm pretty sure there is not an infinite loop here
	// There will be a zero balance eventually
	private void adjustRandomDamageBalance(List<Playable> playableList, Map<Integer, DamageInfo> damageMap)
	{
		// need to ensure any pets who fall asleep have zero random damage balance

		boolean damageBalanced = false;

		while(!damageBalanced)
		{
			// assume balanced unless otherwise not
			damageBalanced = true;

			// get set of pets who will be falling asleep

			Set<Playable> fallingAsleepSet = new HashSet<>();

			for (Playable playable : playableList)
			{
				// only for pets who are awake this round
				if (playable.isAwake())
				{
					int attackingPlayableUid = playable.getPlayableUid();
					int victimPlayableUid = damageMap.get(attackingPlayableUid).getVictimPlayableUid();

					Playable victimPlayable = playableList.get(victimPlayableUid);
					double currentVictimHp = victimPlayable.getCurrentHp();
					double currentDamage = damageMap.get(attackingPlayableUid).getDamage().calculateTotalDamage();

					if (currentVictimHp - currentDamage <= 0)
					{
						fallingAsleepSet.add(victimPlayable);
					}
				}
			}

			// TODO need to only change one at a time
			// case where three pets attack and die at same time = infinite loop?
			boolean hack = true;
			// ^ This actually works, (non-sarcastic comment) extremely complicated logic here
			for(Playable fallingSleepPet : fallingAsleepSet)
			{
				int sleepyPetUid = fallingSleepPet.getPlayableUid();
				int attackingPetUid = getAttackingPetUid(sleepyPetUid, damageMap); // TODO restructure damage map

				double previousRandomDamageDifference = currentRound.getFight().getRandomDamageDifference(sleepyPetUid);
				double currentRandomDamageDealt = damageMap.get(sleepyPetUid).getDamage().getRandomDamage();
				double currentRandomDamageReceived = damageMap.get(attackingPetUid).getDamage().getRandomDamage();
				double currentRandomDamageDifference = currentRandomDamageDealt - currentRandomDamageReceived;
				double newRandomDamageDifference = previousRandomDamageDifference + currentRandomDamageDifference;

				// need to see if previous random damage difference + current = zero
				if(Math.abs(newRandomDamageDifference) > Constants.DOUBLE_THRESHHOLD && hack)
				{
					damageMap.get(sleepyPetUid).getDamage().setRandomDamage(currentRandomDamageDealt + (-1.0 * newRandomDamageDifference));
					damageBalanced = false;
					hack = false;
				}
			}
		}


	}

	private int getAttackingPetUid(int sleepyPetUid, Map<Integer, DamageInfo> damageMap)
	{
		int attackingPlayableUid = -1;

		for(Integer mapKey : damageMap.keySet())
		{
			DamageInfo di = damageMap.get(mapKey);
			if(di.getVictimPlayableUid() == sleepyPetUid)
			{
				attackingPlayableUid = di.getAttackingPlayableUid();
			}
		}

		return attackingPlayableUid;
	}

	public RngHolder getRngHolder()
	{
		return textFightControl.getRngHolder();
	}
	
	public Skills getPlayerSkill(int playerIndex)
	{
		return currentRound.getPlayerRoundDataList().get(playerIndex).getSkillData().getSkill();
	}
	
	public Skills getPredictedSkillEnum(int playerIndex)
	{
		Skills predictedSkill = null;
		
		SkillData skillData = currentRound.getPlayerRoundDataList().get(playerIndex).getSkillData();
		
		try
		{
			if(skillData instanceof ShootTheMoonData)
			{
				ShootTheMoonData stmData = (ShootTheMoonData)skillData;
				predictedSkill = stmData.getPredictedSkillEnum();
			}
		}
		catch(Exception e)
		{
			// TODO make custom exception for failed cast
			e.printStackTrace();
		}
		
		return predictedSkill;
	}
	
	private int findVictimPlayerIndex(int attackingPlayerIndex)
	{
		int victimPlayerIndex = -1;
		boolean found = false;
		List<Playable> playableList = currentRound.getFight().getPlayableList();
		
		int searchIndex = attackingPlayerIndex + 1;
		searchIndex = searchIndex % playableList.size();
		
		while(searchIndex != attackingPlayerIndex && !found)
		{
			Playable player = playableList.get(searchIndex);
			
			if(player.isAwake())
			{
				victimPlayerIndex = searchIndex;
				found = true;
			}
			
			searchIndex++;
			searchIndex = searchIndex % playableList.size();
		}
		
		return victimPlayerIndex;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PetTypes getPetType(int playerIndex)
	{
		return currentRound.getFight().getPlayableList().get(playerIndex).getPetType();
	}

	public double getCumulativeRandomDamageDifference(int playerIndex)
	{
		return currentRound.getFight().getRandomDamageDifference(playerIndex);
	}

	public double getPlayableHp(int playerIndex)
	{
		List<Playable> playableList = currentRound.getFight().getPlayableList();
		return playableList.get(playerIndex).getCurrentHp();
	}
	
	public double getPlayableStartingHp(int playerIndex)
	{
		List<Playable> playableList = currentRound.getFight().getPlayableList();
		return playableList.get(playerIndex).getStartingHp();
	}
	
	public boolean isSkillRecharging(int playerIndex, Skills skill)
	{
		List<Playable> playableList = currentRound.getFight().getPlayableList();
		Playable playable = playableList.get(playerIndex);
		int rechargeTime = playable.getSkillRechargeTime(skill);
		
		return rechargeTime>0;
	}
}
