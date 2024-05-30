package edu.dselent.control.game;

import java.util.List;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.domain.Battle;
import edu.dselent.domain.Fight;
import edu.dselent.domain.RngHolder;
import edu.dselent.io.IoManager;
import edu.dselent.player.Playable;
import edu.dselent.settings.BattleSettings;

public class TextBattleControl
{
	private TextFightControl textFightControl;
	private RngHolder rngHolder;
	private EventBus eventBus;
	private IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();

	public TextBattleControl(List<Playable> playableList, BattleSettings battleSettings)
	{
		rngHolder = new RngHolder(battleSettings.getFightSettings().getRandomSeed());
		eventBus = new EventBus();

		playableList.forEach(playable -> eventBus.register(playable));

		textFightControl = new TextFightControl(playableList, rngHolder, eventBus);
	}
	
	/**
	 * Playables do not have a reference to control classes therefore cannot obtain
	 * this rng by normal means.
	 * @return rngHolder
	 */
	RngHolder getRngHolder()
	{
		return rngHolder;
	}

	EventBus getEventBus()
	{
		return eventBus;
	}
	
	public void runBattle(Battle battle)
	{
		List<Playable> playerList = battle.getPlayableList();

		// TODO comment for season mode
		// TODO uncomment non-season mode

		/*//////*/ioManager.getOutputSender().outputString("Number of Fights = " + battle.getNumberOfFights());
		/*//////*/ioManager.getOutputSender().outputString("\n");
		
		int numberOfFights = battle.getNumberOfFights();
		
		int[] winCounts = new int[playerList.size()];
		
		for(int i=0; i<numberOfFights; i++)
		{
			Fight currentFight = battle.getFight(i);

			for(Playable currentPlayable : playerList)
			{
				currentPlayable.reset();
			}

			/*//////*/ioManager.getOutputSender().outputString("Fight " + (i+1) + " Started");
			/*//////*/ioManager.getOutputSender().outputString("\n");

			textFightControl.runFight(currentFight);

			List<Playable> winnerList = currentFight.calculateWinners();


			// TODO uncomment for Battle Royale

			/*//////*/outputFightWinners(winnerList, i+1);
			
			for(Playable winner : winnerList)
			{
				int winnerIndex = playerList.indexOf(winner);
				winCounts[winnerIndex]++;
			}
			
			/*//////*/ioManager.getOutputSender().outputString("Fight win Counts ");

			for(int j=0; j<playerList.size(); j++)
			{
				Playable player = playerList.get(j);
				/*//////*/ioManager.getOutputSender().outputString(player.getPetName() + ": " + winCounts[j]);
			}
			
			/*//////*/ioManager.getOutputSender().outputString("\n");
		}

		battle.setEnded(true);

	}

	private void outputFightWinners(List<Playable> winnerList, int fightNumber)
	{
		ioManager.getOutputSender().outputString("Fight " + (fightNumber) + " Over");
		ioManager.getOutputSender().outputString("Fight Winner(s)");

		for(Playable playable : winnerList)
		{
			ioManager.getOutputSender().outputString(playable.getPetName());
		}

		ioManager.getOutputSender().outputString("\n");
	}
}
