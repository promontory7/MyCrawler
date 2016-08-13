package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;

public class MyUtils {
	public static String getcurentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		System.out.println("yyyy-MM-dd HH:mm:ss   " + time);
		return time;
	}

	public static String getTime(String value) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		try {
			time = s.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s.format(time);

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
	
	public static boolean isInCompletedURL(String url){
		if (CacheHashMap.completedURL != null && CacheHashMap.completedURL.size() > 0) {
			if (CacheHashMap.completedURL.contains(url.trim())) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public static void addRequestToPage(Page page, String url) {
		if (CacheHashMap.completedURL != null && CacheHashMap.completedURL.size() > 0) {
			if (!CacheHashMap.completedURL.contains(url.trim())) {
				page.addTargetRequest(url.trim());
			}
		} else {
			page.addTargetRequest(url.trim());
		}

	}
	

}
