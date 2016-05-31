package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyUtils {
	public static String getcurentTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static void getLineText(Elements elements, StringBuffer stringBuffer) {
		for (Element element : elements) {
			Elements childs = element.children();
			if (childs == null || childs.size() == 0) {
				stringBuffer.append(element.text()).append("\n");

			} else {
				getLineText(childs, stringBuffer);
			}
		}

	}
}
