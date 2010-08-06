package net.robig.logging;

public interface ILogFormat {

	public void init();
	public String format(LogEntry log);	
	
}
