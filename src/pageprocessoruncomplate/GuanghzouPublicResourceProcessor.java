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
 * 广州公共资源交易网
 * 
 * @author hehe
 *
 */
public class GuanghzouPublicResourceProcessor implements PageProcessor {
	
	public static final String URL_LIST = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\w*";
	// 房建市政
	public static final String URL_LIST1 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=503&pchannelid=466&curgclb=01\\,02&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=1&cIndex=1&page=\\d+";
	// 交通
	public static final String URL_LIST3 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=510&channelids=15&pchannelid=467&curgclb=03&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=2&page=\\d+";
	// 电力
	public static final String URL_LIST2 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=515&channelids=15&pchannelid=468&curgclb=05&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=3&page=\\d+";
	// 铁路
	public static final String URL_LIST4 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=520&channelids=15&pchannelid=469&curgclb=06&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=2&pcurIndex=4&page=\\d+";
	// 水利
	public static final String URL_LIST5 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=525&channelids=15&pchannelid=470&curgclb=04&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=5&page=\\d+";
	// 民航
	public static final String URL_LIST6 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=539&channelids=15&pchannelid=471&curgclb=07&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=6&page=\\d+";
	// 园林
	public static final String URL_LIST7 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=543&channelids=15&pchannelid=472&curgclb=08&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=7&page=\\d+";
	// 小额
	public static final String URL_LIST8 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=530&channelids=15&pchannelid=473&curgclb=&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=2&pcurIndex=8&page=\\d+";
	// 其它
	public static final String URL_LIST9 = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout2/szlist\\.jsp\\?siteId=1&channelId=535&channelids=15&pchannelid=474&curgclb=13&curxmlb=01\\,02\\,03\\,04\\,05&curIndex=1&pcurIndex=9&page=\\d+";

	public static final String URL_DETAILS = "http://www\\.gzggzy\\.cn/cms/wz/view/index/layout3/index\\.jsp\\?siteId=\\d+&channelId=\\d+8&infoId=\\d+";

	public static String url = "http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=503&pchannelid=466&curgclb=01,02&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=1&cIndex=1&page=1";
	public static String test = "http://www.gzggzy.cn/cms/wz/view/index/layout3/index.jsp?siteId=1&channelId=628&infoId=458535";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 房建市政
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=503&pchannelid=466&curgclb=01,02&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=1&cIndex=1&page=0");
			// 交通
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=510&channelids=15&pchannelid=467&curgclb=03&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=2&page=0");
			// 电力
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=515&channelids=15&pchannelid=468&curgclb=05&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=3&page=0");
			// 铁路
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=520&channelids=15&pchannelid=469&curgclb=06&curxmlb=01,02,03,04,05&curIndex=2&pcurIndex=4&page=0");
			// 水利
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=525&channelids=15&pchannelid=470&curgclb=04&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=5&page=0");
			// 民航
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=539&channelids=15&pchannelid=471&curgclb=07&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=6&page=0");
			// 园林
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=543&channelids=15&pchannelid=472&curgclb=08&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=7&page=0");
			// 小额
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=530&channelids=15&pchannelid=473&curgclb=&curxmlb=01,02,03,04,05&curIndex=2&pcurIndex=8&page=0");
			// 其它
			urls.add(
					"http://www.gzggzy.cn/cms/wz/view/index/layout2/szlist.jsp?siteId=1&channelId=535&channelids=15&pchannelid=474&curgclb=13&curxmlb=01,02,03,04,05&curIndex=1&pcurIndex=9&page=0");

			System.out.println("全部的数量" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			List<String> urls = page.getHtml().xpath("//td[@class=\"text_left\"]").links().regex(URL_DETAILS).all();
			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);
			}
		}

		if (page.getUrl().regex(URL_DETAILS).match()) {
			System.out.println(doc.getElementsByAttributeValue("class", "Section1").select("p").get(0).text());
			System.out.println(doc.getElementsByAttributeValue("class", "Section1").text());
		}
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}
}
