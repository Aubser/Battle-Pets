package edu.dselent.settings;

import edu.dselent.player.PetTypes;
import edu.dselent.player.PlayerTypes;
import edu.dselent.skill.Skills;

import java.util.Objects;
import java.util.Set;

public class ComputerPlayerSettings extends PlayerSettings
{
    private long randomSeed;

    public ComputerPlayerSettings(ComputerPlayerSettingsBuilder builder)
    {
        super(builder.playerSettingsBuilder);
        this.randomSeed = builder.randomSeed;
    }

    public long getRandomSeed()
    {
        return randomSeed;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        ComputerPlayerSettings that = (ComputerPlayerSettings) o;
        return getRandomSeed() == that.getRandomSeed();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getRandomSeed());
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ComputerPlayerInfo{");
        sb.append("randomSeed=").append(randomSeed);
        sb.append(", playerType=").append(getPlayerType());
        sb.append(", petType=").append(getPetType());
        sb.append(", startingHp=").append(getStartingHp());
        sb.append(", playerName='").append(getPlayerName()).append('\'');
        sb.append(", petName='").append(getPetName()).append('\'');
        sb.append(", skillSet=").append(getSkillSet());
        sb.append('}');
        return sb.toString();
    }


    // Builder pattern does not work that well with inheritance
        // Would need to override all methods to return "this" because I need instances of the subclass for subclass specific variables
        // But cannot do that because "this" is not the super class, it is only a subtype, which is a different method signature
    // Use composition instead
    public static class ComputerPlayerSettingsBuilder
    {
        private PlayerSettingsBuilder playerSettingsBuilder;
        private long randomSeed;

        public ComputerPlayerSettingsBuilder()
        {
            playerSettingsBuilder = new PlayerSettingsBuilder();
        }

        public ComputerPlayerSettingsBuilder withRandomSeed(long randomSeed)
        {
            this.randomSeed = randomSeed;
            return this;
        }

        public ComputerPlayerSettingsBuilder withPlayerType(PlayerTypes playerType)
        {
            playerSettingsBuilder.withPlayerType(playerType);
            return this;
        }

        public ComputerPlayerSettingsBuilder withPetType(PetTypes petType)
        {
            playerSettingsBuilder.withPetType(petType);
            return this;
        }

        public ComputerPlayerSettingsBuilder withStartingHp(Double startingHp)
        {
            playerSettingsBuilder.withStartingHp(startingHp);
            return this;
        }

        public ComputerPlayerSettingsBuilder withPlayerName(String playerName)
        {
            playerSettingsBuilder.withPlayerName(playerName);
            return this;
        }

        public ComputerPlayerSettingsBuilder withPetName(String petName)
        {
            playerSettingsBuilder.withPetName(petName);
            return this;
        }

        public ComputerPlayerSettingsBuilder withSkillSet(Set<Skills> skillSet)
        {
            playerSettingsBuilder.withSkillSet(skillSet);
            return this;
        }

        public ComputerPlayerSettings build()
        {
            return new ComputerPlayerSettings(this);
        }
    }
}
