package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {
	public static String getcurentTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
}
