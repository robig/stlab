package net.robig.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.robig.logging.stream.LogPrintWriter;
import net.robig.logging.stream.LogWriter;

/**
 * A simple Logger that provides:
 * - different Log levels
 * - specify log levels of packages or classes
 * - 
 * 
 * @author robegroe
 *
 */

public class Logger {
	
	private Class<?> belongsTo = null;
	private String className = null;
	private String classOnly = null;
	private ILogFormat format = defaultLogFormat;
	
	private static Map<String,Level> logLevels = new HashMap<String,Level>();
	private static ILogFormat defaultLogFormat = new PatternLogFormat();
	private static Properties props = new Properties();
	private static List<ILogAppender> appenders = new ArrayList<ILogAppender>();
	private static boolean initialized = false;
	private static boolean initializing = false;
	private static Logger myLog = null;
	
	/**
	 * get a new Logger for given Class
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(Class<?> cls){
		return new Logger(cls);
	}
	
	/**
	 * get a new Logger for given Classname
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(String className){
		return new Logger(className);
	}
	
	/**
	 * Creates new Logger Object
	 * @param class
	 */
	public Logger(Class<?> cls) {
		initialize();
		belongsTo=cls;
		setClassName(findClassName(cls));
		format=defaultLogFormat;
	}
	
	private static String findClassName(Class<?> cls){
		String className=cls.getCanonicalName();
		if(className==null){
			className=findClassName(cls.getSuperclass());
		}
		return className;
	}

	/**
	 * Creates new Logger Object
	 * @param className
	 */
	public Logger(String className) {
		initialize();
		setClassName(className);
		format=defaultLogFormat;
	}
	
	private void setClassName(String qClassName){
		className=qClassName;
		String[] splittedName = qClassName.split("\\.");
		String clsOnly = splittedName[splittedName.length-1];
		classOnly=clsOnly;
	}

	/** 
	 * initialize Logger with default properties: logging.properties
	 * (will be done automatically on first Logger instance)
	 */
	public static void initialize() {
		initialize("logging.properties");
	}
	
	/**
	 * initialize Logger with specific properties file
	 * @param propertiesFile
	 */
	public static synchronized void initialize(String propertiesFile){
		if(initialized || initializing) return;
		initializing=true;
        try {
            props.load(new FileInputStream(propertiesFile));
        } catch(IOException ignored) {
            //TODO: ignore it ??
        }
        for(String key: props.stringPropertyNames()){
        	String value=props.getProperty(key);
            //add default LogFormat defined by properties file:
        	if(key.equals("default_log_format_class")){
        		try {
        			ILogFormat f=(ILogFormat) Class.forName(value).newInstance();
        			defaultLogFormat=f;
        		}catch(Exception e){
        			props.remove(key);
        			initError("Invalid value of property: "+key);
        		}
        	}else if(key.startsWith("appender_")) {
        		//TODO: add appenders defined by properties file
        		try {
        			ILogAppender a=(ILogAppender) Class.forName(value).newInstance();
        			addAppender(a);
        		}catch(Exception e){
        			
        		}
        	}else{
        		try {
        			Level l = Level.valueOf(value);
            		if(l!=null){
            			synchronized (logLevels) {
            				logLevels.put(key, l);	
    					}
            		}else{
            			//TODO: error logging
            		}
        		} catch(IllegalArgumentException e){
        		}
        	}
        }
		
		
        if(appenders.size()==0){
        	addAppender(new SysOutLogAppender());
        }
        if(logLevels.size()==0){
        	logLevels.put("*", Level.INFO);
        }
        defaultLogFormat.init();
        initialized=true;
        initializing=false;
        myLog = new Logger(Logger.class);
        
        synchronized (appenders) {
	        for(ILogAppender a: appenders){
	        	try {
	        		a.init();
	        	}catch(Exception ex){
	        		initError("Appender "+findClassName(a.getClass())+" could not be initialized! " + ex);
	        	}
	        }
		}
	}
	
	/**
	 * add a implementation of ILogAppender that includes another functionality to save/append logs
	 * @param app
	 */
	public static void addAppender(ILogAppender app){
		synchronized (appenders) {
			appenders.add(app);
		}
	}
	
	public static void removeAppender(ILogAppender app){
		synchronized (appenders) {
			appenders.remove(app);
		}
	}
	
	private static void initError(String message){
		String pre="ERROR in Logger initialization! ";
		if(myLog==null){
			System.err.println(pre+message);
			return;
		}
		myLog.error(pre+message);
	}
	
	/**
	 * get a Logging configuration property
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
		return props.getProperty(key);
	}

	private static Level findLevel(String className){
		String[] parts=className.split("\\.");
		if(parts.length==0) return null;
		Level ret=logLevels.get("*");
		String pkg="";
		for(int i=0;i<parts.length;i++){
			pkg+= (i==0? "": ".") + parts[i];
			Level l=logLevels.get(pkg);
			if(l!=null){
				ret=l;
			}
		}
		return ret;
	}
	
	/**
	 * set log format
	 * @param f
	 */
	public void setLogFormat(ILogFormat f){
		f.init();
		format=f;
	}
	
	/** gets current Loggers className */
	public String getClassName() {
		return className;
	}
	
	private boolean haveToLog(Level l){
		Level clsLevel=findLevel(getClassName());
		if(clsLevel != null && clsLevel.compareTo(l)>=0) return true;
		return false;
	}
	
	private void appendLog(String message, LogEntry log){
		if(!haveToLog(log.level)) return;
		synchronized (appenders) {
			for(ILogAppender app: appenders){
				app.append(message,log);
			}
		}
	}
	
	private void newLogEntry(String message,Level l, Object[] objetcs){
		if(!initialized) return; //TODO: Error handling
		LogEntry log=new LogEntry();
		log.message=message;
		log.level=l;
		log.objects=objetcs;
		log.className=className;
		log.classOnly=classOnly;
		appendLog(format.format(log),log);
	}
	
	/**
	 * Log a message with given Level
	 * @param message
	 * @param l
	 */
	public void log(String message, Level l){
		newLogEntry(message,l,new Object[]{});
	}
	
	/**
	 * Log a message with given Level
	 * and insert given objects into message replacing special chars like \1 or {1} 
	 * @param message
	 * @param l
	 */
	public void log(String message, Level l, Object... objects){
		newLogEntry(message,l,objects);
	}
	
	/**
	 * Log a message of level INFO
	 * @param message
	 */
	public void info(String message){
		log(message,Level.INFO);
	}
	
	/**
	 * Log a message of level ERROR
	 * @param message
	 */
	public void error(String message){
		log(message,Level.ERROR);
	}
	
	/**
	 * Log a message of level WARN
	 * @param message
	 */
	public void warn(String message){
		log(message,Level.WARN);
	}
	
	/**
	 * Log a message of level DEBUG
	 * @param message
	 */
	public void debug(String message){
		log(message,Level.DEBUG);
	}
	
	/**
	 * Log a message of level DEBUG
	 * @see debug()
	 * @param message
	 */
	public void log(String message){
		log(message,Level.DEBUG);
	}
	
	/**
	 * Log a message of level INFO
	 * replaces \1 and {1} with 1st parameter, and so on
	 * @param message
	 */
	public void info(String message, Object... objetcs){
		log(message,Level.INFO, objetcs);
	}
	
	/**
	 * Log a message of level ERROR
	 * replaces \1 and {1} with 1st parameter in message, and so on
	 * @param message
	 */
	public void error(String message, Object... objetcs){
		log(message,Level.ERROR, objetcs);
	}
	
	/**
	 * Log a message of level ERROR
	 * replaces \1 and {1} with 1st parameter in message, and so on
	 * @param message
	 */
	public void warn(String message, Object... objetcs){
		log(message,Level.WARN, objetcs);
	}
	
	/**
	 * Log a message of level DEBUG
	 * replaces \1 and {1} with 1st parameter in message, and so on
	 * @param message
	 */
	public void debug(String message, Object... objetcs){
		log(message,Level.DEBUG, objetcs);
	}
	
	/**
	 * Log a message of level DEBUG
	 * replaces \1 and {1} with 1st parameter in message, and so on
	 * @param message
	 */
	public void log(String message, Object... objetcs){
		log(message,Level.DEBUG, objetcs);
	}
	
	public LogWriter getWriter(Level lvl){
		return new LogWriter(this,lvl);
	}
	
	public LogPrintWriter getPrintWriter(Level lvl){
		return new LogPrintWriter(getWriter(lvl));
	}
	
	public LogPrintWriter getInfoPrintWriter(){
		return getPrintWriter(Level.INFO);
	}
	
	public LogPrintWriter getErrorPrintWriter(){
		return getPrintWriter(Level.ERROR);
	}
	
	public LogPrintWriter getWarnPrintWriter(){
		return getPrintWriter(Level.WARN);
	}
	
	public LogPrintWriter getDebugPrintWriter(){
		return getPrintWriter(Level.DEBUG);
	}
	
	/**
	 * main method for tests
	 * @param args
	 */
	public static void main(String[] args){
		Logger log = new Logger(Logger.class);
		log.info("Test \\1 \\2","Hallo","Welt");
		log.error("Test2 {1} {2}","Hallo","Welt");
		log.debug("Test3 {1} {2}");
	}
}
