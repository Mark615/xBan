package de.mark615.xban.object;

import java.util.UUID;

public class Ban
{
	private int id;
	private UUID target;
	private UUID sender;
	private long banTime;
	private long banEnd;
	private String reason;
	
	public Ban(int id, Ban ban)
	{
		this.id = id;
		this.target = ban.getTarget();
		this.sender = ban.getSender();
		this.banTime = ban.getBanTime();
		this.banEnd = ban.getBanEnd();
		this.reason = ban.getReason();
	}
	
	public Ban(UUID target, UUID sender, long banTime, long banEnd, String reason)
	{
		this.target = target;
		this.sender = sender;
		this.banTime = banTime;
		this.banEnd = banEnd;
		this.reason = reason;
	}
	
	public Ban(int id, UUID target, UUID sender, long banTime, long banEnd, String reason)
	{
		this(target, sender, banTime, banEnd, reason);
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

	public long getBanTime()
	{
		return banTime;
	}

	public long getBanEnd()
	{
		return banEnd;
	}

	public String getReason()
	{
		return reason;
	}
	
}
