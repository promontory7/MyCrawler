package pageprocessor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator.IsFirstChild;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class Lixinji implements PageProcessor{

	private Site site = Site.me().setRetryTimes(3).setSleepTime(10).setCharset("gbk");
	public static String url = "http://www.chinaseed114.com/seed/shuidao/1.html";
	private boolean isFirst =true;
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 2; i < 50; i++) {
				urls.add("http://www.chinaseed114.com/seed/shuidao/" + i+".html");
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}
		String doc = Jsoup.parse(page.getHtml().toString()).toString();
		
		page.putField("url", page.getUrl());
		page.putField("html", doc);
		System.out.println(doc);
//		Elements li = doc.getElementsByAttributeValue("class", "px14");
//		for(Element data:li){
//			System.out.println(data.text());
//		}
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
