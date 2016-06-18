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

public class ZhaoqingPrecessor implements PageProcessor {
	public static final String URL_LIST = "http://ggzy\\.zhaoqing\\.gov\\.cn/ggzyjy/jzgcjy/jyggjs/index*";
	public static final String URL_DETAILS = "http://ggzy\\.zhaoqing\\.gov\\.cn/ggzyjy/jzgcjy/jyggjs/\\d+/t\\d+\\_\\d+\\.html";

	public static String url = "http://ggzy.zhaoqing.gov.cn/ggzyjy/jzgcjy/jyggjs/index.html";
	public static String test = "http://ggzy.zhaoqing.gov.cn/ggzyjy/jzgcjy/jyggjs/201606/t20160614_387866.html";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(10000).setSleepTime(300);
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
			// 48
			for (int i = 1; i < 40; i++) {
				urls.add("http://ggzy.zhaoqing.gov.cn/ggzyjy/jzgcjy/jyggjs/index_" + i + ".html");
			}
			page.addTargetRequests(urls);
			System.out.println("这一页一共有  " + urls.size() + " 条数据");

			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			List<String> urls = page.getHtml().xpath("//div[@id=\"gl_right\"]").links().regex(URL_DETAILS).all();
			System.out.println("从列表页获取的详情数目" + urls.size());
			if (urls != null && urls.size() > 0) {
				page.addTargetRequests(urls);
			}

		}

		if (page.getUrl().regex(URL_DETAILS).match()) {

			Project project = new Project();
			Element main = doc.getElementsByAttributeValue("class", "main").get(0);
			String project_name = main.select("h4").get(0).text();
			String project_publicStart = main.select("h6").select("label").get(0).text().replace("发布日期：", "");

			StringBuffer project_article = new StringBuffer();
			for (Element style : main.getElementsByAttributeValue("class", "TRS_Editor").get(0).children()) {
				if (style.children().size() > 8) {
					for (Element p : style.children()) {
						project_article.append(p.text()).append("\n");
					}
				} else {
					project_article.append(style.text()).append("\n");

				}
			}
		

			project.setProjectName(project_name);
			project.setPublicStart(project_publicStart);
			project.setArticle(project_article.toString());
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("肇庆市");
			project.setTime(MyUtils.getcurentTime());
			project.setRawHtml(main.toString());

			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}

	}

}
