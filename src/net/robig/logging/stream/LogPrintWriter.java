package net.robig.logging.stream;

import java.io.PrintWriter;

/**
 * makes it possible to be used as output for Stacktaces
 * @author robegroe
 *
 */
public class LogPrintWriter extends PrintWriter {

	public LogPrintWriter(LogWriter out) {
		super(out);
	}
	

}
