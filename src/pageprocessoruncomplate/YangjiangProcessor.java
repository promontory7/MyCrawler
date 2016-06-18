package pageprocessoruncomplate;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.CacheHashMap;
import utils.HibernateUtil;
import utils.MyUtils;

public class YangjiangProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.yjggzy\\.cn/Query/JsgcBidAfficheQuery2/d4f193435ad04447a997719474139181\\?page\\=\\d+";
	public static final String URL_DETAILS = "http://www\\.yjggzy\\.cn/Details/JsgcDetails/*";
	public static final String URL_DETAILS2 = "http://www\\.yjggzy\\.cn/JsgcTemplate/BidAfficheDetails/\\d+";

	public static String url = "http://www.yjggzy.cn/Query/JsgcBidAfficheQuery2/d4f193435ad04447a997719474139181?page=1";
	public static String test = "http://www.yjggzy.cn/Details/JsgcDetails/59fe6f287a404cbea10c40c300e915f9";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(30000).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().getFirstSourceText());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 80
			for (int i = 2; i < 30; i++) {
				urls.add("http://www.yjggzy.cn/Query/JsgcBidAfficheQuery2/d4f193435ad04447a997719474139181?page=" + i);
			}
			page.addTargetRequests(urls);
			System.out.println("这一页一共有  " + urls.size() + " 条数据");

			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			Elements lis = doc.getElementsByAttributeValue("class", "list").select("li");
			for (Element li : lis) {
				String url = null;
				String title = null;
				String data = null;

				for (Element element : li.children()) {
					if (element.nodeName().equals("a")) {
						url = element.attr("href").trim();
						title = element.text();
					}
					if (element.nodeName().equals("span")) {
						data = element.text();
					}

				}
				CacheHashMap.cache.put(url, title + "###" + data);
				page.addTargetRequest(url);
//				System.out.println(url + "  " + CacheHashMap.cache.get(url));

			}

		}

		if (page.getUrl().regex(URL_DETAILS).match()) {

			Project project = new Project();
			StringBuffer project_article = new StringBuffer();

			String urldetails = CacheHashMap.cache.get(page.getUrl().toString().trim());
			String rawhtml =doc.getElementsByAttributeValue("class", "acticle").toString();

			String[] value = urldetails.split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0]);
				project.setPublicStart(value[1]);
			}

			Elements ps = doc.getElementsByAttributeValue("class", "acticlecontent").select("dd").get(0).children();
			for (Element p : ps) {
				project_article.append(p.text()).append("\n");
			}
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("阳江市");
			project.setTime(MyUtils.getcurentTime());
			project.setRawHtml(rawhtml);

			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}

		if (page.getUrl().regex(URL_DETAILS2).match()) {

			Project project = new Project();
			StringBuffer project_article = new StringBuffer();

			String urldetails = CacheHashMap.cache.get(page.getUrl().toString().trim());

			String[] value = urldetails.split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0]);
				project.setPublicStart(value[1]);
			}

			Element div = doc.getElementById("nr").select("div").get(0);
			for (Element p : div.children()) {
				project_article.append(p.text()).append("\n");
			}
			String rawhtml =div.toString();
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("阳江市");
			project.setTime(MyUtils.getcurentTime());
			
			project.setArticle(project_article.toString());
			project.setRawHtml(rawhtml);
			
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}
	}

}
