package de.mark615.xban.object;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Ban
{
	private int id;
	private UUID target;
	private UUID sender;
	private long banStart;
	private long banEnd;
	private String banLocation;
	private String reason;
	
	public Ban(int id, Ban ban)
	{
		this.id = id;
		this.target = ban.getTarget();
		this.sender = ban.getSender();
		this.banStart = ban.getBanStart();
		this.banEnd = ban.getBanEnd();
		this.banLocation = ban.getBanLocation();
		this.reason = ban.getReason();
	}
	
	public Ban(UUID target, UUID sender, long banTime, long banEnd, String banLocation, String reason)
	{
		this.target = target;
		this.sender = sender;
		this.banStart = banTime;
		this.banEnd = banEnd;
		this.banLocation = banLocation;
		this.reason = reason;
	}
	
	public Ban(int id, UUID target, UUID sender, long banStart, long banEnd, String banLocation, String reason)
	{
		this(target, sender, banStart, banEnd, banLocation, reason);
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

	public long getBanStart()
	{
		return banStart;
	}

	public long getBanEnd()
	{
		return banEnd;
	}

	public String getReason()
	{
		return reason;
	}

	public String getBanLocation() {
		return banLocation;
	}
	
	public String getRemainingBanTime()
	{
		long seconds = banEnd;
		seconds -= System.currentTimeMillis()/1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hour = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		return day + "d " + hour + "h " + minute + "m " + second + "s";
	}
	
}
