package edu.dselent.battlepets;

import java.util.*;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.control.game.GameFactory;
import edu.dselent.control.game.TextBattleControl;
import edu.dselent.control.game.TextFightControl;
import edu.dselent.control.game.TextSeasonControl;
import edu.dselent.control.settings.BattleSettingsControl;
import edu.dselent.control.settings.FightSettingsControl;
import edu.dselent.control.settings.GameSettingsControl;
import edu.dselent.control.settings.SeasonSettingsControl;
import edu.dselent.domain.*;
import edu.dselent.io.TextInputGetter;
import edu.dselent.io.TextOutputSender;
import edu.dselent.player.Playable;
import edu.dselent.player.PlayableInstantiator;
import edu.dselent.io.Inputtable;
import edu.dselent.io.IoManager;
import edu.dselent.io.Outputtable;
import edu.dselent.settings.BattleSettings;
import edu.dselent.settings.FightSettings;
import edu.dselent.settings.GameModes;
import edu.dselent.settings.SeasonSettings;


public class TextGameRunner implements GameRunner
{
	// TODO move win calculation in BattleControl to use Battle -> use this in TextGameRunner
	// Should Round and Round event contain the index?
	// Create Battle Start event?
	// Adapter pattern for last assignment?

	@Override
	public void runGame()
	{
		Inputtable textInputtable = new TextInputGetter();
		Outputtable textOutputtable = new TextOutputSender();
		IoManager textIoManager = new IoManager(textInputtable, textOutputtable);

		ApplicationConfigurations.INSTANCE.setIoManager(textIoManager);

		GameSettingsControl gameSettingsControl = new GameSettingsControl();

		// retrieve game mode
		GameModes selectedGameMode = gameSettingsControl.retrieveGameSettings().getGameMode();

		if(selectedGameMode == GameModes.FIGHT)
		{
			runFight();
		}
		else if(selectedGameMode == GameModes.BATTLE)
		{
			runBattle();
		}
		else if(selectedGameMode == GameModes.TOURNAMENT)
		{
			runTournament();
		}
		else if(selectedGameMode == GameModes.SEASON)
		{
			runSeason();
		}
		else
		{
			throw new UnsupportedOperationException("Game Mode " + selectedGameMode + " is not supported");
		}
	}

	private void runFight()
	{
		FightSettingsControl fightSettingsControl = new FightSettingsControl();
		FightSettings fightSettings = fightSettingsControl.retrieveFightSettings();

		List<Playable> playableList = PlayableInstantiator.instantiatePlayables(fightSettings.getPlayerSettings());
		TextFightControl fightControl = new TextFightControl(playableList, fightSettings);
		Fight fight = new Fight(playableList);

		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		ioManager.getOutputSender().outputString("Fight Started");

		fightControl.runFight(fight);

		outputWinners(fight);
	}

	private void runBattle()
	{
		BattleSettingsControl battleSettingsControl = new BattleSettingsControl();
		BattleSettings battleSettings = battleSettingsControl.retrieveBattleSettings();

		List<Playable> playableList = PlayableInstantiator.instantiatePlayables(battleSettings.getFightSettings().getPlayerSettings());
		TextBattleControl battleControl = new TextBattleControl(playableList, battleSettings);
		Battle battle = new Battle(battleSettings.getNumberOfFights(), playableList);

		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		ioManager.getOutputSender().outputString("Battle Started");

		battleControl.runBattle(battle);

		outputWinners(battle);
	}

	private void runTournament()
	{
		// TODO add tournament mode
	}

	private void runSeason()
	{
		SeasonSettingsControl seasonSettingsControl = new SeasonSettingsControl();
		SeasonSettings seasonSettings = seasonSettingsControl.retrieveSeasonSettings();

		List<Playable> playableList = PlayableInstantiator.instantiatePlayables(seasonSettings.getBattleSettings().getFightSettings().getPlayerSettings());
		TextSeasonControl seasonControl = new TextSeasonControl(playableList, seasonSettings);
		Season season = GameFactory.createSeason(playableList, seasonSettings);

		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();
		ioManager.getOutputSender().outputString("Season Started");

		// TODO HACK
		SeasonStats seasonStats = new SeasonStats(playableList);
		Map<Integer, String> uidToNameMap = new HashMap<>();
		Map<String, Integer> nameToUidMap = new HashMap<>();

		for(Playable playable : playableList)
		{
			int uid = playable.getPlayableUid();
			String name = playable.getPetName();
			uidToNameMap.put(uid, name);
			nameToUidMap.put(name, uid);
		}

		Set<Battle> battleSet = season.getBattleSet();
		int battleNumber = 1;
		for(Battle battle : battleSet)
		{
			List<Playable> playerList = battle.getPlayableList();
			// reset all uids
			for(int i=0; i<playerList.size(); i++)
			{
				Playable playable = playerList.get(i);
				playable.setPlayableUid(i);
			}

			TextBattleControl battleControl = new TextBattleControl(playerList, seasonSettings.getBattleSettings());
			ioManager.getOutputSender().outputString("Battle " + (battleNumber++) + " Started");

			battleControl.runBattle(battle);
			List<Playable> winnerList = battle.calculateWinners();

			for(Playable playable : playableList)
			{
				String petName = playable.getPetName();

				if(winnerList.contains(playable))
				{
					seasonStats.addBattleWins(nameToUidMap.get(petName), 1);

					int fightWins = battle.calculateFightWins(playable.getPlayableUid());
					int fightLosses = battle.calculateFightLosses(playable.getPlayableUid());

					seasonStats.addFightWins(nameToUidMap.get(petName), fightWins);
					seasonStats.addFightLosses(nameToUidMap.get(petName), fightLosses);
				}
				else if(playerList.contains(playable))
				{
					seasonStats.addBattleLosses(nameToUidMap.get(petName), 1);

					int fightWins = battle.calculateFightWins(playable.getPlayableUid());
					int fightLosses = battle.calculateFightLosses(playable.getPlayableUid());

					seasonStats.addFightWins(nameToUidMap.get(petName), fightWins);
					seasonStats.addFightLosses(nameToUidMap.get(petName), fightLosses);
				}


			}

			outputWinners(battle);
			outputBattleStats(battle);

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

			// Uncomment for current season
			// Comment for legends season


			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{

			}


			// TODO remove
			//Scanner s = new Scanner(System.in);
			//s.nextLine();
		}
		//////////////////////////////

		//seasonControl.runSeason(season);

		//outputWinners(season);
	}

	// TODO
	// WITH HACK
	private void outputBattleStats(Battle battle)
	{
		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();

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

	private void outputWinners(Winnable winnable)
	{
		List<Playable> winnerList = winnable.calculateWinners();

		IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();

		ioManager.getOutputSender().outputString(winnable.getClass().getSimpleName() + " Over");
		ioManager.getOutputSender().outputString(winnable.getClass().getSimpleName() + " Winner(s)");

		for(Playable playable : winnerList)
		{
			ioManager.getOutputSender().outputString(playable.getPetName());
		}

		ioManager.getOutputSender().outputString("\n");
	}
}
