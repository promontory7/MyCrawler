package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 广东交通运输厅公众网
 * @author hehe
 *
 */
public class GuangDongTransportationProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.gdcd\\.gov\\.cn/gkzb/Index\\d+\\.shtml";
	public static final String URL_DETAILS = "http://www\\.gdcd\\.gov\\.cn/gkzb/\\d+_1.shtml";

	public static String url = "http://www.gdcd.gov.cn/gkzb/Index0.shtml";
	public static String test = "http://www.gdcd.gov.cn/gkzb/20160113114040707_1.shtml";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {

		Document doc = Jsoup.parse(page.getHtml().toString());
		
		if(isFirst){
			System.out.println("添加所有列表链接");
			ArrayList<String> urls =new ArrayList<String>();
			for(int i=1;i<20;i++){
				urls.add("http://www.gdcd.gov.cn/gkzb/Index"+i+".shtml");
			}
			page.addTargetRequests(urls);
			isFirst=false;
		}
		
		if(page.getUrl().regex(URL_LIST).match()){
			System.out.println("获取列表数据");

			List<String> urls = page.getHtml().xpath("//label[@id=\"Title\"]").links().regex(URL_DETAILS).all();
			System.out.println(urls.size());
			if(urls!=null&&urls.size()>0){
				page.addTargetRequests(urls);
			}
		}

		if (page.getUrl().regex(URL_DETAILS).match()) {

			System.out.println(doc.getElementById("Title").text());
			System.out.println(doc.getElementById("Source").text());
			System.out.println(doc.getElementById("Time").text());
			System.out.println(doc.getElementById("Content").text());
			// Elements td = doc.getElementsByAttributeValue("class",
			// "MsoNormal");
			// System.out.println(td.size());
		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
