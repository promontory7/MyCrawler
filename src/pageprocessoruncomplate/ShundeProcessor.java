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

public class ShundeProcessor implements PageProcessor {

	public static final String URL_LIST = "http://www\\.sdggzy\\.cn/page\\.php\\?page\\=\\d+\\&singleid\\=3\\&ClassID\\=1";
	public static final String URL_DETAILS = "http://www\\.sdggzy\\.cn/viewzbggnew\\.php\\?id\\=*";

	public static String url = "http://www.sdggzy.cn/page.php?page=0&singleid=3&ClassID=1";
	public static String test = "http://www.sdggzy.cn/viewzbggnew.php?id=R0MyMDE2KFNEKVhaMDEzMg==";
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
		Document doc = Jsoup.parse(page.getHtml().toString());

		if (isFirst) {

			ArrayList<String> urls = new ArrayList<String>();
			// 18
			for (int i = 1; i < 10; i++) {
				urls.add("http://www.sdggzy.cn/page.php?page=" + i + "&singleid=3&ClassID=1");
			}
			System.out.println("添加所有列表链接      " + urls.size());
			page.addTargetRequests(urls);
			isFirst = false;
		}
		if (page.getUrl().regex(URL_LIST).match()) {
			Elements trs = doc.getElementsByAttributeValue("class", "lst").get(0).select("tr");
			for (Element tr : trs) {

				Elements td = tr.select("td");
				if (td.size() >= 3) {
					String url = td.get(0).select("a").attr("href").trim();
					String title = td.get(0).text();
					String data = td.get(2).text();
					String no = td.get(1).text();
					CacheHashMap.cache.put(url, title + "###" + data + "###" + no);
					System.out.println(url + "   " + CacheHashMap.cache.get(url));
					page.addTargetRequest(url);
				} else {
					System.out.println("小于3？？？？？");
				}

			}

		}
		if (page.getUrl().regex(URL_DETAILS).match()) {
			Element div = doc.getElementById("zbggzw");

			Project project = new Project();

			String cacheString = CacheHashMap.cache.get(page.getUrl().toString().trim());
			String projectName = cacheString.split("###")[0];
			String publicStart = cacheString.split("###")[1];
			String projectNo = cacheString.split("###")[2];
			String rawhtml = div.toString();

			StringBuffer projectArticle = new StringBuffer();
			Elements tds = div.select("td");
			for (Element p : tds.get(0).children()) {
				projectArticle.append(p.text()).append("\n");
			}

			project.setWebsiteType("顺德市");
			project.setState(0);
			project.setUrl(page.getUrl().toString());
			project.setTime(MyUtils.getcurentTime());
			project.setProjectName(projectName);
			project.setArticle(projectArticle.toString());
			project.setProjectNo(projectNo);
			project.setPublicStart(publicStart);
			project.setRawHtml(rawhtml);
			System.out.println(project.toString());
			HibernateUtil.save2Hibernate(project);
		}
	}

}
