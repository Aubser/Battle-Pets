package edu.dselent.control.settings;

import edu.dselent.player.PlayerTypes;
import edu.dselent.settings.PlayerSettings;

@FunctionalInterface
public interface PlayerSettingsRetrievable
{
    edu.dselent.settings.PlayerSettings retrievePlayerSettings(int playerIndex, PlayerTypes playerType);
}
