package jp.yappo.logminimal;

import java.util.Calendar;

public class Log {

	private static Logger defaultLogger = (time, type, message, rawMessage, trace) -> System.err.println("[" + time + "] [" + type + "] " + message + " at " + trace);
	private static Logger logger = defaultLogger;
	static public void setLogger(Logger newLogger) {
		logger = newLogger;
	}

	static public void setDefaultLogger() {
		logger = defaultLogger;
	}	
	
	private static Calendar testCalendar = null;
	static protected void setTestCalendar(Calendar cal) {
		testCalendar = cal;
	}
	
	static public void critf(String message) {	
		log("CRITICAL", message);
	}
	static public void critf(String format, Object... args) {	
		log("CRITICAL", String.format(format, args));
	}
	
	static public void warnf(String message) {	
		log("WARN", message);
	}
	static public void warnf(String format, Object... args) {	
		log("WARN", String.format(format, args));
	}
	
	static public void infof(String message) {	
		log("INFO", message);
	}
	static public void infof(String format, Object... args) {	
		log("INFO", String.format(format, args));
	}
	
	static public void debugf(String message) {	
		log("DEBUG", message);
	}
	static public void debugf(String format, Object... args) {	
		log("DEBUG", String.format(format, args));
	}

	
	static private void log (String type, String message) {
		Calendar currentCalendar = testCalendar == null ? Calendar.getInstance() : testCalendar; 

		int year   = currentCalendar.get(Calendar.YEAR);
		int month  = currentCalendar.get(Calendar.MONTH);
		int day    = currentCalendar.get(Calendar.DAY_OF_MONTH);
		int hour   = currentCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = currentCalendar.get(Calendar.MINUTE);
		int second = currentCalendar.get(Calendar.SECOND);
		// no use String.format because String.format is too slow. 
		String date = year + "/" +
			(month  < 10 ? "0" + month  : month)  + "/" +
			(day    < 10 ? "0" + day    : day)   + " " +
			(hour   < 10 ? "0" + hour   : hour)  + ":" +
			(minute < 10 ? "0" + minute : minute) + ":" +
			(second < 10 ? "0" + second : second);
	
		logger.print(date, type, message, message, new Throwable().getStackTrace()[2].toString());
	}
	
	@FunctionalInterface
	interface Logger {
		public void print(String time, String type, String message, String rawMessage, String trace);
	}
}
