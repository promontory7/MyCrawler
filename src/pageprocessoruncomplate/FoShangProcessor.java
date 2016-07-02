package pageprocessoruncomplate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Project;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import utils.HibernateUtil;
import utils.SessionFactoryUtil;
import utils.MyUtils;

public class FoShangProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.fsggzy\\.cn/gcjy/gc_zbxx/gc_zbsz/index*";
	public static final String URL_DETAILS = "http://www\\.fsggzy\\.cn/gcjy/gc_zbxx/gc_zbsz/\\d+/t\\d+_\\d+\\.html";

	// 市值
	public static String url = "http://www.fsggzy.cn/gcjy/gc_zbxx/gc_zbsz/index.html";
	// 各区
	public static String url1 = "http://www.fsggzy.cn/gcjy/gc_zbxx/jygg_gq/index.html";

	public static String test = "http://www.fsggzy.cn/gcjy/gc_zbxx/gc_zbsz/201603/t20160325_5552037.html";

	private Site site = Site.me().setRetryTimes(3).setTimeOut(20000).setSleepTime(100);
	private static boolean isFirst = true;

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {
			System.out.println("添加所有列表链接");
			ArrayList<String> urls = new ArrayList<String>();
			// 50
			for (int i = 1; i < 30; i++) {
				urls.add("http://www.fsggzy.cn/gcjy/gc_zbxx/gc_zbsz/index_" + i + ".html");
				urls.add("http://www.fsggzy.cn/gcjy/gc_zbxx/jygg_gq/index_" + i + ".html");
			}
			urls.add("http://www.fsggzy.cn/gcjy/gc_zbxx/jygg_gq/index.html");
			System.out.println("url的总数是：" + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match()) {
			System.out.println("获取列表数据");

			List<String> urls = page.getHtml().xpath("//a[@id=\"title_link\"]").links().regex(URL_DETAILS).all();
			System.out.println(urls.size());
			if (urls != null && urls.size() > 0) {
				for (int i = 0; i < urls.size(); i++) {
					MyUtils.addRequestToPage(page, urls.get(i));
				}
			}
		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Project project = new Project();

			String projectRAWHtml = doc.getElementsByAttributeValue("class", "contentrightlistbox2").toString();
			String projectName = doc.getElementsByAttributeValue("class", "contenttitle2").select("h3").text();
			String projectPublicStart = doc.getElementsByAttributeValue("class", "fbtime").text();
			Elements mElements = doc.getElementsByAttributeValue("class", "content2").select("p");
			StringBuffer article = new StringBuffer();
			for (Element p : mElements) {
				article.append(p.text()).append("\n");
			}

			project.setWebsiteType("佛山市");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setTime(MyUtils.getcurentTime());
			project.setProjectName(projectName);
			project.setPublicStart(projectPublicStart);
			project.setArticle(article.toString());
			project.setRawHtml(projectRAWHtml);
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
