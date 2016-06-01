package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.List;

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

public class HeyuanProcessor implements PageProcessor {
	public static final String URL_LIST = "http://www.hyggzy.com/ggzy/jsgczbgg/index*";
	public static final String URL_DETAILS = "http://www\\.hyggzy\\.com/ggzy/jsgczbgg/\\d+/\\d+\\.html";

	public static String url = "http://www.hyggzy.com/ggzy/jsgczbgg/index.html";
	public static String urlFirst = "http://www.hyggzy.com/ggzy/jsgczbgg/index_";

	public static String test = "http://www.hyggzy.com/ggzy/jsgczbgg/20151123/219112.html";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 2; i < 20; i++) {
				urls.add(urlFirst + i + ".html");
			}
			System.out.println("url的总数是：" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			Elements lis = doc.getElementsByAttributeValue("class", "list2").select("li");
			System.out.println("lis.size()" + lis.size());
			for (Element li : lis) {
				String url = li.select("a").attr("href").trim();
				page.addTargetRequest(url);
				CacheHashMap.cache.put(url, li.select("a").text() + "###" + li.select("span").text());
				System.out.println("cache" + CacheHashMap.cache.get(url));
			}

			// List<String> urls = page.getHtml().xpath("//a[@class=\"b-left
			// title\"]").links().regex(URL_DETAILS).all();
			// System.out.println(urls.size());
			// if (urls != null && urls.size() > 0) {
			// page.addTargetRequests(urls);
			// }
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {

			StringBuffer article = new StringBuffer();
			Elements mElements = doc.getElementsByAttributeValue("class", "content-cnt");
			Elements all = mElements.get(0).children();
			System.out.println("all" + all.size());

			// for (Element element : all) {
			// article.append(element.text()).append("\n");
			//
			// }

			Elements trs = mElements.get(0).children().select("table").select("tbody").select("tr");
			if (trs.size() > 3) {
				for (Element tr : trs) {
					Elements tds = trs.select("td");
					for (Element td : tds) {
						Elements childs = td.children();
						if (childs.size() > 0) {
							for (Element child : childs) {
								article.append(child.text()).append("\n");
							}
						} else {
							article.append(td.text()).append("\n");
						}

					}
				}
			} else {
				for (Element child : all) {
					article.append(child.text()).append("\n");
				}
			}

			Project project = new Project();

			String cacheString = CacheHashMap.cache.get(page.getUrl().toString().trim());
			String projectName = cacheString.split("###")[0];
			String publicStart = cacheString.split("###")[1];

			project.setWebsiteType("heyuan");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setTime(MyUtils.getcurentTime());
			project.setProjectName(projectName);
			project.setArticle(article.toString());
			project.setPublicStart(publicStart);
			System.out.println(project.toString());

			HibernateUtil.save2Hibernate(project);

		}

	}

	public void getLineText(Elements elements, StringBuffer stringBuffer) {
		for (Element element : elements) {
			Elements childs = element.children();
			if (childs == null || childs.size() == 0) {
				stringBuffer.append(element.text()).append("\n");

			} else {
				getLineText(childs, stringBuffer);
			}
		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
