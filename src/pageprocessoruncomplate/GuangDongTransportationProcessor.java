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
import utils.HibernateUtil;
import utils.MyUtils;

/**
 * 广东交通运输厅公众网
 * 
 * @author hehe
 *
 */
public class GuangDongTransportationProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.gdcd\\.gov\\.cn/gkzb/index_\\d+\\.jhtml";
	public static final String URL_DETAILS = "http://www\\.gdcd\\.gov\\.cn\\:80/gkzb/\\d+_1\\.jhtml";
	public static final String URL_DETAILS2 = "http://www\\.gdcd\\.gov\\.cn/gkzb/\\d+_1\\.jhtml";

	public static String url = "http://www.gdcd.gov.cn/gkzb/index_1.jhtml";

	public static String test = "http://www.gdcd.gov.cn/gkzb/20151118154827224_1.jhtml";

	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	private static boolean isFirst = false;

	@Override
	public void process(Page page) {

		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 20
			for (int i = 2; i < 3; i++) {
				urls.add("http://www.gdcd.gov.cn/gkzb/index_" + i + ".jhtml");
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			List<String> urls = page.getHtml().links().regex(URL_DETAILS).all();
			System.out.println(urls.size());
			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);
			}
		}

		if (page.getUrl().regex(URL_DETAILS).match() || page.getUrl().regex(URL_DETAILS2).match()) {
			Project project = new Project();
			String project_name = null;
			String project_publicStart = null;
			StringBuffer project_article = new StringBuffer();

			Elements elements = doc.getElementsByAttributeValue("class", "detail_title");
			Elements h1s = elements.select("h1");
			if (h1s != null && h1s.size() > 0) {
				project_name = h1s.get(0).text();
			}
			Elements spans = elements.select("h3").select("span");
			if (spans != null && spans.size() > 2) {
				project_publicStart = spans.get(1).text();
			}

			
			Elements mains = doc.getElementsByAttributeValue("class", "detail_content").select("maincontent")
					.select("summary").select("main");
			System.out.println("main-------------"+doc.getElementsByAttributeValue("class", "detail_content").select("maincontent")
					.select("summary").get(0).getAllElements().get(0).getAllElements().toString());
			if (mains != null && mains.size() > 0) {
				Elements ps = mains.get(0).children();
				for (Element p : ps) {
					if (p.nodeName().equals("div")) {
						Elements tables = p.getElementsByAttributeValue("class", "MsoNormalTable");
						if (tables != null && tables.size() > 0) {
							for (Element table : tables) {
								Elements trs = table.select("tbody").select("tr");
								if (trs != null && trs.size() > 0) {
									for (Element tr : trs) {
										for (Element td : tr.select("td")) {
											project_article.append(td.text()).append("\n");
										}
									}
								}
							}
						} else {
							project_article.append(p.text()).append("\n");
						}

					} else {
						project_article.append(p.text()).append("\n");
					}

				}
			}else {
				for(Element element:doc.getElementsByAttributeValue("class", "detail_content").get(0).children()){
					project_article.append(element.text()).append("\n");
				}
			}

			project.setUrl(page.getUrl().toString());
			project.setState(0);
			project.setWebsiteType("guangdong_transportation");
			project.setTime(MyUtils.getcurentTime());
			project.setProjectName(project_name);
			project.setPublicStart(project_publicStart);
			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);
		}

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
