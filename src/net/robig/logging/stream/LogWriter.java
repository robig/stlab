package net.robig.logging.stream;

import java.io.IOException;
import java.io.Writer;

import net.robig.logging.Level;
import net.robig.logging.Logger;

public class LogWriter extends Writer {

	Logger log = null;
	Level level = null;
	
	public LogWriter(Logger log, Level lvl) {
		this.log=log;
		this.level = lvl;
	}
	
	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		log.log(new String(cbuf,off,len), level);
	}

}
