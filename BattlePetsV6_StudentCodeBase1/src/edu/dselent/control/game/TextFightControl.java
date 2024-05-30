package edu.dselent.control.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.dselent.config.ApplicationConfigurations;
import edu.dselent.domain.Fight;
import edu.dselent.domain.RngHolder;
import edu.dselent.domain.Round;
import edu.dselent.event.FightStartEvent;
import edu.dselent.event.PlayerEventInfo;
import edu.dselent.event.PlayerEventInfo.PlayerEventInfoBuilder;
import edu.dselent.io.IoManager;
import edu.dselent.player.Playable;
import edu.dselent.settings.FightSettings;

// TODO
// When running season mode or battle royale, comment out all output lines in this file
public class TextFightControl
{
	private TextRoundControl textRoundControl;
	private SkillValidator theValidator;
	private RngHolder rngHolder;
	private EventBus eventBus;

	// Constructor when there does not already exist an event bus to be used (e.g. mode = individual fight)
	public TextFightControl(List<Playable> playableList, FightSettings fightSettings)
	{
		rngHolder = new RngHolder(fightSettings.getRandomSeed());
		theValidator = new SkillValidator(playableList);
		textRoundControl = new TextRoundControl(this);

		eventBus = new EventBus();
		playableList.forEach(playable -> eventBus.register(playable));
	}

	// Constructor when there does already exist an event bus to be used (e.g. mode > individual fight)
	public TextFightControl(List<Playable> playableList, RngHolder rngHolder, EventBus eventBus)
	{
		this.rngHolder = rngHolder;
		theValidator = new SkillValidator(playableList);
		textRoundControl = new TextRoundControl(this);
		this.eventBus = eventBus;
	}

	EventBus getEventBus()
	{
		return eventBus;
	}

	SkillValidator getTheValidator()
	{
		return theValidator;
	}

	public void runFight(Fight fight)
	{
		theValidator.reset();
		List<Playable> playerList = fight.getPlayableList();

		/*//////*/IoManager ioManager = ApplicationConfigurations.INSTANCE.getIoManager();

		/*//////*/ioManager.getOutputSender().outputString("Players");
		/*//////*/ioManager.getOutputSender().outputString("\n");
				
		for(int i=0; i<playerList.size(); i++)
		{
			Playable currentPlayer = playerList.get(i);
			/*//////*/ioManager.getOutputSender().outputString("Player " + (i+1));
			/*//////*/ioManager.getOutputSender().outputString("Player Name: " + currentPlayer.getPlayerName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Name: " + currentPlayer.getPetName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Type: " + currentPlayer.getPetType());
			/*//////*/ioManager.getOutputSender().outputString("Starting HP: " + currentPlayer.getStartingHp());
			/*//////*/ioManager.getOutputSender().outputString("\n");
			
			// TODO output skill set
		}

		List<PlayerEventInfo> playerEventInfoList = new ArrayList<>();
		
		for(Playable currentPlayable : playerList)
		{
			PlayerEventInfoBuilder peib = new PlayerEventInfoBuilder();

			peib.withPlayableUid(currentPlayable.getPlayableUid());
			peib.withPetName(currentPlayable.getPetName());
			peib.withPetType(currentPlayable.getPetType());
			peib.withPlayerType(currentPlayable.getPlayerType());
			peib.withSkillSet(currentPlayable.getSkillSet());
			peib.withStartingHp(currentPlayable.getCurrentHp());
			
			playerEventInfoList.add(peib.build());
		}

		FightStartEvent fightStartEvent = new FightStartEvent(playerEventInfoList);
		eventBus.fireEvent(fightStartEvent);
		
		int roundIndex = 0;
		while(!isFightOver(fight))
		{
			Round round = new Round(fight, roundIndex);
			fight.getRoundList().add(round);
			textRoundControl.runRound(round);
			roundIndex++;
		}

		fight.setEnded(true);

		/*//////*/ioManager.getOutputSender().outputString("Pets");
		/*//////*/ioManager.getOutputSender().outputString("\n");
		
		for(int i=0; i<playerList.size(); i++)
		{
			Playable currentPlayer = playerList.get(i);
			/*//////*/ioManager.getOutputSender().outputString("Pet " + (i+1));
			/*//////*/ioManager.getOutputSender().outputString("Pet Name: " + currentPlayer.getPetName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Type: " + currentPlayer.getPetType());
			/*//////*/ioManager.getOutputSender().outputString("Current HP: " + currentPlayer.getCurrentHp());
			/*//////*/ioManager.getOutputSender().outputString("\n");
			
			// TODO output skill set
		}


		/*//////*/ioManager.getOutputSender().outputString("Sorted by HP");
		/*//////*/ioManager.getOutputSender().outputString("\n");
		
		List<Playable> sortedPlayableList = new ArrayList<>(playerList);
		sortedPlayableList.sort((p1, p2) -> Double.compare(p2.getCurrentHp(), p1.getCurrentHp()));

		for(int i=0; i<sortedPlayableList.size(); i++)
		{
			Playable currentPlayer = sortedPlayableList.get(i);
			/*//////*/ioManager.getOutputSender().outputString("Pet " + (i+1));
			/*//////*/ioManager.getOutputSender().outputString("Pet Name: " + currentPlayer.getPetName());
			/*//////*/ioManager.getOutputSender().outputString("Pet Type: " + currentPlayer.getPetType());
			/*//////*/ioManager.getOutputSender().outputString("Current HP: " + currentPlayer.getCurrentHp());
			/*//////*/ioManager.getOutputSender().outputString("\n");

			// TODO output skill set
		}

	}
	

	RngHolder getRngHolder()
	{
		return rngHolder;
	}
	
	private boolean isFightOver(Fight fight)
	{
		int awakeCount = 0;
		List<Playable> playableList = fight.getPlayableList();

		for (Playable player : playableList)
		{
			if (player.isAwake())
			{
				awakeCount++;
			}
		}
		
		return awakeCount<=1;
	}
	

}
