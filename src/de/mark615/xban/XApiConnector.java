package de.mark615.xban;

import de.mark615.xapi.interfaces.XBanApi;

public class XApiConnector extends XBanApi
{
	private XBan plugin;
	//private PriorityConfigBase priority;
	
	public XApiConnector(de.mark615.xapi.XApi xapi, XBan plugin)
	{
		super(xapi);
		//this.priority = xapi.getPriorityConfig();
		this.plugin = plugin;
	}
	
}
