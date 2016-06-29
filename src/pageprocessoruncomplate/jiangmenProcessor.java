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

public class jiangmenProcessor implements PageProcessor {
	public static final String URL_LIST = "http://zyjy.jiangmen.gov.cn//szqjszbgg/index*";
	public static final String URL_DETAILS = "http://zyjy\\.jiangmen\\.gov\\.cn//szqjszbgg/\\d+\\.htm";

	public static String url = "http://zyjy.jiangmen.gov.cn//zbgg/index.htm";
	public static String test = "http://zyjy.jiangmen.gov.cn//szqjszbgg/12935.htm";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(300);
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
			// 33
			for (int i = 2; i < 30; i++) {
				urls.add("http://zyjy.jiangmen.gov.cn//szqjszbgg/index_" + i + ".htm");
			}
			page.addTargetRequests(urls);
			System.out.println("这一页一共有  " + urls.size() + " 条数据");

			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match() || page.getUrl().toString().trim().equals(url)) {
			System.out.println("获取列表数据");

			Elements uls = doc.getElementsByAttributeValue("class", "c1-bline");
			for (Element ul : uls) {
				String url = ul.getElementsByAttributeValue("class", "f-left").get(0).select("a").attr("href").trim();
				String title = ul.getElementsByAttributeValue("class", "f-left").get(0).text();
				String data = ul.getElementsByAttributeValue("class", "f-right").get(0).text();
				CacheHashMap.cache.put(url, title + "###" + data);
				MyUtils.addRequestToPage(page, url);
				System.out.println(url + "  " + CacheHashMap.cache.get(url));
			}

		}
		if (page.getUrl().regex(URL_DETAILS).match()) {

			Project project = new Project();
			StringBuffer project_article = new StringBuffer();

			String urldetails = CacheHashMap.cache.get(page.getUrl().toString().trim());
			System.out.println("url" + page.getUrl().toString().trim());
			System.out.println("urldetails   " + urldetails);

			String[] value = urldetails.split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0]);
				project.setPublicStart(value[1]);
			}

			Elements divs = doc.getElementsByAttributeValue("class", "contlist minheight");
			for (Element div : divs.get(0).children()) {
				if (div.nodeName().equals("table")) {
					Elements trs = divs.select("tbody").select("tr");
					for (Element tr : trs) {
						project_article.append(tr.text()).append("\n");
					}
				} else {
					project_article.append(div.text()).append("\n");

				}
			}
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("江门市");
			project.setTime(MyUtils.getcurentTime());
			project.setRawHtml(divs.toString());

			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}

	}

}
