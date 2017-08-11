package de.mark615.xban.object;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Mute
{
	private int id;
	private UUID target;
	private UUID sender;
	private long muteStart;
	private long muteEnd;
	private String muteLocation;
	private String reason;
	
	public Mute(int id, Mute mute)
	{
		this.id = id;
		this.target = mute.getTarget();
		this.sender = mute.getSender();
		this.muteStart = mute.getMuteStart();
		this.muteEnd = mute.getMuteEnd();
		this.muteLocation = mute.getMuteLocation();
		this.reason = mute.getReason();
	}
	
	public Mute(UUID target, UUID sender, long muteTime, long muteEnd, String muteLocation, String reason)
	{
		this.target = target;
		this.sender = sender;
		this.muteStart = muteTime;
		this.muteEnd = muteEnd;
		this.muteLocation = muteLocation;
		this.reason = reason;
	}
	
	public Mute(int id, UUID target, UUID sender, long muteStart, long muteEnd, String muteLocation, String reason)
	{
		this(target, sender, muteStart, muteEnd, muteLocation, reason);
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public UUID getTarget()
	{
		return target;
	}

	public UUID getSender()
	{
		return sender;
	}

	public long getMuteStart()
	{
		return muteStart;
	}

	public long getMuteEnd()
	{
		return muteEnd;
	}

	public String getReason()
	{
		return reason;
	}

	public String getMuteLocation() {
		return muteLocation;
	}
	
	public String getRemainingMuteTime()
	{
		long seconds = muteEnd;
		seconds -= System.currentTimeMillis()/1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hour = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		return day + "d " + hour + "h " + minute + "m " + second + "s";
	}
}
