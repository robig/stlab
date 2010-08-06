package net.robig.logging;

import java.io.File;
import java.io.FileWriter;

/**
 * Simple file log appender
 * appends to a file
 * 
 * @author robegroe
 *
 */

public class SimpleFileLogAppender implements ILogAppender {

	private FileWriter writer=null;
	private String logFile = "file.log";
	Logger log = Logger.getLogger(this.getClass());
	private boolean enabled=true;
	
	private FileWriter getWriter() throws Exception{
		if(writer==null)
			writer = new FileWriter(new File(logFile));
		return writer;
	}
	
	@Override
	public void append(String message, LogEntry l) {
		if(!enabled) return;
		try {
			getWriter();
			writer.write(message);
			writer.write("\n");writer.flush();
		} catch (Exception e) {
			enabled=false; //disable on error
			log.error("Cannot log to logfile \\1: \\2",logFile,e);
		}
	}

	@Override
	public void init() throws Exception {
		if(Logger.getProperty("log_file")!=null)
			logFile=Logger.getProperty("log_file");
		enabled=true;
	}

	@Override
	protected void finalize() throws Throwable {
		if(writer!=null) writer.close();
		super.finalize();
	}
}
