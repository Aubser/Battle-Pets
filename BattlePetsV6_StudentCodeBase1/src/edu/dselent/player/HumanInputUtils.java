package edu.dselent.player;

import edu.dselent.skill.Skills;
import edu.dselent.utils.Utils;

public class HumanInputUtils
{
    public static String generateSkillChoiceString(Skills[] skillValues)
    {
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<skillValues.length; i++)
        {
            sb.append(i + 1).append(": ");
            sb.append(Utils.convertEnumString(skillValues[i].name()));

            if(i < skillValues.length -1)
            {
                sb.append(" | ");
            }
        }

        return sb.toString();
    }
}
