package net.robig.logging;

public class SysOutLogAppender implements ILogAppender {
	@Override
	public void append(String message, LogEntry l) {
		if(l.level.equals(Level.ERROR))
			System.err.println(message);
		else
			System.out.println(message);
		
	}

	@Override
	public void init() throws Exception {
	}

}
