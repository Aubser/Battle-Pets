package edu.dselent.settings;
import java.util.HashSet;
import java.util.Set;

import edu.dselent.player.PetTypes;
import edu.dselent.player.PlayerTypes;
import edu.dselent.skill.Skills;

// Exists prior to battle
// Grouped player and pet settings together for now... can separate out if needed
public class PlayerSettings
{
	private PlayerTypes playerType;
	private PetTypes petType;
	private Double startingHp;
	private String playerName;
	private String petName;
	private Set<Skills> skillSet;
	
	public PlayerSettings(PlayerSettingsBuilder builder)
	{
		// TODO verify state and throw illegal state exception if not valid values
		
		playerType = builder.playerType;
		petType = builder.petType;
		startingHp = builder.startingHp;
		playerName = builder.playerName;
		petName = builder.petName;
		skillSet = new HashSet<>(builder.skillSet);
	}
		
	public PlayerTypes getPlayerType()
	{
		return playerType;
	}

	public PetTypes getPetType()
	{
		return petType;
	}

	public Double getStartingHp()
	{
		return startingHp;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public String getPetName()
	{
		return petName;
	}
	
	public Set<Skills> getSkillSet()
	{
		return skillSet;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((petName == null) ? 0 : petName.hashCode());
		result = prime * result + ((petType == null) ? 0 : petType.hashCode());
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((playerType == null) ? 0 : playerType.hashCode());
		result = prime * result + ((skillSet == null) ? 0 : skillSet.hashCode());
		result = prime * result + ((startingHp == null) ? 0 : startingHp.hashCode());
		
		return result;
	}

	// TODO format
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PlayerSettings))
			return false;
		PlayerSettings other = (PlayerSettings) obj;
		if (petName == null) {
			if (other.petName != null)
				return false;
		} else if (!petName.equals(other.petName))
			return false;
		if (petType != other.petType)
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (playerType != other.playerType)
			return false;
		if (skillSet == null) {
			if (other.skillSet != null)
				return false;
		} else if (!skillSet.equals(other.skillSet))
			return false;
		if (startingHp == null) {
			if (other.startingHp != null)
				return false;
		} else if (!startingHp.equals(other.startingHp))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("PlayerSettings [playerType=");
		builder.append(playerType);
		builder.append(", petType=");
		builder.append(petType);
		builder.append(", startingHp=");
		builder.append(startingHp);
		builder.append(", playerName=");
		builder.append(playerName);
		builder.append(", petName=");
		builder.append(petName);
		builder.append(", skillSet=");
		builder.append(skillSet);
		builder.append("]");
		
		return builder.toString();
	}




	public static class PlayerSettingsBuilder
	{
		private PlayerTypes playerType;
		private PetTypes petType;
		private Double startingHp;
		private String playerName;
		private String petName;
		private Set<Skills> skillSet;

		public PlayerSettingsBuilder withPlayerType(PlayerTypes playerType)
		{
			this.playerType = playerType;
			return this;
		}
		
		public PlayerSettingsBuilder withPetType(PetTypes petType)
		{
			this.petType = petType;
			return this;
		}
		
		public PlayerSettingsBuilder withStartingHp(Double startingHp)
		{
			this.startingHp = startingHp;
			return this;
		}
		
		public PlayerSettingsBuilder withPlayerName(String playerName)
		{
			this.playerName = playerName;
			return this;
		}
		
		public PlayerSettingsBuilder withPetName(String petName)
		{
			this.petName = petName;
			return this;
		}
		
		public PlayerSettingsBuilder withSkillSet(Set<Skills> skillSet)
		{
			this.skillSet = skillSet;
			return this;
		}
		
		public PlayerSettings build()
		{
			return new PlayerSettings(this);
		}
	}
}
