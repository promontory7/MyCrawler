package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.metamodel.relational.Size;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.CacheHashMap;

public class ShaoguangProcessor implements PageProcessor{
	
	public static final String URL_LIST = "http://www\\.sgjyzx\\.com/businessAnnounceAction\\!frontBusinessAnnounceListChildren\\.do\\?businessAnnounce\\.announcetype\\=12\\&page\\=\\d+";
	public static final String URL_DETAILS = "http://www\\.sgjyzx\\.com/businessAnnounceAction\\!frontToBusinessAnnounceForm\\.do\\?businessAnnounce\\.id=*";

	// 市值
	public static String url = "http://www.sgjyzx.com/businessAnnounceAction!frontBusinessAnnounceListChildren.do?businessAnnounce.announcetype=12&page=1";

	public static String test = "http://www.sgjyzx.com/businessAnnounceAction!frontToBusinessAnnounceForm.do?businessAnnounce.id=ebb33d52b9824471be5cd443b0fe9d04";
	public static String detailStart = "http://www.sgjyzx.com/businessAnnounceAction!frontToBusinessAnnounceForm.do?businessAnnounce.id=";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;
	
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 2; i < 10; i++) {
				urls.add("http://www.sgjyzx.com/businessAnnounceAction!frontBusinessAnnounceListChildren.do?businessAnnounce.announcetype=12&page=" + i);
			}
			System.out.println("url的总数是：" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		Document doc = Jsoup.parse(page.getHtml().toString());
		
		if (page.getUrl().regex(URL_LIST).match()) {
			Elements trs = doc.getElementsByAttributeValue("class", "listPanel").select("tbody").select("tr");
			for (Element tr : trs) {
				Elements tds=tr.select("td");
				if (tds.size()==3) {
					String id=tds.get(1).select("a").toString().substring(11, 43);
					CacheHashMap.cache.put(id, tds.get(1).text() + "###" + tds.get(2).text());
					page.addTargetRequest(detailStart+id);
					System.out.println(CacheHashMap.cache.get(id));
				}
			}
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Elements trs = doc.getElementsByAttributeValue("class", "xx-text");
		}
		
		
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
