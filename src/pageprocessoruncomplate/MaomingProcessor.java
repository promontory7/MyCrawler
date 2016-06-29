package pageprocessoruncomplate;

import java.util.ArrayList;

import org.hibernate.sql.Select;
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

public class MaomingProcessor implements PageProcessor {
	public static final String URL_LIST = "http://mmgpc\\.maoming\\.gov\\.cn/mmzbtb/jyxx/033001/033001001/033001001001/033001001001001/\\?Paging\\=\\d+";
	public static final String URL_DETAILS = "http://mmgpc\\.maoming\\.gov\\.cn/mmzbtb/infodetail/\\?infoid\\=*";

	public static String url = "http://mmgpc.maoming.gov.cn/mmzbtb/jyxx/033001/033001001/033001001001/033001001001001/?Paging=1";
	public static String test = "http://mmgpc.maoming.gov.cn/mmzbtb/infodetail/?infoid=aa7ee4b5-2777-4e1a-a559-2fd012be13a8&categoryNum=";

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
			// 18
			for (int i = 2; i < 15; i++) {
				urls.add(
						"http://mmgpc.maoming.gov.cn/mmzbtb/jyxx/033001/033001001/033001001001/033001001001001/?Paging="
								+ i);
			}
			page.addTargetRequests(urls);
			System.out.println("这一页一共有  " + urls.size() + " 条数据");

			isFirst = false;
		}

		if (page.getUrl().regex(URL_LIST).match() || page.getUrl().toString().trim().equals(url)) {
			System.out.println("获取列表数据");

			Elements tbodys = doc.getElementsByAttributeValue("class", "lrline").select("tbody");
			for (Element tbody : tbodys) {
				if (tbody.children().size() > 15) {
					System.out.println("在这里");

					for (Element tr : tbody.children()) {
						if (tr.children().size() > 2) {
							Elements tds = tr.select("td");
							String url = tds.get(0).select("a").get(0).attr("href");
							String title = tds.get(0).text();
							String data = tds.get(2).text();

							CacheHashMap.cache.put(url, title + "###" + data);
							MyUtils.addRequestToPage(page, url);
							System.out.println(url + "  " + CacheHashMap.cache.get(url));
						}
					}
				}
			}

		}

		if (page.getUrl().regex(URL_DETAILS).match()) {

			Project project = new Project();
			StringBuffer project_article = new StringBuffer();

			String urldetails = CacheHashMap.cache.get(page.getUrl().toString().trim());

			String[] value = urldetails.split("###");
			if (value != null && value.length > 1) {
				project.setProjectName(value[0]);
				project.setPublicStart(value[1]);
			}

			Element td = doc.getElementById("TDContent");
			for (Element p : td.select("div").get(0).children()) {
				project_article.append(p.text()).append("\n");
			}
			project.setUrl(page.getUrl().toString().trim());
			project.setState(0);
			project.setWebsiteType("茂名市");
			project.setTime(MyUtils.getcurentTime());
			project.setRawHtml(td.select("div").get(0).toString());

			project.setArticle(project_article.toString());
			System.out.println(project);

			HibernateUtil.save2Hibernate(project);

		}
	}

}
