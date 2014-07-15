package jp.yappo.logminimal;

import static jp.yappo.logminimal.Log.*;
import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class LogTest {
	static String logResult = "";
	

	@Test
	public void test() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, Calendar.JUNE, 9, 18, 19, 20);
		Log.setTestCalendar(calendar);

		Log.setLogger((time, type, message, rawMessage, trace) -> {
			LogTest.logResult = "[" + time + "] [" + type + "] " + message + " at " + trace;
		});

		critf("foo");
		assertEquals(LogTest.logResult, "[2014/06/09 18:19:20] [CRITICAL] foo at jp.yappo.logminimal.LogTest.test(LogTest.java:24)");
		warnf("foo");
		assertEquals(LogTest.logResult, "[2014/06/09 18:19:20] [WARN] foo at jp.yappo.logminimal.LogTest.test(LogTest.java:26)");
		infof("foo");
		assertEquals(LogTest.logResult, "[2014/06/09 18:19:20] [INFO] foo at jp.yappo.logminimal.LogTest.test(LogTest.java:28)");
		debugf("foo");
		assertEquals(LogTest.logResult, "[2014/06/09 18:19:20] [DEBUG] foo at jp.yappo.logminimal.LogTest.test(LogTest.java:30)");

		calendar.set(2014, Calendar.JUNE, 10, 18, 19, 20);
		Log.setTestCalendar(calendar);
		critf("%s %04d", "log", 1);
		assertEquals(LogTest.logResult, "[2014/06/10 18:19:20] [CRITICAL] log 0001 at jp.yappo.logminimal.LogTest.test(LogTest.java:35)");
		warnf("%s %04d", "log", 1);
		assertEquals(LogTest.logResult, "[2014/06/10 18:19:20] [WARN] log 0001 at jp.yappo.logminimal.LogTest.test(LogTest.java:37)");
		infof("%s %04d", "log", 1);
		assertEquals(LogTest.logResult, "[2014/06/10 18:19:20] [INFO] log 0001 at jp.yappo.logminimal.LogTest.test(LogTest.java:39)");
		debugf("%s %04d", "log", 1);
		assertEquals(LogTest.logResult, "[2014/06/10 18:19:20] [DEBUG] log 0001 at jp.yappo.logminimal.LogTest.test(LogTest.java:41)");

		critf("foo\nbar\rbaz\tblha");
		assertEquals(LogTest.logResult, "[2014/06/10 18:19:20] [CRITICAL] foo\\nbar\\rbaz\\tblha at jp.yappo.logminimal.LogTest.test(LogTest.java:44)");

		/*
		long startTime = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			critf("foo");			
		}
		System.out.println(((double)(System.nanoTime() - startTime))/1000000 + " ms.");

		startTime = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			critf("foo");			
		}
		System.out.println(((double)(System.nanoTime() - startTime))/1000000 + " ms.");

		startTime = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			critf("%s %04d", "log", 1);
		}
		System.out.println(((double)(System.nanoTime() - startTime))/1000000 + " ms.");
		 */

	}
}
