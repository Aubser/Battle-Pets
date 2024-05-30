package edu.dselent.customexceptions;

import edu.dselent.skill.Skills;

public class InvalidSkillChoiceException extends RuntimeException
{
    private final int playableUid;
    private final String petName;
    private final Skills skillChoice;

    public InvalidSkillChoiceException(String message, Throwable cause, int playableUid, String petName, Skills skillChoice)
    {
        super(message, cause);
        this.playableUid = playableUid;
        this.petName = petName;
        this.skillChoice = skillChoice;
    }

    public InvalidSkillChoiceException(String message, int playableUid, String petName, Skills skillChoice)
    {
        super(message);
        this.playableUid = playableUid;
        this.petName = petName;
        this.skillChoice = skillChoice;
    }

    public InvalidSkillChoiceException(Throwable cause, int playableUid, String petName, Skills skillChoice)
    {
        super(cause);
        this.playableUid = playableUid;
        this.petName = petName;
        this.skillChoice = skillChoice;
    }

    public InvalidSkillChoiceException(int playableUid, String petName, Skills skillChoice)
    {
        this.playableUid = playableUid;
        this.petName = petName;
        this.skillChoice = skillChoice;
    }

    public int getPlayableUid()
    {
        return playableUid;
    }

    public String getPetName()
    {
        return petName;
    }

    public Skills getSkillChoice()
    {
        return skillChoice;
    }

    @Override
    public String toString()
    {
        return "Skill is still recharging: InvalidSkillChoiceException{" +
                "playableUid=" + playableUid +
                ", petName='" + petName + '\'' +
                ", skillChoice=" + skillChoice +
                '}';
    }
}
