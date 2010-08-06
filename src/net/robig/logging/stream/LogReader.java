package net.robig.logging.stream;

import java.io.IOException;
import java.io.Reader;

import net.robig.logging.Level;
import net.robig.logging.Logger;

public class LogReader extends Reader {

	Logger log = null;
	Level level = null;
	
	public LogReader(Logger log, Level lvl) {
		this.log=log;
		this.level = lvl;
	}
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		log.log(new String(cbuf,off,len), level);
		return 0;
	}


}
