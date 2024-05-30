package edu.dselent.unused;

import edu.dselent.skill.Skills;

import java.util.ArrayList;
import java.util.List;

public class OutputObject
{
    // for each player
    private List<Double> hpList;
    private List<Double> balanceList;
    private List<Skills> skillList;
    private List<Double> randomDamageList;
    private List<Double> conditionalDamageList;

    public OutputObject()
    {
        hpList = new ArrayList<>();
        balanceList = new ArrayList<>();
        skillList = new ArrayList<>();
        randomDamageList = new ArrayList<>();
        conditionalDamageList = new ArrayList<>();
    }

    public void addHp(double hp)
    {
        hpList.add(hp);
    }

    public void addBalance(double balance)
    {
        balanceList.add(balance);
    }

    public void addSkill(Skills skill)
    {
        skillList.add(skill);
    }

    public void addRandomDamage(Double randomDamage)
    {
        randomDamageList.add(randomDamage);
    }

    public void addConditionalDamage(Double conditionalDamage)
    {
        conditionalDamageList.add(conditionalDamage);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<hpList.size(); i++)
        {
            sb.append(hpList.get(i));
            sb.append(",");

            if(i < balanceList.size())
            {
                sb.append(balanceList.get(i));
                sb.append(",");
                sb.append(skillList.get(i));
                sb.append(",");
                sb.append(randomDamageList.get(i));
                sb.append(",");
                sb.append(conditionalDamageList.get(i));
            }

            if(i < hpList.size()-1)
            {
                sb.append(",");
            }

            if(hpList.get(i) > 20000)
            {
                System.exit(0);
            }
        }



        //sb.append("\n");

        return sb.toString();
    }
}
