package edu.dselent.event;


public class RoundStartEvent extends BaseEvent
{
	private final int roundIndex;
	
	public RoundStartEvent(int roundIndex)
	{
		super(EventTypes.ROUND_START);
		this.roundIndex = roundIndex;
	}

	public RoundStartEvent(RoundStartEvent otherEvent)
	{
		super(EventTypes.ROUND_START);
		this.roundIndex = otherEvent.roundIndex;
	}
	
	public int getRoundIndex()
	{
		return roundIndex;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + roundIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof RoundStartEvent))
			return false;
		RoundStartEvent other = (RoundStartEvent) obj;
		if (roundIndex != other.roundIndex)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("RoundStartEvent{");
		sb.append("roundNumber=").append(roundIndex);
		sb.append(", eventType=").append(getEventType());
		sb.append('}');
		return sb.toString();
	}
}
