package net.robig.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>Simple pattern based logging format</h1>
 * 
 * <h2>supports optional options in Logger properties:</h2>
 * <ul>
 * <li> log_date_format - the format of the date/time in {@link SimpleDateFormat} syntax </li>
 * <li> log_format - pattern for log messages, that supports the following data:
 *    <ul>
 *       <li> %D - date/time if format of log_date_format property </li>
 *       <li> %L - log level </li>
 *       <li> %T - thread id </li>
 *       <li> %M - log message </li>
 *       <li> %C - class name of logger </li>
 *    </ul>
 * </li>
 * </ul>
 * 
 * @author robegroe
 *
 */

public class PatternLogFormat implements ILogFormat {
	
	public String formatPattern = "%D %L [%c]: %M";
	public String dateFormatPattern = "dd.MM.yyyy HH:mm:ss.SSS";
	
	public void init() {
		String logFormat=Logger.getProperty("log_format");
		if(logFormat!=null) formatPattern=logFormat;
		String logDateFormat=Logger.getProperty("log_date_format");
		if(logDateFormat!=null) dateFormatPattern=logDateFormat;
	}

	private String formatDate(long millis) {
		return new SimpleDateFormat(dateFormatPattern).format(new Date(millis));
	}

	private String getTemplate(){
		return formatPattern;
	}
	
	private String replace(LogEntry log){
		long threadId=Thread.currentThread().getId();
		long now=System.currentTimeMillis();
		
		String message  = getTemplate();
		String txt = log.message;
		if(log.objects!=null) 
			for(int i=0;i<log.objects.length;i++){
				txt=txt.replaceAll("\\{"+(i+1)+"\\}", log.objects[i].toString());
				txt=txt.replaceAll("\\\\"+(i+1), log.objects[i].toString());
				//message=message.replace("\\"+(i+1), objects[i].toString());
				txt=txt.replaceAll("%"+(i+1), log.objects[i].toString());
			}
		
		message=message.replace("%T", ""+threadId);
		message=message.replace("%L", log.level.name());
		message=message.replace("%C", log.className);
		message=message.replace("%c", log.classOnly);
		message=message.replace("%M", txt);
		message=message.replace("%D", formatDate(now));
		message=message.replace("%%", "%");
		
		return message;
	}
	
	@Override
	public String format(LogEntry log) {
		return replace(log);
	}

}
