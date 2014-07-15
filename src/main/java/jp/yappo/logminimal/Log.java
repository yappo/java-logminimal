package jp.yappo.logminimal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	
		// white space escape
		StringBuilder escapedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			switch (c) {
				case '\r': {
					escapedMessage.append('\\');
					escapedMessage.append('r');
					break;
				}
				case '\n': {
					escapedMessage.append('\\');
					escapedMessage.append('n');
					break;
				}
				case '\t': {
					escapedMessage.append('\\');
					escapedMessage.append('t');
					break;
				}
				default: {
					escapedMessage.append(c);
					break;
				}
			}
		}

		String stack;
		try {
			stack = getStackTrace(2).toString();
		} catch (LogCallerException e) {
			stack = "(getStackTrace caught exception: " + e.getMessage() + ")";
		}
		logger.print(date, type, escapedMessage.toString(), message, stack);
	}

	// get stackTrace
	// getStackTraceElement is 5x faster than Throwable().getStackTrace()
	static private boolean getStackTraceElementInitialize = false;
	static private Method getStackTraceElement = null;
	static private StackTraceElement getStackTrace (int stack) throws LogCallerException {
		stack++;
		if (getStackTraceElementInitialize == false) {
			try {
				getStackTraceElement = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
				getStackTraceElement.setAccessible(true);
			} catch (NoSuchMethodException e) {
				getStackTraceElement = null;
			}
			getStackTraceElementInitialize = true;
		}

		if (getStackTraceElement == null) {
			return new Throwable().getStackTrace()[stack];
		} else {
			try {
				return (StackTraceElement) getStackTraceElement.invoke(new Throwable(), stack);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new LogCallerException(e);
			}
		}
	}

	static private class LogCallerException extends Exception {
		private static final long serialVersionUID = 2676683947539341764L;
		private LogCallerException(Exception e) {
			super(e);
		}
	}

	@FunctionalInterface
	interface Logger {
		public void print(String time, String type, String message, String rawMessage, String trace);
	}
}
