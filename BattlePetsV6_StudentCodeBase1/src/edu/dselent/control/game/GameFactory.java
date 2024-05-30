package edu.dselent.control.game;

import edu.dselent.domain.Battle;
import edu.dselent.domain.Season;
import edu.dselent.player.Playable;
import edu.dselent.settings.SeasonSettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameFactory
{
    // Some old thoughts

    // Think about factory for constructing Season, Battle, Fight
    // Change Battle constructor to take the array of fights instead of creating them, fight would take a round list
    // Entities would require settings in all constructors if no factory
    // Should not be tied to setting objects, only controls need some information
    // Factory objects can take the settings
    // Really only need this for season right now, can modify others later as needed


    public static Season createSeason(List<Playable> playableList, SeasonSettings seasonSettings)
    {
        Set<Battle> battleSet = new HashSet<>();

        // Generate all combination of 1v1 battles
        // assume 1v1 for now

        for(int i=0; i<playableList.size(); i++)
        {
            Playable playable1 = playableList.get(i);

            for(int j=i+1; j<playableList.size(); j++)
            {
                Playable playable2 = playableList.get(j);

                List<Playable> battlePlayableList = new ArrayList<>();
                battlePlayableList.add(playable1);
                battlePlayableList.add(playable2);

                battleSet.add(new Battle(seasonSettings.getBattleSettings().getNumberOfFights(), battlePlayableList));
            }
        }

        return new Season(playableList, battleSet);
    }
}
