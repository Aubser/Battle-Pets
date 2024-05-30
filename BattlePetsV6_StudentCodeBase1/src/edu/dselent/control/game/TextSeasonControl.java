package edu.dselent.control.game;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.domain.*;
import edu.dselent.io.IoManager;
import edu.dselent.player.Playable;
import edu.dselent.settings.BattleSettings;
import edu.dselent.settings.SeasonSettings;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TextSeasonControl
{
    private IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
    private SeasonSettings seasonSettings;

	public TextSeasonControl(List<Playable> playableList, SeasonSettings seasonSettings)
	{
		this.seasonSettings = seasonSettings;
	}

	public void runSeason(Season season)
	{
		List<Playable> playerList = season.getPlayableList();
        SeasonStats seasonStats = new SeasonStats(playerList);

		ioManager.getOutputSender().outputString("Number of season rounds = " + season.getNumberOfSeasonRounds());
		ioManager.getOutputSender().outputString("\n");

		int battleNumber = 1;
        for (Battle currentBattle : season)
        {
            TextBattleControl textBattleControl = new TextBattleControl(currentBattle.getPlayableList(), seasonSettings.getBattleSettings());

            ioManager.getOutputSender().outputString("Battle " + battleNumber + " Started");
            textBattleControl.runBattle(currentBattle);

            List<Playable> winnerList = currentBattle.calculateWinners();

            outputBattleWinners(winnerList, battleNumber);
            outputBattleStats(currentBattle);

            // TODO decide on better output for seasons stats after each battle
            // total stats -> for each player output battle wins, losses, and fight wins, losses
                // sort by battle then by fight
            // Team	Battle Wins	Battle Losses	Fight Wins	Fight Losses


            for(Playable playable : playerList)
            {
                int playableUid = playable.getPlayableUid();

                if(winnerList.contains(playable))
                {
                    seasonStats.addBattleWins(playableUid, 1);

                    int fightWins = currentBattle.calculateFightWins(playableUid);
                    int fightLosses = currentBattle.calculateFightLosses(playableUid);

                    seasonStats.addFightWins(playableUid, fightWins);
                    seasonStats.addFightLosses(playableUid, fightLosses);
                }
                else if(currentBattle.getPlayableList().contains(playable))
                {
                    seasonStats.addBattleLosses(playableUid, 1);

                    int fightWins = currentBattle.calculateFightWins(playableUid);
                    int fightLosses = currentBattle.calculateFightLosses(playableUid);

                    seasonStats.addFightWins(playableUid, fightWins);
                    seasonStats.addFightLosses(playableUid, fightLosses);
                }

            }

            ioManager.getOutputSender().outputString("Season Stats ");

            List<SeasonStats.SeasonPlayerStats> statList = seasonStats.getSortedList();

            // TODO
            // No good way to format the headers with SeasonPlayerStats.toString()?
            ioManager.getOutputSender().outputString("Pet Name,Battle Wins,Battle Losses,Fight Wins,Fight Losses");

            for(SeasonStats.SeasonPlayerStats playerStats : statList)
            {
                ioManager.getOutputSender().outputString(playerStats.toString());
            }

            ioManager.getOutputSender().outputString("\n");

            battleNumber++;

            // TODO remove
            Scanner s = new Scanner(System.in);
            s.nextLine();
        }

        season.setEnded(true);

	}

	// TODO similar to outputFightWinners
    private void outputBattleWinners(List<Playable> winnerList, int battleNumber)
    {
        ioManager.getOutputSender().outputString("Battle " + (battleNumber) + " Over");
        ioManager.getOutputSender().outputString("Battle Winner(s)");

        for(Playable playable : winnerList)
        {
            ioManager.getOutputSender().outputString(playable.getPetName());
        }

        ioManager.getOutputSender().outputString("\n");
    }

    private void outputBattleStats(Battle battle)
    {
        for(Playable playable : battle.getPlayableList())
        {
            int fightWins = battle.calculateFightWins(playable.getPlayableUid());
            int fightLosses = battle.calculateFightLosses(playable.getPlayableUid());

            ioManager.getOutputSender().outputString(playable.getPetName());
            ioManager.getOutputSender().outputString("Fight Wins: " + fightWins);
            ioManager.getOutputSender().outputString("Fight Losses: " + fightLosses);

            ioManager.getOutputSender().outputString("\n");
        }

        ioManager.getOutputSender().outputString("\n");
    }
}
