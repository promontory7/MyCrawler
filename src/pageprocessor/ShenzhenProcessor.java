package pageprocessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class ShenzhenProcessor implements PageProcessor {

	public static String url = "https://www.szjsjy.com.cn:8001/jyw/jyw/zbGongGao_View.do?ggguid=91e031be-b50f-431a-90c5-a2156f795949";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(10);

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		Document doc = Jsoup.parse(page.getHtml().toString());
		System.out.println(doc.body());

		Elements trs = doc.select("table");
		for (int i = 0; i < trs.size(); i++) {
			System.out.println(trs.text());
		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
