package edu.dselent.control.settings;

import edu.dselent.player.PetTypes;
import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.ComputerPlayerSettings;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;
import edu.dselent.utils.StringConstants;

import java.util.Set;

public class ComputerPlayerSettingsRetrievable extends DefaultPlayerSettingsRetrievable
{
    @Override
    public PlayerSettings retrievePlayerSettings(int playerIndex, PlayerTypes playerType)
    {
        PetTypes petType = retrievePetType(playerIndex);
        String playerName = retrievePlayerName(playerIndex);
        String petName = retrievePetName(playerName);
        double startingHp = retrieveStartingHp(petName);
        long randomSeed = retrieveRandomSeed(petName);
        Set<Skills> skillSet = getDefaultSkillSet();

        ComputerPlayerSettings.ComputerPlayerSettingsBuilder computerPlayerSettingsBuilder = new ComputerPlayerSettings.ComputerPlayerSettingsBuilder();
        ComputerPlayerSettings playerInfo = computerPlayerSettingsBuilder.withPlayerType(playerType)
                .withPetType(petType)
                .withPlayerName(playerName)
                .withPetName(petName)
                .withStartingHp(startingHp)
                .withRandomSeed(randomSeed)
                .withSkillSet(skillSet)
                .build();

        return playerInfo;
    }

    long retrieveRandomSeed(String petName)
    {
        long randomSeed = -1;
        boolean validSeed = false;

        while(!validSeed)
        {
            String outputMessage1 = StringConstants.getFormattedString(StringConstants.StringKeys.ENTER_COMPUTER_RANDOM_SEED_KEY, petName);
            output.outputString(outputMessage1);

            String randomSeedString = input.getString();

            try
            {
                randomSeed = Long.parseLong(randomSeedString);
                validSeed = true;
            }
            catch(Exception e)
            {
                String errorMessage = StringConstants.getFormattedString(StringConstants.StringKeys.INVALID_COMPUTER_RANDOM_SEED_KEY, randomSeedString);
                output.outputString(errorMessage);
            }
        }

        return randomSeed;
    }
}
