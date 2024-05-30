package edu.dselent.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FightStartEvent extends BaseEvent
{
	private final List<PlayerEventInfo> playerEventInfoList;
	
	public FightStartEvent(List<PlayerEventInfo> playerEventInfoList)
	{
		super(EventTypes.FIGHT_START);
		this.playerEventInfoList = playerEventInfoList;
	}

	public FightStartEvent(FightStartEvent otherEvent)
	{
		super(EventTypes.FIGHT_START);
		this.playerEventInfoList = otherEvent.getPlayerEventInfoList();
	}

	// Returns a deep copy so pets cannot modify information and affect each others information
	public List<PlayerEventInfo> getPlayerEventInfoList()
	{
		List<PlayerEventInfo> playerEventInfoListCopy = new ArrayList<>();
		playerEventInfoList.forEach(playerEventInfo -> playerEventInfoListCopy.add(new PlayerEventInfo(playerEventInfo)));
		return playerEventInfoListCopy;
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
		FightStartEvent that = (FightStartEvent) o;
		return Objects.equals(playerEventInfoList, that.playerEventInfoList);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), playerEventInfoList);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("FightStartEvent{");
		sb.append(", playerEventInfoList=").append(playerEventInfoList);
		sb.append(", eventType=").append(getEventType());
		sb.append('}');
		return sb.toString();
	}
}
