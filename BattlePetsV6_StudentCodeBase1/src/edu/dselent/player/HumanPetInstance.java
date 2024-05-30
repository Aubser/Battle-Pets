package edu.dselent.player;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.io.Inputtable;
import edu.dselent.io.IoManager;
import edu.dselent.io.Outputtable;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;
import edu.dselent.skill.instances.ShootTheMoonInstance;
import edu.dselent.skill.instances.SkillInstance;
import edu.dselent.utils.StringConstants;
import edu.dselent.utils.Utils;

import java.util.Map;

public class HumanPetInstance extends PetInstance
{
	private Outputtable outputtable;
	private Inputtable inputtable;

	public HumanPetInstance(int playableUid, PlayerSettings playerSettings)
	{
		super(playableUid, playerSettings);

		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		outputtable = ioManager.getOutputSender();
		inputtable = ioManager.getInputGetter();
	}

	@Override
	public Skills chooseSkill()
	{
		Skills skillChoice = null;
		boolean validSkill = false;
		Map<Skills, SkillInstance> skillInstanceMap = getSkillInstanceMap();

		while (!validSkill)
		{
			outputtable.outputString(StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_SKILL_CHOICE_KEY, getPetName(), HumanInputUtils.generateSkillChoiceString(Skills.values())));

			skillChoice = retrieveSkillChoice();

			// Have player verify their own skill for the user input
			// Game controlling classes will be much harsher if an invalid skill is chosen

			if(skillChoice != null)
			{
				if(skillInstanceMap.get(skillChoice).isRecharging())
				{
					String enumString = Utils.convertEnumString(skillChoice.toString());
					outputtable.outputString(StringConstants.getFormattedString(StringConstants.StringKeys.SKILL_CHOICE_RECHARGING_KEY, enumString));
				}
				else
				{
					validSkill = true;
				}
			}
		}

		if(skillChoice == Skills.SHOOT_THE_MOON)
		{
			chooseSkillPrediction();
		}

		return skillChoice;
	}

	private void chooseSkillPrediction()
	{
		boolean validSkillPrediction = false;
		Skills predictionSkillChoice = null;

		while (!validSkillPrediction)
		{
			outputtable.outputString(StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_SKILL_PREDICTION_CHOICE_KEY, getPetName(), HumanInputUtils.generateSkillChoiceString(Skills.values())));
			predictionSkillChoice = retrieveSkillChoice();

			if(predictionSkillChoice != null)
			{
				validSkillPrediction = true;
			}

		}

		ShootTheMoonInstance stmInstance = (ShootTheMoonInstance)getSkillInstanceMap().get(Skills.SHOOT_THE_MOON);
		stmInstance.setPredictedSkillEnum(predictionSkillChoice);
	}

	private Skills retrieveSkillChoice()
	{
		Skills skillChoice = null;
		String skillNumberString = inputtable.getString();

		try
		{
			int skillNumberInt = Integer.parseInt(skillNumberString);
			skillChoice = Skills.values()[skillNumberInt-1];
		}
		catch(Exception e)
		{
			outputtable.outputString(StringConstants.getFormattedString(StringConstants.StringKeys.INVALID_SKILL_CHOICE_KEY, skillNumberString));
		}

		return skillChoice;
	}

	@Override
	public void update(Object event)
	{
		// TODO Auto-generated method stub
		
	}
	
}
