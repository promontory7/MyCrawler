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

public class MeizhouProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.mzggzy\\.com/list/index*";
	public static final String URL_DETAILS = "http://www\\.mzggzy\\.com/show/index*";

	// 市值
	public static String url = "http://www.mzggzy.com/list/index/1722/1";

	public static String test = "http://www.mzggzy.com/show/index/1722/36129";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			urls.add("http://www.mzggzy.com/list/index/1723/1");
			for (int i = 2; i < 20; i++) {
				urls.add("http://www.mzggzy.com/list/index/1722/" + i);
				urls.add("http://www.mzggzy.com/list/index/1723/" + i);

			}
			System.out.println("url的总数是：" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("进入List");

			Elements lis = doc.getElementsByAttributeValue("class", "list").select("ul").select("li");
			for (Element li : lis) {
				String url = li.select("a").attr("href").trim();
				page.addTargetRequest(url);
				CacheHashMap.cache.put(url, li.select("a").text().trim() + "###" + li.select("span").text().trim());
				System.out.println(CacheHashMap.cache.get(url));
			}
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			System.out.println("进入Detail");

			Project project = new Project();

			StringBuffer projectArticle = new StringBuffer();
			String projectName = null;
			String projectPublicStart = null;

//			Elements elements = doc.getElementsByAttributeValue("class", "size13");
//			if (elements != null && elements.size() > 0) {
//				if (elements.get(0).select("div").size() > 0) {
//					for (Element child : elements.get(0).children()) {
//						if (child.nodeName().equals("div")) {
//							for (Element last : child.children()) {
//								projectArticle.append(last.text().trim()).append("\n");
//							}
//						} else {
//							projectArticle.append(child.text().trim()).append("\n");
//						}
//					}
//
//				} else if (elements.get(0).select("table").size() > 0) {
//					Elements ps = elements.get(0).getElementsByAttributeValue("class", "WordSection1");
//					for (Element p : ps.get(0).children()) {
//						projectArticle.append(p.text().trim()).append("\n");
//					}
//
//				} else {
//					for (Element p : elements.get(0).children()) {
//						projectArticle.append(p.text().trim()).append("\n");
//					}
//				}
//			} else {
//				System.out.println("--------------------------------------" + page.getUrl().toString());
//			}
			Elements elements = doc.getElementsByAttributeValue("class", "MsoNormal");
			for(Element element:elements){
				projectArticle.append(element.text().trim()).append("\n");
			}

			String value = CacheHashMap.cache.get(page.getUrl().toString());
			projectName = value.split("###")[0];
			projectPublicStart = value.split("###")[1];

			project.setTime(MyUtils.getcurentTime());
			project.setWebsiteType("meizhou");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setProjectName(projectName);
			project.setPublicStart(projectPublicStart);
			project.setArticle(projectArticle.toString());
			System.out.println(project.toString());

			HibernateUtil.save2Hibernate(project);

		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
