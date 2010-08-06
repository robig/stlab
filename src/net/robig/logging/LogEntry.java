package net.robig.logging;
/**
 * Data transfer object for a log message with corresponding data
 * @author robegroe
 *
 */
public class LogEntry {
	public String message;
	public Object[] objects;
	public Level level;
	public String className;
	public String classOnly;
}
