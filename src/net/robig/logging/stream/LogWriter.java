package net.robig.logging.stream;

import java.io.IOException;
import java.io.Writer;

import net.robig.logging.Level;
import net.robig.logging.Logger;

/**
 * Logwriter for outputting to a stream
 * @author robegroe
 *
 */
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
		int rlen=len;
		//Skip lines with only \r ņ and space:
		for(int i=off+len-1;i>=off;i--){
			if(cbuf[i]!=0x0a && cbuf[i]!=0x0d && cbuf[i]!=0x20){
				//cbuf[i]==0x0a
				rlen=i+1;
				break;
			}
			return;
		}
		log.log(new String(cbuf,off,rlen), level);
	}

}
