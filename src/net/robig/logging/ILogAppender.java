package net.robig.logging;

public interface ILogAppender {
	public void init() throws Exception;
	public void append(String formatedMessage,LogEntry log);
}
